package com.astrovis.astrovisbackend.commons;

public class Response <T>{

    private Integer code;
    private String msg;
    private T data;

    public Response(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }



    public static Response ok(Object data){
        return new Response(ResponseConstants.CODE_OK,ResponseConstants.MSG_OK,data);
    }

    public static Response error(int errorCode,String msg){
        return new Response(errorCode,msg,null);
    }


}
