package mg.tommy.springboot.springbootwebapp.model.domain.embedded;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
public class Itinerary {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    @NonNull
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "itinerary")
    @NonNull
    private List<Visit> visits;

    @JsonIgnore
    @ToString.Exclude
    @OneToOne(mappedBy = "itinerary")
    private Plan plan;
}
