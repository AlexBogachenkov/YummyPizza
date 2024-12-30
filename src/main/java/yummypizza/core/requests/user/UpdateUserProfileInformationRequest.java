package yummypizza.core.requests.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import yummypizza.core.domain.UserRole;

@Data
@NoArgsConstructor
public class UpdateUserProfileInformationRequest {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String passwordOneMoreTime;
    private String phone;
    private UserRole role;

    public UpdateUserProfileInformationRequest(Long id, String firstName, String lastName, String email, String password, String passwordOneMoreTime, String phone, UserRole role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.passwordOneMoreTime = passwordOneMoreTime;
        this.phone = phone;
        this.role = role;
    }

}
