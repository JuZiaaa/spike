package com.juzi.result;

/**
 * @author: juzi
 * @date: 2019/4/3
 * @time: 22:15
 */
public class Result<T> {

    private int code;
    private String msg;
    private T data;

    public static <T> Result success(T data){
        return new Result<T>(CodeMsg.SUCCESS,data);
    }

    public static <T> Result error(CodeMsg codeMsg){
        return new Result<T>(codeMsg);
    }

    private Result(CodeMsg codeMsg){
        this.code = codeMsg.getCode();
        this.msg = codeMsg.getMsg();
    }
    private Result(CodeMsg codeMsg,T data){
        if(null == codeMsg){
            return;
        }
        this.code = codeMsg.getCode();
        this.msg = codeMsg.getMsg();
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

}
