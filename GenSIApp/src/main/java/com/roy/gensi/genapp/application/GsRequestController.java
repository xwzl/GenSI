package com.roy.gensi.genapp.application;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.roy.gensi.config.GsLogConfig;
import com.roy.gensi.genapp.domain.genservice.entity.*;
import com.roy.gensi.genapp.domain.genservice.service.AsyncBusiService;
import com.roy.gensi.genapp.domain.genservice.service.DecryptService;
import com.roy.gensi.genapp.domain.genservice.service.SyncBusiService;
import com.roy.gensi.genapp.domain.hisrequest.entity.GsHisRequest;
import com.roy.gensi.genapp.domain.hisrequest.service.GsRequestDomainService;
import com.roy.gensi.genapp.domain.sysManage.service.SysManageDomainService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author ：楼兰
 * @date ：Created in 2021/5/6
 * @description:
 **/
@RestController
@RequestMapping(value = "/genSI")
@Api("GenSI服务请求接口")
public class GsRequestController {

    private Logger logger = Logger.getLogger(getClass());
    @Autowired
    SysManageDomainService sysManageDomainService;
    @Autowired
    GsRequestDomainService gsRequestDomainService;
    @Autowired
    GsLogConfig gsLogConfig;
    @Autowired
    DecryptService decryptService;
    @Autowired
    AsyncBusiService asyncBusiService;
    @Autowired
    SyncBusiService syncBusiService;

    @ApiOperation(value = "GenSI Restful接口", notes = "内部调试接口")
    @RequestMapping(value = "/gsServiceTest", method = RequestMethod.POST)
    public Object gsRestInterface(@RequestBody String requestMessage, HttpServletRequest request) {
        logger.info("GsRestInterface: request=> " + requestMessage);
        JSONObject jRequestMessage = JSONObject.parseObject(requestMessage);
        SimpleRequestMsgHeader reqHeader = jRequestMessage.getObject("header", SimpleRequestMsgHeader.class);
        reqHeader.initIntime();
        JSONObject jRequestBody = jRequestMessage.getJSONObject("body");

        SimpleRequestMsg requestMsg = new SimpleRequestMsg(reqHeader, jRequestBody);

        //检查报文 报文检查不通过就直接返回响应。
        CommonGsResponse response = this.checkSimpleMsg(requestMsg, sysManageDomainService);
        if (!response.getResult().equals(CommonGsResponse.RESULT_CODE_SUCCESS)) {
            return response;
        }
        gsLogConfig.addTransLogAppender(logger, reqHeader.getTransId());
        //处理业务
        this.dealMessageAsync(requestMsg);
        logger.info("GsRestInterface: 返回请求响应  => " + response.toJsonFormat());
        gsLogConfig.removeTransLogAppender(logger);
        return response;
    }

    private void dealMessageAsync(SimpleRequestMsg requestMsg) {

        SimpleRequestMsgHeader reqHeader = requestMsg.getRequestMsgHeader();
        logger.info("GsRestInterface: 请求验证通过 request=> " + requestMsg);
        //报文检查通过后，同步返回请求接收成功的响应，异步推送业务处理结果
        final List<GsHisRequest> gsRequests = gsRequestDomainService.queryGsRequest(reqHeader.getTransId(), reqHeader.getServiceCode());
        String hisResponse = "";
        if (null != gsRequests && !gsRequests.isEmpty()) {
            //重复的请求，直接推送之前的响应结果。
            hisResponse = gsRequests.get(0).getRspbody();
            logger.info("GsRestInterface: 查询到历史请求记录，响应结果：hisResponse => " + hisResponse);
            sysManageDomainService.sendMessageAsync(reqHeader.getSysId(), hisResponse);
        } else {
            //处理实际业务
            asyncBusiService.doBusi(requestMsg,logger);
        }
    }

    private SimpleResponseMsg dealMessageSync(SimpleRequestMsg requestMsg) {
        SimpleResponseMsg response = new SimpleResponseMsg();
        SimpleRequestMsgHeader reqHeader = requestMsg.getRequestMsgHeader();
        logger.info("GsRestInterface: 请求验证通过 request=> " + requestMsg);
        //报文检查通过后，同步返回请求接收成功的响应，异步推送业务处理结果
        final List<GsHisRequest> gsRequests = gsRequestDomainService.queryGsRequest(reqHeader.getTransId(), reqHeader.getServiceCode());
        String hisResponse = "";
        if (null != gsRequests && !gsRequests.isEmpty()) {
            //重复的请求，直接推送之前的响应结果。
            hisResponse = gsRequests.get(0).getRspbody();
            logger.info("GsRestInterface: 查询到历史请求记录，响应结果：hisResponse => " + hisResponse);
            response = JSON.parseObject(hisResponse, SimpleResponseMsg.class);
        } else {
            //处理实际业务
            response = syncBusiService.doBusi(requestMsg,logger);
        }
        return response;
    }

