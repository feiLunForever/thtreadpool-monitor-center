package org.idea.threadpool.monitor.console.config;

/**
 * @Author linhao
 * @Date created in 10:36 下午 2022/9/7
 */
public class IResponse<T> {

    private String msg;
    private Integer code;
    private T data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> IResponse<T> error(T data, String errorMsg) {
        IResponse response = new IResponse();
        response.setCode(-999);
        response.setMsg(errorMsg);
        response.setData(data);
        return response;
    }

    public static <T> IResponse<T> error() {
        IResponse response = new IResponse();
        response.setCode(-999);
        response.setMsg("error");
        return response;
    }

    public static <T> IResponse<T> success() {
        IResponse response = new IResponse();
        response.setCode(200);
        response.setMsg("success");
        return response;
    }

    public static <T> IResponse<T> success(T data) {
        IResponse response = new IResponse();
        response.setCode(200);
        response.setMsg("success");
        response.setData(data);
        return response;
    }
}
