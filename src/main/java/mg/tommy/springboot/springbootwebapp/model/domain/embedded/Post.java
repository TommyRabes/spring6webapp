package mg.tommy.springboot.springbootwebapp.model.domain.embedded;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Entity
public class Post {

    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    private String title;

    @Column(columnDefinition = "TEXT")
    private String body;

    @Column(columnDefinition = "TEXT")
    private String teaser;

    private String slug;

    @ElementCollection(fetch = FetchType.EAGER)
    // @ElementCollection creates a new table for the annotated property with a one-to-many relationship
    // the generated table is fully owned by the overlying entity
    private Set<String> keywords;

    private boolean active;
    @CreatedDate
    // @Column(columnDefinition = "TIMESTAMP")
    // Replaced the @Column + columnDefinition with @Temporal
    // because the latter is an abstraction from JPA standard so every JPA implementation
    // handle the type-mapping
    // columnDefinition tells the database data type for this particular column which is not portable
    @Temporal(TemporalType.TIMESTAMP)
    private Date postedOn;

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne
    private Author author;
}
