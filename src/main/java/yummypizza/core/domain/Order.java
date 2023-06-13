package yummypizza.core.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "cart_id", referencedColumnName = "id", nullable = false)
    private Cart cart;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "date_created", nullable = false)
    private LocalDateTime dateCreated;

    @Column(name = "date_completed")
    private LocalDateTime dateCompleted;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "building_number", nullable = false)
    private String buildingNumber;

    @Column(name = "apartment_number")
    private String apartmentNumber;

    public Order(Cart cart, OrderStatus status, BigDecimal amount, LocalDateTime dateCreated,
                 LocalDateTime dateCompleted, String city, String street, String buildingNumber,
                 String apartmentNumber) {
        this.cart = cart;
        this.status = status;
        this.amount = amount;
        this.dateCreated = dateCreated;
        this.dateCompleted = dateCompleted;
        this.city = city;
        this.street = street;
        this.buildingNumber = buildingNumber;
        this.apartmentNumber = apartmentNumber;
    }
}
