package yummypizza.core.services.user;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import yummypizza.core.database.UserRepository;
import yummypizza.core.domain.User;
import yummypizza.core.domain.UserRole;
import yummypizza.core.requests.user.UpdateUserRequest;
import yummypizza.core.responses.CoreError;
import yummypizza.core.responses.user.UpdateUserResponse;
import yummypizza.core.validators.user.UpdateUserRequestValidator;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UpdateUserServiceTest {

    @Mock
    UpdateUserRequestValidator validator;
    @Mock
    UserRepository repository;
    @InjectMocks
    UpdateUserService service;

    private UpdateUserRequest invalidRequest;
    private UpdateUserRequest validRequest;
    private User user;

    @BeforeAll
    public void setup() {
        invalidRequest = new UpdateUserRequest(null, "Michael", "Smith", "m.smith@gmail.com",
                "password", "25436565", UserRole.CLIENT);
        validRequest = new UpdateUserRequest(5L, "Michael", "Smith", "m.smith@gmail.com",
                "password", "25436565", UserRole.CLIENT);
        user = new User(validRequest.getId(), validRequest.getFirstName(), validRequest.getLastName(), validRequest.getEmail(),
                validRequest.getPassword(), validRequest.getPhone(), validRequest.getRole());
    }

    @Test
    public void shouldReturnResponseWithErrorsWhenValidationFails() {
        Mockito.when(validator.validate(invalidRequest)).thenReturn(List.of(new CoreError("User ID", "is mandatory.")));
        UpdateUserResponse response = service.execute(invalidRequest);
        assertTrue(response.hasErrors());
        assertEquals(1, response.getErrors().size());
        assertEquals("User ID", response.getErrors().get(0).getField());
        assertEquals("is mandatory.", response.getErrors().get(0).getMessage());
    }

    @Test
    public void shouldNotInvokeRepositoryWhenRequestValidationFails() {
        Mockito.when(validator.validate(invalidRequest)).thenReturn(List.of(new CoreError("User ID", "is mandatory.")));
        service.execute(invalidRequest);
        Mockito.verifyNoInteractions(repository);
    }

    @Test
    public void shouldUpdateUserInRepositoryWhenValidationPasses() {
        Mockito.when(validator.validate(validRequest)).thenReturn(List.of());
        service.execute(validRequest);
        Mockito.verify(repository).save(user);
    }

    @Test
    public void shouldReturnResponseWithoutErrorsWhenValidationPasses() {
        Mockito.when(validator.validate(validRequest)).thenReturn(List.of());
        UpdateUserResponse response = service.execute(validRequest);
        assertFalse(response.hasErrors());
    }

    @Test
    public void shouldReturnResponseWithUpdatedUserWhenValidationPasses() {
        Mockito.when(validator.validate(validRequest)).thenReturn(List.of());
        Mockito.when(repository.save(user)).thenReturn(user);
        UpdateUserResponse response = service.execute(validRequest);
        assertNotNull(response.getUpdatedUser());
        assertEquals("Michael", response.getUpdatedUser().getFirstName());
        assertEquals("Smith", response.getUpdatedUser().getLastName());
    }

}