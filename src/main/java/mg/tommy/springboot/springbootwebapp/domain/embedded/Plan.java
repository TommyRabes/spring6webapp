package mg.tommy.springboot.springbootwebapp.domain.embedded;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity(name = "traveling_plan")
public class Plan {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    @NonNull
    private String name;

    @PrimaryKeyJoinColumn(name = "itinerary_id", referencedColumnName = "plan_id")
    @OneToOne
    @NonNull
    private Itinerary itinerary;

    @ManyToMany
    @JoinTable(
            name = "plans_travelers",
            joinColumns = {@JoinColumn(name = "plan_id")},
            inverseJoinColumns = {@JoinColumn(name = "traveler_id")}
    )
    @NonNull
    private Set<Traveler> travelers;
}
