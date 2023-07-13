package mg.tommy.springboot.springbootwebapp.domain.embedded;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Beer {
    @Id
    /**
     * Prior to Hibernate 6.2 (Hibernate 4.x and 5.x), you can use one the two approaches to generate UUID
     */
    // The verbose approach:
    // @GeneratedValue(strategy = GenerationType.UUID)
    // @GenericGenerator(name = "UUID", type = UuidGenerator.class)
    // @Column(length = 36, columnDefinition = "varchar", updatable = false, nullable = false, unique = true)

    // Single annotation approach
    // @UuidGenerator

    // But this one is recommended as of Hibernate 6.2
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Version
    private Integer version;
    private String beerName;
    private BeerStyle beerStyle;
    private String upc;
    private BigDecimal price;
    private Integer quantityOnHand;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
}
