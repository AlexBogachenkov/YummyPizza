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
import yummypizza.core.requests.user.DeleteUserByIdRequest;
import yummypizza.core.responses.CoreError;
import yummypizza.core.responses.user.DeleteUserByIdResponse;
import yummypizza.core.validators.user.DeleteUserByIdRequestValidator;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DeleteUserByIdServiceTest {

    @Mock
    private UserRepository repository;
    @Mock
    private DeleteUserByIdRequestValidator validator;
    @InjectMocks
    private DeleteUserByIdService service;

    private DeleteUserByIdRequest invalidRequest;
    private DeleteUserByIdRequest validRequest;

    @BeforeAll
    public void setup() {
        invalidRequest = new DeleteUserByIdRequest(-5L);
        validRequest = new DeleteUserByIdRequest(2L);
    }

    @Test
    public void shouldReturnResponseWithErrorsWhenValidationFails() {
        Mockito.when(validator.validate(invalidRequest)).thenReturn(List.of(new CoreError("User ID", "must be a positive number.")));
        DeleteUserByIdResponse response = service.execute(invalidRequest);
        assertTrue(response.hasErrors());
        assertEquals(1, response.getErrors().size());
        assertEquals("User ID", response.getErrors().get(0).getField());
        assertEquals("must be a positive number.", response.getErrors().get(0).getMessage());
    }

    @Test
    public void shouldNotInvokeRepositoryWhenValidationFails() {
        Mockito.when(validator.validate(invalidRequest)).thenReturn(List.of(new CoreError("User ID", "must be a positive number.")));
        service.execute(invalidRequest);
        Mockito.verifyNoInteractions(repository);
    }

    @Test
    public void shouldDeleteUserFromRepositoryWhenValidationPasses() {
        Mockito.when(validator.validate(validRequest)).thenReturn(List.of());
        service.execute(validRequest);
        Mockito.verify(repository).deleteById(2l);
    }

    @Test
    public void shouldReturnResponseWithoutErrorsWhenValidationPasses() {
        Mockito.when(validator.validate(validRequest)).thenReturn(List.of());
        DeleteUserByIdResponse response = service.execute(validRequest);
        assertFalse(response.hasErrors());
    }

}