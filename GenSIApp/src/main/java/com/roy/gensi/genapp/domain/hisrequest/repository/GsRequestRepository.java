package com.roy.gensi.genapp.domain.hisrequest.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.roy.gensi.genapp.domain.hisrequest.entity.GsHisRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author ：楼兰
 * @date ：Created in 2021/5/5
 * @description:
 **/

@Component
public class GsRequestRepository {

    @Autowired
    private GsRequestMapper gsRequestMapper;

    public List<GsHisRequest> queryGsRequest(String transId, String serviceCode){
        QueryWrapper<GsHisRequest> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotEmpty(transId)){
            queryWrapper.eq("transId",transId);
        }
        if(StringUtils.isNotEmpty(serviceCode)){
            queryWrapper.eq("serviceCode",serviceCode);
        }
        return gsRequestMapper.selectList(queryWrapper);
    }

    public int addGsRequest(GsHisRequest gsRequest) {
        return gsRequestMapper.insert(gsRequest);
    }
}
