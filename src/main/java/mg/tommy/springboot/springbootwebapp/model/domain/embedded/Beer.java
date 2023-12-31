package mg.tommy.springboot.springbootwebapp.model.domain.embedded;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
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
    // @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false, unique = true)

    // Single annotation approach
    // @UuidGenerator

    // But this one is recommended as of Hibernate 6.2
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Version
    private Integer version;

    @NotNull
    @NotBlank
    @Size(min = 2, max = 255)
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

    @CreationTimestamp
    private LocalDateTime createdDate;
    @UpdateTimestamp
    private LocalDateTime updateDate;

    @OneToMany(mappedBy = "beer")
    private Set<BeerOrderLine> beerOrderLines;

    @Builder.Default
    @ManyToMany(mappedBy = "beers")
    Set<Category> categories = new HashSet<>();

    public void addCategory(Category category) {
        categories.add(category);
        category.getBeers().add(this);
    }

    public void removeCategory(Category category) {
        categories.remove(category);
        category.getBeers().remove(this);
    }

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
