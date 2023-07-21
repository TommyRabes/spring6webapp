package mg.tommy.springboot.springbootwebapp.model.domain.embedded;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
public class Visit {

    @Id
    @GeneratedValue
    private Long id;

    @ElementCollection(fetch = FetchType.EAGER)
    @NonNull
    List<String> todos;

    @Temporal(TemporalType.DATE)
    @NonNull
    private Date fromDate;

    @Temporal(TemporalType.DATE)
    @NonNull
    private Date untilDate;

    @OneToOne
    @NonNull
    private Flight entryFlight;

    @OneToOne
    @NonNull
    private Flight quitFlight;

    @ManyToOne
    @NonNull
    private Itinerary itinerary;

}
