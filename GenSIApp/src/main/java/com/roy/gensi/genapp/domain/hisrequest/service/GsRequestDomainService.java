package com.roy.gensi.genapp.domain.hisrequest.service;

import com.roy.gensi.genapp.domain.hisrequest.entity.GsHisRequest;
import com.roy.gensi.genapp.domain.hisrequest.repository.GsRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ：楼兰
 * @date ：Created in 2021/5/5
 * @description:
 **/

@Service
public class GsRequestDomainService {

    @Autowired
    GsRequestRepository gsRequestRepository;

    public List<GsHisRequest> queryGsRequest(String transId, String serviceCode) {
        return gsRequestRepository.queryGsRequest(transId,serviceCode);
    }
}
