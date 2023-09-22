package mg.tommy.springboot.springbootwebapp.model.domain.embedded;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

@Builder
@NoArgsConstructor
@Getter
@Setter
@Entity
public class BeerOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    private UUID id;

    @Version
    private Long version;

    private String customerRef;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdDate;
    @UpdateTimestamp
    private Timestamp lastModifiedDate;

    @OneToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
    private BeerOrderShipment beerOrderShipment;

    @ManyToOne
    private Customer customer;

    @OneToMany(mappedBy = "beerOrder")
    private Set<BeerOrderLine> beerOrderLines;

    /*
    This sets up the bidirectional relationship but remember that if you use Lombok's Builder
    to create this entity, it will call the constructor to create the instance, not this setter
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
        customer.getBeerOrders().add(this);
    }

    public void setBeerOrderShipment(BeerOrderShipment beerOrderShipment) {
        this.beerOrderShipment = beerOrderShipment;
        beerOrderShipment.setBeerOrder(this);
    }

    public BeerOrder(UUID id, Long version, String customerRef, Timestamp createdDate, Timestamp lastModifiedDate,
                     BeerOrderShipment beerOrderShipment, Customer customer, Set<BeerOrderLine> beerOrderLines) {
        this.setId(id);
        this.setVersion(version);
        this.setCustomerRef(customerRef);
        this.setCreatedDate(createdDate);
        this.setLastModifiedDate(lastModifiedDate);
        this.setBeerOrderShipment(beerOrderShipment);
        this.setCustomer(customer);
        this.setBeerOrderLines(beerOrderLines);
    }
}
