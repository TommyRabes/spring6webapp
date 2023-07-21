package mg.tommy.springboot.springbootwebapp.model.domain.embedded;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
public class Book {
    @Id
    @GeneratedValue
    private UUID uuid;
    private String title;
    private String isbn;
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> keywords;
    @Column(columnDefinition = "TEXT")
    private String content;
    private LocalDate publishDate;
    @ManyToMany(mappedBy = "books")
    private Set<Author> authors = new HashSet<>();
    @ManyToOne
    private Publisher publisher;
}
