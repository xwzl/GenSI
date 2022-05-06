package com.roy.gensi.genapp.domain.sysManage.entity;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.roy.gensi.util.RsaUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @author ：楼兰
 * @date ：Created in 2021/5/5
 * @description:
 **/
@TableName("gsmanage")
public class SysManage {
    @TableId
    private String sysid;

    private String sysname;

    private String authedip;

    private String username;

    private String pwd;

    private String notifyurl;

    private String notifyparam;

    private String deleteflag;

    private String reserve1;

    private String reserve2;

    private String privatekey;

    private String publickey;

    public String getSysid() {
        return sysid;
    }

    public void setSysid(String sysid) {
        this.sysid = sysid;
    }

    public String getSysname() {
        return sysname;
    }

    public void setSysname(String sysname) {
        this.sysname = sysname;
    }

    public String getAuthedip() {
        return authedip;
    }

    public void setAuthedip(String authedip) {
        this.authedip = authedip;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getNotifyurl() {
        return notifyurl;
    }

    public void setNotifyurl(String notifyurl) {
        this.notifyurl = notifyurl;
    }

    public String getNotifyparam() {
        return notifyparam;
    }

    public void setNotifyparam(String notifyparam) {
        this.notifyparam = notifyparam;
    }

    public String getDeleteflag() {
        return deleteflag;
    }

    public void setDeleteflag(String deleteflag) {
        this.deleteflag = deleteflag;
    }

    public String getReserve1() {
        return reserve1;
    }

    public void setReserve1(String reserve1) {
        this.reserve1 = reserve1;
    }

    public String getReserve2() {
        return reserve2;
    }

    public void setReserve2(String reserve2) {
        this.reserve2 = reserve2;
    }

    public String getPrivatekey() {
        return privatekey;
    }

    public void setPrivatekey(String privatekey) {
        this.privatekey = privatekey;
    }

    public String getPublickey() {
        return publickey;
    }

    public void setPublickey(String publickey) {
        this.publickey = publickey;
    }

    @Override
    public String toString() {
        return "SysManage{" +
                "sysid='" + sysid + '\'' +
                ", sysname='" + sysname + '\'' +
                ", authedip='" + authedip + '\'' +
                ", username='" + username + '\'' +
                ", pwd='" + pwd + '\'' +
                ", notifyurl='" + notifyurl + '\'' +
                ", notifyparam='" + notifyparam + '\'' +
                ", deleteflag='" + deleteflag + '\'' +
                ", reserve1='" + reserve1 + '\'' +
                ", reserve2='" + reserve2 + '\'' +
                ", privatekey='" + privatekey + '\'' +
                ", publickey='" + publickey + '\'' +
                '}';
    }

    public void generateKey(){
        if(StringUtils.isEmpty(this.getPrivatekey()) || StringUtils.isEmpty(this.getPublickey())){
            JSONObject newRsaKey = RsaUtils.generateKeyPairForJava();
            this.setPublickey(newRsaKey.getString("publicKey"));
            this.setPrivatekey(newRsaKey.getString("privateKey"));
        }
    }
}
