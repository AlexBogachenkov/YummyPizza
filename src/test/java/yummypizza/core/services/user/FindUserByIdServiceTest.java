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
import yummypizza.core.requests.user.FindUserByIdRequest;
import yummypizza.core.responses.CoreError;
import yummypizza.core.responses.user.FindUserByIdResponse;
import yummypizza.core.validators.user.FindUserByIdRequestValidator;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FindUserByIdServiceTest {

    @Mock
    private FindUserByIdRequestValidator validator;
    @Mock
    private UserRepository repository;
    @InjectMocks
    private FindUserByIdService service;

    private FindUserByIdRequest invalidRequest;
    private FindUserByIdRequest validRequest;

    @BeforeAll
    public void setup() {
        invalidRequest = new FindUserByIdRequest(-5L);
        validRequest = new FindUserByIdRequest(4L);
    }

    @Test
    public void shouldReturnResponseWithErrorsWhenValidationFails() {
        Mockito.when(validator.validate(invalidRequest)).thenReturn(List.of(new CoreError("User ID", "is mandatory.")));
        FindUserByIdResponse response = service.execute(invalidRequest);
        assertTrue(response.hasErrors());
        assertEquals(1, response.getErrors().size());
        assertEquals("User ID", response.getErrors().get(0).getField());
        assertEquals("is mandatory.", response.getErrors().get(0).getMessage());
    }

    @Test
    public void shouldNotInvokeRepositoryWhenValidationFails() {
        Mockito.when(validator.validate(invalidRequest)).thenReturn(List.of(new CoreError("User ID", "is mandatory.")));
        service.execute(invalidRequest);
        Mockito.verifyNoInteractions(repository);
    }

    @Test
    public void shouldSearchUserInRepositoryWhenValidationPasses() {
        Mockito.when(validator.validate(validRequest)).thenReturn(List.of());
        service.execute(validRequest);
        Mockito.verify(repository).findById(4L);
    }

    @Test
    public void shouldReturnResponseWithoutErrorsWhenValidationPasses() {
        Mockito.when(validator.validate(validRequest)).thenReturn(List.of());
        FindUserByIdResponse response = service.execute(validRequest);
        assertFalse(response.hasErrors());
    }

    @Test
    public void shouldReturnResponseWithFoundUserWhenValidationPasses() {
        Mockito.when(validator.validate(validRequest)).thenReturn(List.of());
        Mockito.when(repository.findById(4L)).thenReturn(Optional.of(new User("Michael", "Smith", "m.smith@gmail.com",
                "password", "25436565", UserRole.CLIENT)));
        FindUserByIdResponse response = service.execute(validRequest);
        assertNotNull(response.getFoundUser());
        assertEquals("Michael", response.getFoundUser().get().getFirstName());
        assertEquals("Smith", response.getFoundUser().get().getLastName());
    }

}