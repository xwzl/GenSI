package com.roy.gensi.genapp.domain.genservice.entity;

import com.alibaba.fastjson.JSONObject;

/**
 * @author ：楼兰
 * @date ：Created in 2021/5/7
 * @description:
 **/

public class SimpleResponseMsg {
    private SimpleRequestMsgHeader requestMsgHeader;

    private JSONObject responseMsgBody;

    public SimpleRequestMsgHeader getRequestMsgHeader() {
        return requestMsgHeader;
    }

    public void setRequestMsgHeader(SimpleRequestMsgHeader requestMsgHeader) {
        this.requestMsgHeader = requestMsgHeader;
    }

    public JSONObject getResponseMsgBody() {
        return responseMsgBody;
    }

    public void setResponseMsgBody(JSONObject responseMsgBody) {
        this.responseMsgBody = responseMsgBody;
    }

    @Override
    public String toString() {
        return "SimpleResponseMsg{" +
                "requestMsgHeader=" + requestMsgHeader +
                ", responseMsgBody=" + responseMsgBody +
                '}';
    }

    public void getHeaderFromRequest(SimpleRequestMsg requestMsg){
        this.setRequestMsgHeader(requestMsg.getRequestMsgHeader());
        this.setResponseMsgBody(null);
    }

    public void initRspTime() {
        this.getRequestMsgHeader().initRespTime();
    }
}
