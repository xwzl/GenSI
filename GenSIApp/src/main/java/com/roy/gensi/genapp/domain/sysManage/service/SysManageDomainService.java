package com.roy.gensi.genapp.domain.sysManage.service;

import com.roy.gensi.genapp.domain.sysManage.entity.SysManage;
import com.roy.gensi.genapp.domain.sysManage.repository.SysManageRepository;
import com.roy.gensi.genapp.domain.sysManage.util.SysCache;
import com.roy.gensi.util.CommonHttpUtil;
import com.roy.gensi.util.ThreadPoolHolder;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ：楼兰
 * @date ：Created in 2021/5/5
 * @description: 领域服务
 **/

@Service
public class SysManageDomainService {

    private Logger logger = Logger.getLogger(this.getClass());
    @Autowired
    SysManageRepository sysManageRepository;
    @Autowired
    SysCache sysCache;
    public int saveSys(SysManage gsSys) {
        return sysManageRepository.saveSys(gsSys);
    }

    public int deleteSysById(String sysId) {
        return sysManageRepository.deleteSysById(sysId);
    }

    public List<SysManage> queryAllSys() {
        return sysManageRepository.queryAllSys();
    }

    public Boolean isContainSys(String sysId){
        return sysCache.checkSysId(sysId);
    }

    public SysManage getSysManage(String sysId){
        return sysCache.getSysManage(sysId);
    }

    public void sendMessageAsync(String sysId, String message){
        logger.info("异步推送报文:目标系统=>"+sysId+";报文内容 =》 "+message);
        ThreadPoolHolder.pushMessageExecutor.execute(()->{
            doSend(sysId,message);
        });
    }
    //FIXME 这里业务处理简单一点，只管向业务系统推送，业务系统有没有接收到就先不管了。
    private void doSend(String sysId, String message){
        try{
            final SysManage sysManage = sysCache.getSysManage(sysId);
            final String notifyparam = sysManage.getNotifyparam();
            if(StringUtils.isBlank(notifyparam)){
                CommonHttpUtil.sendHttpBodyRequest(sysManage.getNotifyurl(),message);
            }else{
                CommonHttpUtil.sendHttpParameterRequest(sysManage.getNotifyurl(),notifyparam,message);
            }
        }catch (Exception e){
            logger.error(message + " ," + e.getMessage(), e);
        }
    }

}
