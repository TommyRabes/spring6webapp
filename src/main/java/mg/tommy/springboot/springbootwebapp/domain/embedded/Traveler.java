package mg.tommy.springboot.springbootwebapp.domain.embedded;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Traveler {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    @NonNull
    private String fullName;

    @Column(nullable = false)
    @NonNull
    private String gender;

    @Column(nullable = false)
    @NonNull
    private Date birthdate;

    @JsonIgnore
    @ToString.Exclude
    @ManyToMany(mappedBy = "travelers")
    private Set<Plan> plans;
}
