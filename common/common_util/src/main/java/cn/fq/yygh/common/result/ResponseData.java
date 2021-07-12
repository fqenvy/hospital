package cn.fq.yygh.common.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 全局统一返回结果类
 */
@Data
@ApiModel(value = "全局统一返回结果")
public class ResponseData<T> {

    @ApiModelProperty(value = "返回码")
    private Integer code;

    @ApiModelProperty(value = "返回消息")
    private String message;

    @ApiModelProperty(value = "返回数据")
    private T data;

    public ResponseData(){}

    protected static <T> ResponseData<T> build(T data) {
        ResponseData<T> result = new ResponseData<T>();
        if (data != null)
            result.setData(data);
        return result;
    }

    public static <T> ResponseData<T> build(T body, ResultCodeEnum resultCodeEnum) {
        ResponseData<T> result = build(body);
        result.setCode(resultCodeEnum.getCode());
        result.setMessage(resultCodeEnum.getMessage());
        return result;
    }

    public static <T> ResponseData<T> build(Integer code, String message) {
        ResponseData<T> result = build(null);
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    public static<T> ResponseData<T> ok(){
        return ResponseData.ok(null);
    }

    /**
     * 操作成功
     * @param data
     * @param <T>
     * @return
     */
    public static<T> ResponseData<T> ok(T data){
        // Result<T> result = build(data);
        return build(data, ResultCodeEnum.SUCCESS);
    }

    public static<T> ResponseData<T> fail(){
        return ResponseData.fail(null);
    }

    /**
     * 操作失败
     * @param data
     * @param <T>
     * @return
     */
    public static<T> ResponseData<T> fail(T data){
        // Result<T> result = build(data);
        return build(data, ResultCodeEnum.FAIL);
    }

    public ResponseData<T> message(String msg){
        this.setMessage(msg);
        return this;
    }

    public ResponseData<T> code(Integer code){
        this.setCode(code);
        return this;
    }

    public boolean isOk() {
        if(this.getCode().intValue() == ResultCodeEnum.SUCCESS.getCode().intValue()) {
            return true;
        }
        return false;
    }
}
