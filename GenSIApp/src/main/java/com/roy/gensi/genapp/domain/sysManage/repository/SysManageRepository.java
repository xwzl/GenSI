package com.roy.gensi.genapp.domain.sysManage.repository;

import com.roy.gensi.genapp.domain.sysManage.entity.SysManage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author ：楼兰
 * @date ：Created in 2021/5/5
 * @description:
 **/

@Component
public class SysManageRepository {

    @Autowired
    SysManageMapper sysManageMapper;

    public int saveSys(SysManage gsSys) {
        try{
           return sysManageMapper.insert(gsSys);
        }catch (Exception e){
            return sysManageMapper.updateById(gsSys);
        }
    }

    public int deleteSysById(String sysid) {
        return sysManageMapper.deleteById(sysid);
    }


    public List<SysManage> queryAllSys() {
        return sysManageMapper.selectList(null);
    }
}
