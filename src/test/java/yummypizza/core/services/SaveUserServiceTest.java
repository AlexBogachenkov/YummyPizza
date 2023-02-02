package yummypizza.core.services;

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
import yummypizza.core.requests.SaveUserRequest;
import yummypizza.core.responses.CoreError;
import yummypizza.core.responses.SaveUserResponse;
import yummypizza.core.validators.SaveUserRequestValidator;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SaveUserServiceTest {

    @Mock
    SaveUserRequestValidator validator;
    @Mock
    UserRepository repository;
    @InjectMocks
    SaveUserService service;

    private SaveUserRequest invalidRequest;
    private SaveUserRequest validRequest;
    private User user;

    @BeforeAll
    public void setup() {
        invalidRequest = new SaveUserRequest(null, "Smith", "m.smith@gmail.com",
                "password", "25436565", UserRole.CLIENT);
        validRequest = new SaveUserRequest("Michael", "Smith", "m.smith@gmail.com",
                "password", "25436565", UserRole.CLIENT);
        user = new User(validRequest.getFirstName(), validRequest.getLastName(), validRequest.getEmail(),
                validRequest.getPassword(), validRequest.getPhone(), validRequest.getRole());
    }

    @Test
    public void shouldReturnResponseWithErrorsWhenValidationFails() {
        Mockito.when(validator.validate(invalidRequest)).thenReturn(List.of(new CoreError("First name", "is mandatory.")));
        SaveUserResponse response = service.execute(invalidRequest);
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
    public void shouldSaveUserToRepositoryWhenValidationPasses() {
        Mockito.when(validator.validate(validRequest)).thenReturn(List.of());
        service.execute(validRequest);
        Mockito.verify(repository).save(user);
    }

    @Test
    public void shouldReturnResponseWithoutErrorsWhenValidationPasses() {
        Mockito.when(validator.validate(validRequest)).thenReturn(List.of());
        SaveUserResponse response = service.execute(validRequest);
        assertFalse(response.hasErrors());
    }

    @Test
    public void shouldReturnResponseWithSavedUserWhenValidationPasses() {
        Mockito.when(validator.validate(validRequest)).thenReturn(List.of());
        Mockito.when(repository.save(user)).thenReturn(user);
        SaveUserResponse response = service.execute(validRequest);
        assertNotNull(response.getSavedUser());
        assertEquals("Michael", response.getSavedUser().getFirstName());
        assertEquals("Smith", response.getSavedUser().getLastName());
    }

}