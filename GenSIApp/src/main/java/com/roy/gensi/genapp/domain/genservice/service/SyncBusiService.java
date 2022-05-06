package com.roy.gensi.genapp.domain.genservice.service;

import com.alibaba.fastjson.JSONObject;
import com.roy.gensi.genapp.domain.genservice.entity.SimpleRequestMsg;
import com.roy.gensi.genapp.domain.genservice.entity.SimpleRequestMsgHeader;
import com.roy.gensi.genapp.domain.genservice.entity.SimpleResponseMsg;
import com.roy.gensi.genapp.domain.genservice.repository.BusiServiceRepository;
import com.roy.gensi.genapp.domain.hisrequest.entity.GsHisRequest;
import com.roy.gensi.genapp.domain.hisrequest.repository.GsRequestRepository;
import com.roy.gensi.genapp.domain.sysManage.service.SysManageDomainService;
import com.roy.gensi.genapp.interfaces.GsService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ：楼兰
 * @date ：Created in 2021/5/7
 * @description: 同步业务防腐层
 **/
@Service
public class SyncBusiService {

    @Autowired
    BusiServiceRepository busiServiceRepository;

    @Autowired
    SysManageDomainService sysManageDomainService;

    @Autowired
    GsRequestRepository gsRequestRepository;

    public SimpleResponseMsg doBusi(SimpleRequestMsg requestMsg, Logger logger) {
        SimpleResponseMsg responseMsg = new SimpleResponseMsg();
        responseMsg.getHeaderFromRequest(requestMsg);
        final JSONObject responseBody = new JSONObject();

        final SimpleRequestMsgHeader requestMsgHeader = requestMsg.getRequestMsgHeader();
        GsService gsService = busiServiceRepository.getGsService(requestMsgHeader.getServiceCode());
        if (null == gsService) {
            logger.error("ServiceCode[" + requestMsgHeader.getServiceCode() + "] is not support yet.");
            responseBody.put("error", "ServiceCode[" + requestMsgHeader.getServiceCode() + "] is not support yet.");
            responseMsg.setResponseMsgBody(responseBody);
            return responseMsg;
        }

        logger.info("busi porcess entry => " + requestMsgHeader.getServiceCode());
        final JSONObject busiRes = gsService.doBusi(requestMsg);
        logger.info("busi Res => " + busiRes);

        responseMsg.setResponseMsgBody(busiRes);

        GsHisRequest gsHisRequest = new GsHisRequest();
        gsHisRequest.setTransid(requestMsgHeader.getTransId());
        gsHisRequest.setSysid(requestMsgHeader.getSysId());
        gsHisRequest.setServicecode(requestMsgHeader.getServiceCode());
        gsHisRequest.setReqbody(JSONObject.toJSONString(requestMsg));
        responseMsg.initRspTime();
        gsHisRequest.setRspbody(JSONObject.toJSONString(responseMsg));
        gsHisRequest.setIntime(requestMsgHeader.getInTime());
        gsHisRequest.setRsptime(requestMsgHeader.getRespTime());

        gsRequestRepository.addGsRequest(gsHisRequest);

        logger.info("history loaded: gsHisRequest => " + gsHisRequest);
        return responseMsg;
    }
}
