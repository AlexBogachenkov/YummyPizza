package yummypizza.core.responses;

import lombok.Getter;

import java.util.List;

@Getter
public class CoreResponse {

    private List<CoreError> errors;

    public CoreResponse(List<CoreError> errors) {
        this.errors = errors;
    }

    public boolean hasErrors() {
        return errors != null && !errors.isEmpty();
    }

}
