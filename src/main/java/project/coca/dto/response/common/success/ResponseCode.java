package project.coca.dto.response.common.success;

import lombok.Getter;

@Getter
public enum ResponseCode {
    OK(200, "OK"),
    CREATED(201, "Created");

    private final Integer code;
    private final String message;

    ResponseCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
