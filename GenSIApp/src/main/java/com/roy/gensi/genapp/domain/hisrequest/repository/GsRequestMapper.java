package com.roy.gensi.genapp.domain.hisrequest.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.roy.gensi.genapp.domain.hisrequest.entity.GsHisRequest;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author ：楼兰
 * @date ：Created in 2021/5/5
 * @description:
 **/
@Mapper
public interface GsRequestMapper extends BaseMapper<GsHisRequest> {
}
