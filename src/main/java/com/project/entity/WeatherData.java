package com.project.entity;

import lombok.Data;

/**
 * @Author: xuesong.lei
 * @Date: 2023/9/11 22:44
 * @Description: 天气数据
 */
@Data
public class WeatherData {

    public int code;

    public String msg;

    public Result result;

    @Data
    public static class Result {

        /**
         * 日期
         */
        public String date;

        /**
         * 星期
         */
        public String week;

        /**
         * 天气
         */
        public String weather;

        /**
         * 实时温度
         */
        public String real;

        /**
         * 最低温度
         */
        public String lowest;

        /**
         * 最高温度
         */
        public String highest;

        /**
         * 风向
         */
        public String wind;
    }
}
