package yummypizza.core.services.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yummypizza.core.database.OrderRepository;
import yummypizza.core.responses.order.FindAllOrdersResponse;

@Service
public class FindAllOrdersService {

    @Autowired
    private OrderRepository repository;

    public FindAllOrdersResponse execute() {
        return new FindAllOrdersResponse(repository.findAll());
    }

}
