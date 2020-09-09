package com.iflytek.zhyl.valhalla.utils;

import lombok.Data;

/**
 * @author quwang2
 */
@Data
public class Msg {
    /**
     * 代码
     */
    private Integer code = 0;
    /**
     * 错误信息
     */
    private String message;
    /**
     * 内容
     */
    private Object data;

}
