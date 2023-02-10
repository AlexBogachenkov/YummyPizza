package yummypizza.core.requests;

import lombok.Data;
import lombok.NoArgsConstructor;
import yummypizza.core.domain.UserRole;

@Data
@NoArgsConstructor
public class CreateUserRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private UserRole role;

    public CreateUserRequest(String firstName, String lastName, String email, String password, String phone, UserRole role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = role;
    }

}
