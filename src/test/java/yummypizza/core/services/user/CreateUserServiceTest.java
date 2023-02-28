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
import yummypizza.core.requests.user.CreateUserRequest;
import yummypizza.core.responses.CoreError;
import yummypizza.core.responses.user.CreateUserResponse;
import yummypizza.core.validators.user.CreateUserRequestValidator;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CreateUserServiceTest {

    @Mock
    private CreateUserRequestValidator validator;
    @Mock
    private UserRepository repository;
    @InjectMocks
    private CreateUserService service;

    private CreateUserRequest invalidRequest;
    private CreateUserRequest validRequest;
    private User user;

    @BeforeAll
    public void setup() {
        invalidRequest = new CreateUserRequest(null, "Smith", "m.smith@gmail.com",
                "password", "25436565", UserRole.CLIENT);
        validRequest = new CreateUserRequest("Michael", "Smith", "m.smith@gmail.com",
                "password", "25436565", UserRole.CLIENT);
        user = new User(validRequest.getFirstName(), validRequest.getLastName(), validRequest.getEmail(),
                validRequest.getPassword(), validRequest.getPhone(), validRequest.getRole());
    }

    @Test
    public void shouldReturnResponseWithErrorsWhenValidationFails() {
        Mockito.when(validator.validate(invalidRequest)).thenReturn(List.of(new CoreError("First name", "is mandatory.")));
        CreateUserResponse response = service.execute(invalidRequest);
        assertTrue(response.hasErrors());
        assertEquals(1, response.getErrors().size());
        assertEquals("First name", response.getErrors().get(0).getField());
        assertEquals("is mandatory.", response.getErrors().get(0).getMessage());
    }

    @Test
    public void shouldNotInvokeRepositoryWhenRequestValidationFails() {
        Mockito.when(validator.validate(invalidRequest)).thenReturn(List.of(new CoreError("First name", "is mandatory.")));
        service.execute(invalidRequest);
        Mockito.verifyNoInteractions(repository);
    }

    @Test
    public void shouldCreateUserInRepositoryWhenValidationPasses() {
        Mockito.when(validator.validate(validRequest)).thenReturn(List.of());
        service.execute(validRequest);
        Mockito.verify(repository).save(user);
    }

    @Test
    public void shouldReturnResponseWithoutErrorsWhenValidationPasses() {
        Mockito.when(validator.validate(validRequest)).thenReturn(List.of());
        CreateUserResponse response = service.execute(validRequest);
        assertFalse(response.hasErrors());
    }

    @Test
    public void shouldReturnResponseWithCreatedUserWhenValidationPasses() {
        Mockito.when(validator.validate(validRequest)).thenReturn(List.of());
        Mockito.when(repository.save(user)).thenReturn(user);
        CreateUserResponse response = service.execute(validRequest);
        assertNotNull(response.getCreatedUser());
        assertEquals("Michael", response.getCreatedUser().getFirstName());
        assertEquals("Smith", response.getCreatedUser().getLastName());
    }

}