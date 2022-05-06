package com.roy.gensi.genapp.interfaces;

import com.alibaba.fastjson.JSONObject;
import com.roy.gensi.genapp.domain.genservice.entity.SimpleRequestMsg;

/**
 * @author ：楼兰
 * @date ：Created in 2021/5/6
 * @description:
 **/
public interface GsService {

    JSONObject doBusi(SimpleRequestMsg requestMsg);

    String serviceCode();


}
