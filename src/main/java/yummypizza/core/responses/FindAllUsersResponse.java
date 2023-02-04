package yummypizza.core.responses;

import lombok.Getter;
import yummypizza.core.domain.User;

import java.util.List;

@Getter
public class FindAllUsersResponse extends CoreResponse {

    private List<User> allUsers;

    public FindAllUsersResponse(List<User> allUsers) {
        this.allUsers = allUsers;
    }

}
