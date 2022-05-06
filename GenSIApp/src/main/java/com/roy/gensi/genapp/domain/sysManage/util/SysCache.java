package com.roy.gensi.genapp.domain.sysManage.util;

import com.roy.gensi.genapp.domain.sysManage.entity.SysManage;
import com.roy.gensi.genapp.domain.sysManage.service.SysManageDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：楼兰
 * @date ：Created in 2021/5/6
 * @description:
 **/

@Component
public class SysCache {
    private static Map<String, SysManage> gsmanageMap = new HashMap<String, SysManage>();

    @Autowired
    private SysManageDomainService sysManageDomainService;
    @PostConstruct
    public void init() {
        final List<SysManage> sysManages = sysManageDomainService.queryAllSys();
        if(null != sysManages && !sysManages.isEmpty()){
            sysManages.forEach(sysManage -> {
                gsmanageMap.put(sysManage.getSysid(),sysManage);
            });
        }
    }

    public void reLoadConfig() {
        gsmanageMap.clear();
        this.init();
    }

    public SysManage getSysManage(String sysId){
        return gsmanageMap.containsKey(sysId)?gsmanageMap.get(sysId):null;
    }

    public boolean checkSysId(String sysId) {
        return gsmanageMap.containsKey(sysId);
    }

}
