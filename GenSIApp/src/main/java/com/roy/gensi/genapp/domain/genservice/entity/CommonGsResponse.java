package com.roy.gensi.genapp.domain.genservice.entity;

import com.alibaba.fastjson.JSONObject;
import com.roy.gensi.genapp.domain.hisrequest.entity.GsHisRequest;

/**
 * @author ：楼兰
 * @date ：Created in 2021/5/6
 * @description:
 **/

public class CommonGsResponse {
    public static final String RESULT_CODE_SUCCESS="0";
    public static final String RESULT_CODE_FORMAT_ERROR="1";
    public static final String RESULT_CODE_SYSID_ERROR="2";
    public static final String RESULT_CODE_PARAM_ERROR="3";
    private String transId;

    private String sysId;

    private String result;

    private String desc;

    private String serviceCode;

    public CommonGsResponse(){

    }

    public CommonGsResponse(String serviceCode, String transId, String sysId, String result, String desc){
        this.serviceCode = serviceCode;
        this.transId = transId;
        this.result = result;
        this.desc = desc;
        this.sysId = sysId;
    }


    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getSysId() {
        return sysId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    public JSONObject toJsonFormat(){
        JSONObject res = new JSONObject();

        JSONObject header = new JSONObject();
        header.put("serviceCode", this.getServiceCode());
        header.put("transId", this.getTransId());

        JSONObject body = new JSONObject();
        body.put("result", this.getResult());
        body.put("desc", this.getDesc());

        res.put("header", header);
        res.put("body", body);

        return res;
    }
    //聚合之间的根对象可以互相引用。但是如果是跨领域的对象引用就要通过领域服务。
    //在这里，服务响应和历史记录可以认为是不太可能分割的聚合关系，所以可以互相引用。
    public void convertFromHisRequest(GsHisRequest gsHisRequest){
        this.setTransId(gsHisRequest.getTransid());
        this.setServiceCode(gsHisRequest.getServicecode());
        this.setResult(CommonGsResponse.RESULT_CODE_SUCCESS);
        this.setDesc(JSONObject.toJSONString(gsHisRequest).replace("\\",""));
        this.setSysId(gsHisRequest.getSysid());
    }
}
