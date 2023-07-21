package mg.tommy.springboot.springbootwebapp.model.domain.embedded;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
public class Author {

    @Id
    @GeneratedValue // By default, Hibernate will use Generation.AUTO as its strategy
    // which would come down to GenerationType.SEQUENCE for numerical ids
    // It's possible to customize the sequence by doing as follows :
    // @GeneratedValue(generator = "author_generator", strategy = GenerationType.SEQUENCE)
    // @SequenceGenerator(name = "author_generator", sequenceName = "author_seq", allocationSize = 1)
    private Long id;

    // Looks like the Jakarta Bean Validation API does apply
    // for JPA entity upon database operation
    @NotEmpty
    @Size(min = 3, max = 255)
    @NonNull
    private String firstName;

    @NotEmpty
    @Size(min = 3, max = 255)
    @NonNull
    private String lastName;

    @NotEmpty
    @Email
    @Size(max = 255)
    @NotEmpty
    private String email;

    @OneToMany(mappedBy = "author")
    private List<Post> posts;

    @ManyToMany
    @JoinTable(
            name = "author_book",
            joinColumns = @JoinColumn(name = "author_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    private Set<Book> books;
}
