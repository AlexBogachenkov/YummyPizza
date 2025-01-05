package yummypizza.core.validators.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import yummypizza.core.database.CartProductRepository;
import yummypizza.core.database.CartRepository;
import yummypizza.core.database.OrderRepository;
import yummypizza.core.domain.OrderStatus;
import yummypizza.core.requests.order.CreateOrderRequest;
import yummypizza.core.responses.CoreError;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Component
public class CreateOrderRequestValidator {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartProductRepository cartProductRepository;

    private List<CoreError> errors;

    public List<CoreError> validate(CreateOrderRequest request) {
        errors = new ArrayList<>();
        validateCartId(request.getCartId());
        validateStatus(request.getStatus());
        validateAmount(request.getAmount());
        validateDateCreated(request.getDateCreated());
        validateDateCompleted(request.getDateCompleted(), request.getDateCreated());
        validateCity(request.getCity(), request.isForTakeaway());
        validateStreet(request.getStreet(), request.isForTakeaway());
        validateBuildingNumber(request.getBuildingNumber(), request.isForTakeaway());
        return errors;
    }

    private void validateCartId(Long id) {
        if (id == null) {
            errors.add(new CoreError("Cart ID", "is mandatory."));
            return;
        }
        if (id <= 0) {
            errors.add(new CoreError("Cart ID", "must be a positive number."));
            return;
        }
        if (!cartRepository.existsById(id)) {
            errors.add(new CoreError("Cart ID", "doesn't exist."));
        }
        if (orderRepository.existsByCartId(id)) {
            errors.add(new CoreError("Cart ID", "is already in use in another order."));
        }
        if (cartProductRepository.findByCartId(id).isEmpty()) {
            errors.add(new CoreError("Cart", "should have at least one product to make an order."));
        }
    }

    private void validateStatus(OrderStatus status) {
        if (status == null || status.name().isBlank()) {
            errors.add(new CoreError("Status", "is mandatory."));
            return;
        }
        if (status != OrderStatus.RECEIVED && status != OrderStatus.PREPARING && status != OrderStatus.COMPLETED) {
            errors.add(new CoreError("Status", "must be either 'RECEIVED', 'INACTIVE' or 'COMPLETED'."));
        }
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null) {
            errors.add(new CoreError("Amount", "is mandatory."));
            return;
        }
        if (amount.compareTo(new BigDecimal(0)) <= 0) {
            errors.add(new CoreError("Amount", "must be positive."));
        }
    }

    private void validateDateCreated(LocalDateTime dateCreated) {
        if (dateCreated == null) {
            errors.add(new CoreError("Date created", "is mandatory."));
            return;
        }
        if (dateCreated.isAfter(LocalDateTime.now(ZoneId.of("Europe/Riga")))) {
            errors.add(new CoreError("Date created", "can not be in the future."));
        }
    }

    private void validateDateCompleted(LocalDateTime dateCompleted, LocalDateTime dateCreated) {
        if (dateCompleted == null) {
            return;
        }
        if (dateCompleted.isAfter(LocalDateTime.now(ZoneId.of("Europe/Riga")))) {
            errors.add(new CoreError("Date completed", "can not be in the future."));
        }
        if (dateCompleted.isBefore(dateCreated)) {
            errors.add(new CoreError("Date completed", "can not be before the date created."));
        }
    }

    private void validateCity(String city, boolean isForTakeaway) {
        if (isForTakeaway) {
            return;
        }
        if (city == null || city.isBlank()) {
            errors.add(new CoreError("Pilsēta", "ir obligāta."));
        }
    }

    private void validateStreet(String street, boolean isForTakeaway) {
        if (isForTakeaway) {
            return;
        }
        if (street == null || street.isBlank()) {
            errors.add(new CoreError("Iela", "ir obligāta."));
        }
    }

    private void validateBuildingNumber(String buildingNumber, boolean isForTakeaway) {
        if (isForTakeaway) {
            return;
        }
        if (buildingNumber == null || buildingNumber.isBlank()) {
            errors.add(new CoreError("Building number", "is mandatory."));
        }
    }

}
