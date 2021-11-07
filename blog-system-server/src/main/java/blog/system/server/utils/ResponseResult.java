package blog.system.server.utils;

import org.springframework.http.HttpStatus;

/**
 * Description:
 *
 * @author 宣君
 * @date 2021-11-07 0:15
 */
public class ResponseResult<T> {
    private int code;
    private HttpStatus httpStatus;
    private String message;
    private T data;

    public ResponseResult(HttpStatus httpStatus, String message, T data) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.data = data;
    }

    public ResponseResult(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public ResponseResult(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public ResponseResult() {
    }

    public int getCode() {
        return this.httpStatus.value();
    }


    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
