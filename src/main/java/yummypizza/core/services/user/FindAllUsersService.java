package yummypizza.core.services.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yummypizza.core.database.UserRepository;
import yummypizza.core.responses.user.FindAllUsersResponse;

@Service
public class FindAllUsersService {

    @Autowired
    private UserRepository repository;

    public FindAllUsersResponse execute() {
        return new FindAllUsersResponse(repository.findAll());
    }

}
