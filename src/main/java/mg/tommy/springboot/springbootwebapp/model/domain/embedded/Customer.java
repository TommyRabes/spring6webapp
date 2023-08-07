package mg.tommy.springboot.springbootwebapp.model.domain.embedded;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import mg.tommy.springboot.springbootwebapp.model.dto.constraint.validator.Early;

import java.time.LocalDate;
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
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Version
    private Integer version;

    @NotNull
    @NotBlank
    @Size(min = 3, max = 100)
    @Column(length = 100, nullable = false)
    private String firstName;

    @NotNull
    @NotBlank
    @Size(min = 3, max = 100)
    @Column(length = 100, nullable = false)
    private String lastName;

    @NotNull
    @NotBlank
    @Size(min = 3, max = 320)
    @Email
    @Column(length = 320, nullable = false)
    private String email;

    @NotNull
    @Early(years = 18)
    private LocalDate birthdate;

    @Size(min = 5, max = 200)
    @Column(length = 200)
    private String address;

    private LocalDateTime createdDate;
    private LocalDateTime updateDate;

    @Builder.Default
    @OneToMany(mappedBy = "customer")
    private Set<BeerOrder> beerOrders = new HashSet<>();
}
