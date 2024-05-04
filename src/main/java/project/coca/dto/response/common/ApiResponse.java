package project.coca.dto.response.common;

import lombok.Builder;
import lombok.Data;
import project.coca.dto.response.common.error.ErrorCode;
import project.coca.dto.response.common.success.ResponseCode;

@Data
@Builder
public class ApiResponse<T> {
    private Integer code;
    private String message;
    private T data;

    public static <T> ApiResponse<T> response(ResponseCode code, T data) {
        return ApiResponse.<T>builder()
                .code(code.getCode())
                .message(code.getMessage())
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> success(ResponseCode code, String message, T data) {
        return ApiResponse.<T>builder()
                .code(code.getCode())
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> success(ResponseCode code, String message) {
        return ApiResponse.<T>builder()
                .code(code.getCode())
                .message(message)
                .build();
    }

    public static <T> ApiResponse<T> fail(ErrorCode code, String message) {
        return ApiResponse.<T>builder()
                .code(code.getCode())
                .message(message)
                .build();
    }
}
