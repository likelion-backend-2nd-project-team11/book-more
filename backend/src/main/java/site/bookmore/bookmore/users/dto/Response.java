package site.bookmore.bookmore.users.dto;

import lombok.Getter;

@Getter
public class Response<T> {
    private String resultCode;
    private T result;

    public Response(String resultCode, T result) {
        this.resultCode = resultCode;
        this.result = result;
    }

    public static <T> Response<T> success(T result) {
        return new Response<>("SUCCESS", result);
    }

    public static <T> Response<T> error(T result) {
        return new Response<>("ERROR", result);
    }
}
