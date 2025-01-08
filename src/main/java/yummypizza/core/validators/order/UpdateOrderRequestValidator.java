package yummypizza.core.validators.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import yummypizza.core.database.CartProductRepository;
import yummypizza.core.database.CartRepository;
import yummypizza.core.database.OrderRepository;
import yummypizza.core.domain.OrderStatus;
import yummypizza.core.requests.order.UpdateOrderRequest;
import yummypizza.core.responses.CoreError;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class UpdateOrderRequestValidator {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartProductRepository cartProductRepository;

    private List<CoreError> errors;

    public List<CoreError> validate(UpdateOrderRequest request) {
        errors = new ArrayList<>();
        validateId(request.getId());
        validateCartId(request.getCartId(), request.getId());
        validateAmount(request.getAmount());
        validateDateCreated(request.getDateCreated());
        validateCity(request.getCity(), request.isForTakeaway());
        validateStreet(request.getStreet(), request.isForTakeaway());
        validateBuildingNumber(request.getBuildingNumber(), request.isForTakeaway());
        validateApartmentNumber(request.getApartmentNumber(), request.isForTakeaway());
        return errors;
    }

    private void validateId(Long id) {
        if (id == null) {
            errors.add(new CoreError("Pasūtījuma ID", "ir obligāts"));
            return;
        }
        if (id <= 0) {
            errors.add(new CoreError("Pasūtījuma ID", "ir jābūt pozitīvam skaitlim"));
            return;
        }
        if (!orderRepository.existsById(id)) {
            errors.add(new CoreError("Pasūtījums", "ar šādu ID netika atrasts"));
        }
    }

    private void validateCartId(Long cartId, Long orderId) {
        if (cartId == null) {
            errors.add(new CoreError("Groza ID", "ir obligāts"));
            return;
        }
        if (cartId <= 0) {
            errors.add(new CoreError("Groza ID", "ir jābūt pozitīvam skaitlim"));
            return;
        }
        if (!cartRepository.existsById(cartId)) {
            errors.add(new CoreError("Grozs", "ar šādu ID netika atrasts"));
        }
        if (orderRepository.existsByCartId(cartId) && !Objects.equals(orderRepository.findByCartId(cartId).get().getId(), orderId)) {
            errors.add(new CoreError("Grozs", "ar šādu ID jau tika izmantots citā pasūtījumā"));
        }
        if (cartProductRepository.findByCartId(cartId).isEmpty()) {
            errors.add(new CoreError("Grozā", "jābūt vismaz vienam produktam, lai veiktu pasūtīumu"));
        }
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null) {
            errors.add(new CoreError("Summa", "ir obligāta"));
        }
    }

    private void validateDateCreated(LocalDateTime dateCreated) {
        if (dateCreated == null) {
            errors.add(new CoreError("Izveidošanas datums", "ir obligāts"));
        }
    }

    private void validateCity(String city, boolean isForTakeaway) {
        if (isForTakeaway) {
            return;
        }
        if (city == null || city.isBlank()) {
            errors.add(new CoreError("Pilsēta", "ir obligāta"));
            return;
        }
        if (city.length() > 30) {
            errors.add(new CoreError("Pilsētas nosaukuma", "garumam ir jābūt robežās līdz 30 simboliem"));
        }
    }

    private void validateStreet(String street, boolean isForTakeaway) {
        if (isForTakeaway) {
            return;
        }
        if (street == null || street.isBlank()) {
            errors.add(new CoreError("Iela", "ir obligāta"));
            return;
        }
        if (street.length() > 30) {
            errors.add(new CoreError("Ielas nosaukuma", "garumam ir jābūt robežās līdz 30 simboliem"));
        }
    }

    private void validateBuildingNumber(String buildingNumber, boolean isForTakeaway) {
        if (isForTakeaway) {
            return;
        }
        if (buildingNumber == null || buildingNumber.isBlank()) {
            errors.add(new CoreError("Mājas numurs", "ir obligāts"));
            return;
        }
        if (buildingNumber.length() > 10) {
            errors.add(new CoreError("Mājas numura", "garumam ir jābūt robežās līdz 10 simboliem"));
        }
    }

    private void validateApartmentNumber(String apartmentNumber, boolean isForTakeaway) {
        if (!isForTakeaway && apartmentNumber.length() > 10) {
            errors.add(new CoreError("Dzīvokļa numura", "garumam ir jābūt robežās līdz 10 simboliem"));
        }
    }

}
