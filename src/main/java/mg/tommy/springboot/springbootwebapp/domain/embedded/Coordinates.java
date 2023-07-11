package mg.tommy.springboot.springbootwebapp.domain.embedded;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Embeddable
public class Coordinates {

    private Double lat;
    private Double lon;
    private Double accuracy;
}
