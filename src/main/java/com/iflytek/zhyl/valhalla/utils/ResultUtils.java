package com.iflytek.zhyl.valhalla.utils;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

/**
 * @author quwang2
 */
public class ResultUtils {

    public static Msg success(Object object){
        Msg msg=new Msg();
        msg.setCode(0);
        msg.setMessage("请求成功");
        if(null==object){
            object="";
        }
        msg.setData(object);
        return msg;
    }

    public static Msg success(){
        Msg msg=new Msg();
        msg.setCode(0);
        msg.setMessage("success");
        return msg;
    }


    public static Msg error(Integer code,String resultmsg){
        Msg msg=new Msg();
        msg.setCode(code);
        msg.setMessage(resultmsg);
        return msg;
    }

    public static Msg error(String errorMsg) {
        Msg msg=new Msg();
        msg.setCode(-1);
        msg.setMessage(errorMsg);
        return msg;
    }

    public static Msg parse(String result)
    {
        Msg msg=new Msg();
        try {
            JSONObject jsonObject = JSONObject.parseObject(result);
            msg.setCode(jsonObject.getInteger("code"));
            msg.setMessage(jsonObject.getString("message"));
            msg.setData(jsonObject.get("data"));
        }catch (JSONException e)
        {
            msg.setCode(-1);
            msg.setMessage(result);
        }

        return msg;
    }

}
