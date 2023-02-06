package yummypizza.core.validators;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import yummypizza.core.database.UserRepository;
import yummypizza.core.requests.DeleteUserByIdRequest;
import yummypizza.core.responses.CoreError;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DeleteUserByIdRequestValidatorTest {

    @Mock
    private UserRepository repository;
    @InjectMocks
    private DeleteUserByIdRequestValidator validator = new DeleteUserByIdRequestValidator();

    @Test
    public void shouldReturnErrorWhenIdIsNull() {
        DeleteUserByIdRequest request = new DeleteUserByIdRequest(null);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("User ID", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenIdIsZero() {
        DeleteUserByIdRequest request = new DeleteUserByIdRequest(0L);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("User ID", errors.get(0).getField());
        assertEquals("must be a positive number.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenIdIsNegative() {
        DeleteUserByIdRequest request = new DeleteUserByIdRequest(-5L);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("User ID", errors.get(0).getField());
        assertEquals("must be a positive number.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenIdNotExists() {
        DeleteUserByIdRequest request = new DeleteUserByIdRequest(5L);
        Mockito.when(repository.existsById(request.getId())).thenReturn(false);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("User ID", errors.get(0).getField());
        assertEquals("doesn't exist.", errors.get(0).getMessage());
    }

    @Test
    public void shouldNotReturnErrorsWhenRequestIsCorrect() {
        DeleteUserByIdRequest request = new DeleteUserByIdRequest(5L);
        Mockito.when(repository.existsById(request.getId())).thenReturn(true);
        List<CoreError> errors = validator.validate(request);
        assertTrue(errors.isEmpty());
    }

}