    private CommonGsResponse checkSimpleMsg(SimpleRequestMsg requestMsg, SysManageDomainService sysManageDomainService) {
        CommonGsResponse response = null;
        final SimpleRequestMsgHeader requestMsgHeader = requestMsg.getRequestMsgHeader();
        final JSONObject requestMsgBody = requestMsg.getRequestMsgBody();
        if (null == requestMsgHeader || null == requestMsgBody) {
            response = new CommonGsResponse("", "", "", CommonGsResponse.RESULT_CODE_FORMAT_ERROR, "接口数据错误");
        } else if (StringUtils.isEmpty(requestMsgHeader.getTransId()) ||
                StringUtils.isEmpty(requestMsgHeader.getServiceCode()) ||
                StringUtils.isEmpty(requestMsgHeader.getSysId())) {
            response = new CommonGsResponse("", "", "", CommonGsResponse.RESULT_CODE_PARAM_ERROR, "参数处理错误");
        } else if (!sysManageDomainService.isContainSys(requestMsgHeader.getSysId())) {
            response = new CommonGsResponse("", "", "", CommonGsResponse.RESULT_CODE_SYSID_ERROR, "无效的系统标识");
        } else {
            response = new CommonGsResponse(requestMsgHeader.getServiceCode(), requestMsgHeader.getTransId(),
                    requestMsgHeader.getSysId(), CommonGsResponse.RESULT_CODE_SUCCESS, "请求接收成功");
        }
        return response;
    }

    @ApiOperation(value = "GenSI异步业务接口", notes = "配合客户端使用的正式接口。")
    @RequestMapping(value = "/gsServiceAsync", method = RequestMethod.POST)
    public Object gsServiceAsync(String ftRequestInfo, HttpServletRequest request) {
        logger.info("gsServiceAsync: received Request => " + ftRequestInfo);
        // 报文解密
        SimpleRequestMsg requestMsg;
        try {
            requestMsg = decryptService.decrypData(ftRequestInfo);
        } catch (DecryptMessageException e) {
            return new CommonGsResponse("","","", CommonGsResponse.RESULT_CODE_PARAM_ERROR,e.getMessage());
        }
        requestMsg.getRequestMsgHeader().initIntime();

        CommonGsResponse response = this.checkSimpleMsg(requestMsg, sysManageDomainService);
        if (!response.getResult().equals(CommonGsResponse.RESULT_CODE_SUCCESS)) {
            return response;
        }
        gsLogConfig.addTransLogAppender(logger, requestMsg.getRequestMsgHeader().getTransId());
        //处理业务
        this.dealMessageAsync(requestMsg);
        logger.info("gsServiceAsync: 返回请求响应  => " + response.toJsonFormat());
        gsLogConfig.removeTransLogAppender(logger);
        return response;
    }

    @ApiOperation(value = "GenSI同步业务接口", notes = "配合客户端使用的正式接口。")
    @RequestMapping(value = "/gsServiceSync", method = RequestMethod.POST)
    public Object gsServiceSync(String ftRequestInfo, HttpServletRequest request) {
        logger.info("gsServiceSync: received Request => " + ftRequestInfo);
        // 报文解密
        SimpleRequestMsg requestMsg;
        try {
            requestMsg = decryptService.decrypData(ftRequestInfo);
        } catch (DecryptMessageException e) {
            return new CommonGsResponse("","","", CommonGsResponse.RESULT_CODE_PARAM_ERROR,e.getMessage());
        }
        requestMsg.getRequestMsgHeader().initIntime();

        CommonGsResponse response = this.checkSimpleMsg(requestMsg, sysManageDomainService);
        if (!response.getResult().equals(CommonGsResponse.RESULT_CODE_SUCCESS)) {
            return response;
        }
        gsLogConfig.addTransLogAppender(logger, requestMsg.getRequestMsgHeader().getTransId());
        //处理业务
        final SimpleResponseMsg simpleResponseMsg = this.dealMessageSync(requestMsg);
        logger.info("gsServiceSync: 返回请求响应  => " + simpleResponseMsg);
        return simpleResponseMsg;
    }
}
