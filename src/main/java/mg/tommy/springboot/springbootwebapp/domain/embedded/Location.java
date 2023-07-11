package mg.tommy.springboot.springbootwebapp.domain.embedded;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
public class Location {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    @NonNull
    private String name;

    @Column(nullable = false)
    @NonNull
    private String description;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "lat", column = @Column(name = "latitude", nullable = false)),
            @AttributeOverride(name = "lon", column = @Column(name = "longitude", nullable = false)),
            @AttributeOverride(name = "accuracy", column = @Column(name = "accuracy", nullable = false)),
    })
    @NonNull
    private Coordinates coordinates;
}
