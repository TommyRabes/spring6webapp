package mg.tommy.springboot.springbootwebapp.service.library;

import mg.tommy.springboot.springbootwebapp.domain.embedded.Author;
import mg.tommy.springboot.springbootwebapp.repository.embedded.AuthorRepository;
import mg.tommy.springboot.springbootwebapp.repository.embedded.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final PostRepository postRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository, PostRepository postRepository) {
        this.authorRepository = authorRepository;
        this.postRepository = postRepository;
    }

    public List<Author> list() {
        return authorRepository.findAllByOrderByIdAsc();
    }

    public Author save(Author author) {
        return authorRepository.save(author);
    }

    public Optional<Author> get(Long id) {
        return authorRepository.findById(id);
    }

    public boolean delete(Long id) {
        Optional<Author> authorToDelete = authorRepository.findById(id);
        if (authorToDelete.isEmpty())
            return false;
        postRepository.deleteAll(authorToDelete.get().getPosts());
        authorRepository.deleteById(id);
        return true;
    }
}
