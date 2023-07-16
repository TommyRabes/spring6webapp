package mg.tommy.springboot.springbootwebapp.domain.embedded;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Beer {
    @Id
    /**
     * Prior to Hibernate 6.2 (Hibernate 4.x and 5.x), you can use one the two approaches to generate UUID
     */
    // The verbose approach:
    // @GeneratedValue(strategy = GenerationType.UUID)
    // @GenericGenerator(name = "UUID", type = UUIDGenerator.class)
    // @Column(length = 36, columnDefinition = "varchar", updatable = false, nullable = false, unique = true)

    // Single annotation approach
    // @UuidGenerator

    // But this one is recommended as of Hibernate 6.2
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Version
    private Integer version;

    @NotNull
    @NotBlank
    @Size(min = 3, max = 255)
    @Column(nullable = false)
    private String beerName;

    @NotNull
    @Column(nullable = false)
    private BeerStyle beerStyle;

    @NotNull
    @NotBlank
    @Size(min = 3, max = 100)
    @Column(length = 100, nullable = false)
    private String upc;

    @NotNull
    @PositiveOrZero
    @Column(nullable = false)
    private BigDecimal price;

    @PositiveOrZero
    private Integer quantityOnHand;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;

    public static class BeerBuilder {
        private BeerStyle beerStyle;

        public BeerBuilder beerStyle(BeerStyle beerStyle) {
            this.beerStyle = beerStyle;
            return this;
        }

        public BeerBuilder beerStyle(String beerStyle) {
            this.beerStyle = beerStyle == null ? null : BeerStyle.valueOf(beerStyle);
            return this;
        }
    }
}
