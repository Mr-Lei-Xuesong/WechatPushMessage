package com.project.entity;

import lombok.Data;

/**
 * @Author: xuesong.lei
 * @Date: 2023/9/11 23:12
 * @Description: 土味情话
 */
@Data
public class SayLoveData {

    public int code;

    public String msg;

    public Result result;

    @Data
    public static class Result {
        /**
         * 内容
         */
        public String content;
    }
}
