package com.roy.gensi.mobiletag.application;

import com.alibaba.fastjson.JSONObject;
import com.roy.gensi.genapp.domain.genservice.entity.SimpleRequestMsg;
import com.roy.gensi.genapp.interfaces.GsService;
import com.roy.gensi.genapp.util.ServiceCodeConstants;
import com.roy.gensi.mobiletag.util.CodeUtil;
import com.roy.gensi.mobiletag.util.HTTPUtil;

import java.io.IOException;

/**
 * @author ：楼兰
 * @date ：Created in 2021/5/6
 * @description:
 **/

public class MobileTagServiceImpl implements GsService {
    @Override
    public JSONObject doBusi(SimpleRequestMsg requestMsg) {
        JSONObject response = new JSONObject();

        final JSONObject requestMsgBody = requestMsg.getRequestMsgBody();
        String mobile = requestMsgBody.getString("mobile");
        response.put("mobile",mobile);
        String tagSearchUrl = "https://www.sogou.com/reventondc/inner/vrapi?number=${mobile}&callback=show&isSogoDomain=0&objid=10001001";
        try {
            String httpRes = HTTPUtil.httpGet(tagSearchUrl.replace("${mobile}", mobile));
            String mobileTag = CodeUtil.decodeUnicode(httpRes.substring(httpRes.indexOf(":") + 1, httpRes.indexOf(",")).replace("\"", "").replace("\\\\", "\\"));
            response.put("mobileTag",mobileTag);
        } catch (IOException e) {
            response.put("error","查询出错，请更新transId重新查询");
        }
        return response;
    }

    @Override
    public String serviceCode() {
        return ServiceCodeConstants.SERVICE_CODE_MOBLE_TAG;
    }
}
