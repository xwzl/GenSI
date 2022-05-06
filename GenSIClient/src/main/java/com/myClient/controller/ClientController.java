package com.myClient.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author ：楼兰
 * @date ：Created in 2021/5/6
 * @description:
 **/

@Controller
@RequestMapping("/client")
public class ClientController {

    @RequestMapping("/receiveMessage")
    public void receiveMessage(@RequestBody String message){
        System.out.println("received message from gensi "+message);
//        final String mobileArea = JSONObject.parseObject(message).getJSONObject("body").getJSONObject("desc").getJSONObject("responseMsgBody").getString("mobileArea");
//        System.out.println(mobileArea);
        System.out.println(JSONObject.parseObject(message).getJSONObject("responseMsgBody"));
    }
}
