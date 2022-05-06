package com.roy.gensi.genapp.domain.genservice.service;

import com.alibaba.fastjson.JSONObject;
import com.roy.gensi.genapp.domain.genservice.entity.CommonGsResponse;
import com.roy.gensi.genapp.domain.genservice.entity.SimpleRequestMsg;
import com.roy.gensi.genapp.domain.genservice.entity.SimpleRequestMsgHeader;
import com.roy.gensi.genapp.domain.genservice.entity.SimpleResponseMsg;
import com.roy.gensi.genapp.domain.genservice.repository.BusiServiceRepository;
import com.roy.gensi.genapp.domain.hisrequest.entity.GsHisRequest;
import com.roy.gensi.genapp.domain.hisrequest.repository.GsRequestRepository;
import com.roy.gensi.genapp.domain.sysManage.entity.SysManage;
import com.roy.gensi.genapp.domain.sysManage.service.SysManageDomainService;
import com.roy.gensi.genapp.interfaces.GsService;
import com.roy.gensi.util.CommonHttpUtil;
import com.roy.gensi.util.ThreadPoolHolder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ：楼兰
 * @date ：Created in 2021/5/6
 * @description: 将所有外部业务抽象，这就相当于是个防腐层。以后如果需要将单体架构改成微服务架构，只需要在这一层修改具体业务服务的调用方式。
 **/
@Service
public class AsyncBusiService {

    @Autowired
    BusiServiceRepository busiServiceRepository;

    @Autowired
    SysManageDomainService sysManageDomainService;

    @Autowired
    GsRequestRepository gsRequestRepository;

    public void doBusi(SimpleRequestMsg requestMsg, Logger logger){
        final SimpleRequestMsgHeader requestMsgHeader = requestMsg.getRequestMsgHeader();
        final JSONObject requestMsgBody = requestMsg.getRequestMsgBody();

        GsService gsService = busiServiceRepository.getGsService(requestMsgHeader.getServiceCode());
        if(null == gsService){
            logger.error("ServiceCode["+requestMsgHeader.getServiceCode()+"] is not support yet.");
            return;
        }

        ThreadPoolHolder.callBusiExecutor.execute(()->{
            logger.info("busi porcess entry => "+requestMsgHeader.getServiceCode());
            final JSONObject busiRes = gsService.doBusi(requestMsg);
            logger.info("busi Res => "+busiRes);

            SimpleResponseMsg simpleResponseMsg = new SimpleResponseMsg();
            simpleResponseMsg.getHeaderFromRequest(requestMsg);
            simpleResponseMsg.setResponseMsgBody(busiRes);

            GsHisRequest gsHisRequest = new GsHisRequest();
            gsHisRequest.setTransid(requestMsgHeader.getTransId());
            gsHisRequest.setSysid(requestMsgHeader.getSysId());
            gsHisRequest.setServicecode(requestMsgHeader.getServiceCode());
            gsHisRequest.setReqbody(JSONObject.toJSONString(requestMsg));
            simpleResponseMsg.initRspTime();
            gsHisRequest.setRspbody(JSONObject.toJSONString(simpleResponseMsg));
            gsHisRequest.setIntime(requestMsgHeader.getInTime());
            gsHisRequest.setRsptime(requestMsgHeader.getRespTime());

            gsRequestRepository.addGsRequest(gsHisRequest);
            logger.info("history loaded: gsHisRequest => "+gsHisRequest);
            final SysManage sysManage = sysManageDomainService.getSysManage(requestMsgHeader.getSysId());
            CommonHttpUtil.sendHttpBodyRequest(sysManage.getNotifyurl(),JSONObject.toJSONString(simpleResponseMsg));
            logger.info("callback info sended to sysId => "+requestMsgHeader.getSysId());
        });
    }
}
