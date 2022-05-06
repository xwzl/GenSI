package com.roy.gensi.genapp.domain.genservice.entity;

import com.alibaba.fastjson.JSONObject;

/**
 * @author ：楼兰
 * @date ：Created in 2021/5/6
 * @description:
 **/

public class SimpleRequestMsg {

    private SimpleRequestMsgHeader requestMsgHeader;

    private JSONObject requestMsgBody;

    public SimpleRequestMsg(){

    }

    public SimpleRequestMsg(SimpleRequestMsgHeader requestMsgHeader, JSONObject requestMsgBody) {
        this.requestMsgHeader = requestMsgHeader;
        this.requestMsgBody = requestMsgBody;
    }

    public SimpleRequestMsgHeader getRequestMsgHeader() {
        return requestMsgHeader;
    }

    public void setRequestMsgHeader(SimpleRequestMsgHeader requestMsgHeader) {
        this.requestMsgHeader = requestMsgHeader;
    }

    public JSONObject getRequestMsgBody() {
        return requestMsgBody;
    }

    public void setRequestMsgBody(JSONObject requestMsgBody) {
        this.requestMsgBody = requestMsgBody;
    }

    @Override
    public String toString() {
        return "SimpleRequestMsg{" +
                "requestMsgHeader=" + requestMsgHeader +
                ", requestMsgBody=" + requestMsgBody +
                '}';
    }
}
