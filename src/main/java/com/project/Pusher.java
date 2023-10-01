package com.project;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.project.entity.SayLoveData;
import com.project.entity.WeatherData;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.*;
import java.util.ArrayList;
import java.util.HashMap;
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

    public static final String SAY_LOVE_URL="https://apis.tianapi.com/saylove/index";

    public static final String CITY_CODE="101270501";

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
        final WeatherData weatherData = getWeatherData();
        data.add(new WxMpTemplateData("date",weatherData.getResult().getDate()+" "+weatherData.getResult().getWeek(), "#00BFFF"));
        data.add(new WxMpTemplateData("weather",weatherData.getResult().getWeather(), "#00FFFF"));
        data.add(new WxMpTemplateData("real",weatherData.getResult().getReal(), "#EE212D"));
        data.add(new WxMpTemplateData("highest",weatherData.getResult().getHighest(), "#FF6347"));
        data.add(new WxMpTemplateData("lowest",weatherData.getResult().getLowest(), "#173177"));
        data.add(new WxMpTemplateData("wind",weatherData.getResult().getWind(), "#42B857"));
        data.add(new WxMpTemplateData("wind",weatherData.getResult().getWind(), "#42B857"));
        data.add(new WxMpTemplateData("customer",customer(), "#FF1493"));
        data.add(new WxMpTemplateData("sentence",getSayLoveData().getResult().getContent(), "#42B857"));
        return data;
    }

    private WeatherData getWeatherData(){
        HashMap<String, Object> map = new HashMap<>(3);
        map.put("key", apyKey);
        map.put("city", CITY_CODE);
        map.put("type", 1);
        final String result = HttpUtil.post(WEATHER_URL, map);
        return JSONUtil.toBean(result, WeatherData.class);
    }

    private SayLoveData getSayLoveData(){
        HashMap<String, Object> map = new HashMap<>(1);
        map.put("key", apyKey);
        final String result = HttpUtil.post(SAY_LOVE_URL, map);
        return JSONUtil.toBean(result, SayLoveData.class);
    }

    private String customer(){
        LocalDate currentDate = LocalDate.now();
        if (currentDate.equals(LocalDate.of(currentDate.getYear(), Month.MAY, 25))){
            LocalDate memorialDay = LocalDate.of(2023, Month.MAY, 25);
            Period period = Period.between(memorialDay, currentDate);
            int yearsDifference = period.getYears();
            return "今天是恋爱"+yearsDifference+"周年纪念日！";
        }else if (currentDate.equals(LocalDate.of(currentDate.getYear(), Month.MARCH, 14))){
            LocalDate birthday = LocalDate.of(2000, Month.MARCH, 14);
            Period period = Period.between(birthday, currentDate);
            int yearsDifference = period.getYears();
            return "今天是"+yearsDifference+"岁生日！";
        }else {
            LocalDate targetDate = LocalDate.of(2023, Month.MAY, 25);
            LocalTime targetTime = LocalTime.of(0, 0, 0);
            LocalDateTime targetDateTime = LocalDateTime.of(targetDate, targetTime);
            LocalDateTime currentDateTime = LocalDateTime.now();
            Duration duration = Duration.between(targetDateTime, currentDateTime);
            long days = duration.toDays();
            return "今天是我们恋爱的第"+days+"天！";
        }
    }
}
