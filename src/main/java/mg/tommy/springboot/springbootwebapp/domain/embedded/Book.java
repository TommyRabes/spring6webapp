package mg.tommy.springboot.springbootwebapp.domain.embedded;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
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
    private Date publishDate;
    @ManyToMany(mappedBy = "books")
    private Set<Author> authors;
    @ManyToOne
    private Publisher publisher;
}
