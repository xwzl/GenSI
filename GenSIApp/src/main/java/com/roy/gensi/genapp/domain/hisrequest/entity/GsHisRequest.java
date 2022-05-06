package com.roy.gensi.genapp.domain.hisrequest.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @author ：楼兰
 * @date ：Created in 2021/5/5
 * @description:
 **/
@TableName("gsrequest")
public class GsHisRequest {
    @TableId
    private String transid;
    @TableId
    private String servicecode;

    private String reqbody;

    private String rspbody;

    private String intime;

    private String rsptime;

    private String sysid;

    public String getReqbody() {
        return reqbody;
    }

    public void setReqbody(String reqbody) {
        this.reqbody = reqbody;
    }

    public String getRspbody() {
        return rspbody;
    }

    public void setRspbody(String rspbody) {
        this.rspbody = rspbody;
    }

    public String getIntime() {
        return intime;
    }

    public void setIntime(String intime) {
        this.intime = intime;
    }

    public String getRsptime() {
        return rsptime;
    }

    public void setRsptime(String rsptime) {
        this.rsptime = rsptime;
    }

    public String getTransid() {
        return transid;
    }

    public void setTransid(String transid) {
        this.transid = transid;
    }

    public String getServicecode() {
        return servicecode;
    }

    public void setServicecode(String servicecode) {
        this.servicecode = servicecode;
    }

    public String getSysid() {
        return sysid;
    }

    public void setSysid(String sysid) {
        this.sysid = sysid;
    }

    @Override
    public String toString() {
        return "GsRequest{" +
                "transid='" + transid + '\'' +
                ", servicecode='" + servicecode + '\'' +
                ", reqbody='" + reqbody + '\'' +
                ", rspbody='" + rspbody + '\'' +
                ", intime='" + intime + '\'' +
                ", rsptime='" + rsptime + '\'' +
                ", sysid='" + sysid + '\'' +
                '}';
    }
}
