package yummypizza.core.requests;

import lombok.Getter;
import lombok.Setter;
import yummypizza.core.domain.UserRole;

@Getter
@Setter
public class SaveUserRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private UserRole role;

    public SaveUserRequest(String firstName, String lastName, String email, String password, String phone, UserRole role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = role;
    }

}
