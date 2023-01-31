package yummypizza.core.responses;

import lombok.Getter;

@Getter
public class CoreError {

    private String field;
    private String message;

    public CoreError(String field, String message) {
        this.field = field;
        this.message = message;
    }

}
