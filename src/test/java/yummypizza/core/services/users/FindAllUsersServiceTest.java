package yummypizza.core.services.users;

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
import yummypizza.core.responses.user.FindAllUsersResponse;
import yummypizza.core.services.user.FindAllUsersService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FindAllUsersServiceTest {

    @Mock
    private UserRepository repository;
    @InjectMocks
    private FindAllUsersService service = new FindAllUsersService();

    private User user1;
    private User user2;

    @BeforeAll
    public void setup() {
        user1 = new User("Michael", "Smith", "m.smith@gmail.com",
                "password", "25436565", UserRole.CLIENT);
        user2 = new User("John", "Jordan", "j.jordan@gmail.com",
                "qwerty123", "22345344", UserRole.CLIENT);
    }

    @Test
    public void shouldReturnFoundUsers() {
        Mockito.when(repository.findAll()).thenReturn(List.of(user1, user2));
        FindAllUsersResponse response = service.execute();
        assertNotNull(response.getAllUsers());
        assertEquals(2, response.getAllUsers().size());
        assertEquals(user1, response.getAllUsers().get(0));
        assertEquals(user2, response.getAllUsers().get(1));
    }

}