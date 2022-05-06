package com.roy.gensi.genapp.domain.genservice.entity;

/**
 * @author ：楼兰
 * @date ：Created in 2021/5/6
 * @description:
 **/

public class EncryptedRequestMsgHeader {
    private String sysId;
    private String sysUser;
    private String sysPwd;
    private String transId;
    private String serviceCode;
    private String key;//生成的AES key用RSA公钥加密后的加密串
    private String inTime="";
    private String respTime = "";

    public String getSysId() {
        return sysId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    public String getSysUser() {
        return sysUser;
    }

    public void setSysUser(String sysUser) {
        this.sysUser = sysUser;
    }

    public String getSysPwd() {
        return sysPwd;
    }

    public void setSysPwd(String sysPwd) {
        this.sysPwd = sysPwd;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public String getRespTime() {
        return respTime;
    }

    public void setRespTime(String respTime) {
        this.respTime = respTime;
    }

    @Override
    public String toString() {
        return "EncryptedRequestMsgHeader{" +
                "sysId='" + sysId + '\'' +
                ", sysUser='" + sysUser + '\'' +
                ", sysPwd='" + sysPwd + '\'' +
                ", transId='" + transId + '\'' +
                ", serviceCode='" + serviceCode + '\'' +
                ", key='" + key + '\'' +
                ", inTime='" + inTime + '\'' +
                ", respTime='" + respTime + '\'' +
                '}';
    }
}
