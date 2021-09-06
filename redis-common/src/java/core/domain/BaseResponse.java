package core.domain;

import core.constant.HttpStatus;
import org.springframework.util.StringUtils;

import javax.xml.crypto.Data;
import java.util.HashMap;

public class BaseResponse extends HashMap<String,Object> {

    /**
     * 状态码
     */

    public static final String  CODE ="code";


    /**
     * 返回内容
     */

    public  static  final String MSG = "msg";

    /**
     * 数据对象
     */

    public  static final String DATA ="data";


    /**
     * 构建返回数据
     * @param code
     * @param msg
     * @param data
     */

    public  BaseResponse(int code,String msg,Object data){
        super.put(CODE,code);
        super.put(MSG,msg);
        if(!StringUtils.isEmpty(data)){
            super.put(DATA, data);
        }

    }

    /**
     * 返回成功消息
     * @param msg
     * @param data
     * @return
     */
    public  static BaseResponse success (String msg,Object data){
        return  new BaseResponse(HttpStatus.SUCCESS,msg,data);
}

/**
 * 返回成功消息
 */
   public static BaseResponse success(Object data){
       return success("操作成功",data);
   }


    /**
     * 返回错误消息（内部服务器）
     * @param msg
     * @param data
     * @return
     */
   public static BaseResponse error(String msg,Object data){
       return  new BaseResponse(HttpStatus.Error,msg,data);
   }

   public static BaseResponse error(Object data){
        return error("操作失败，服务器发生了错误",data);
   }

}
