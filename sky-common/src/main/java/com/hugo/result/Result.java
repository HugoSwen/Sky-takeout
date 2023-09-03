package com.hugo.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 后端统一返回结果
 *
 * @param <T>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> implements Serializable {

    private Integer code; //编码：1成功，0和其它数字为失败
    private String msg; //错误信息
    private T data; //数据

    public static <T> Result<T> success() {
        return new Result<>(1, null, null);
    }

    public static <T> Result<T> success(T object) {
        return new Result<>(1, null, object);
    }

    public static <T> Result<T> error(String msg) {
        return new Result<>(0, msg, null);
    }

}
