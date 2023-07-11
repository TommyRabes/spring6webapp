package mg.tommy.springboot.springbootwebapp.domain.embedded;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
public class Flight {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    @NonNull
    private BigDecimal cost;

    @Column(nullable = false)
    @NonNull
    private Date departureDate;

    @NonNull
    private Date arrivalDate;

    @JoinColumn(nullable = false)
    @ManyToOne
    @NonNull
    private Location initialLocation;

    @JoinColumn(nullable = false)
    @ManyToOne
    @NonNull
    private Location destination;
}
