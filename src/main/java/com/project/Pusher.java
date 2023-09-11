package com.project;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: xuesong.lei
 * @Date: 2023/9/7 19:59
 * @Description: 消息推送类
 */
@Component
@EnableScheduling
@RequiredArgsConstructor
public class Pusher {

    private final WxMpService wxMpService;

    @Value("${wechat.push.user}")
    private String pushUser;

    @Value("${wechat.push.template-id}")
    private String templateId;

    @Value("${tianxing.api-key}")
    private String apyKey;

    public static final String WEATHER_URL = "https://apis.tianapi.com/tianqi/index";

    @SneakyThrows
    @Scheduled(cron = "0 0 8 * * ?")
    public void pusher() {
        wxMpService.getTemplateMsgService()
                .sendTemplateMsg(WxMpTemplateMessage.builder()
                        .toUser(pushUser)
                        .templateId(templateId)
                        .data(buildData())
                        .build());
    }

    private List<WxMpTemplateData> buildData() {
        List<WxMpTemplateData> data = new ArrayList<>();
        data.add(new WxMpTemplateData("first", "早上好，今天也要元气满满哦！", "#173177"));
        return data;
    }


}
