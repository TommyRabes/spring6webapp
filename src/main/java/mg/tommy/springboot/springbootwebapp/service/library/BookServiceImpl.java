package mg.tommy.springboot.springbootwebapp.service.library;

import lombok.RequiredArgsConstructor;
import mg.tommy.springboot.springbootwebapp.domain.embedded.Book;
import mg.tommy.springboot.springbootwebapp.repository.embedded.BookRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    @Override
    public Iterable<Book> findAll() {
        return bookRepository.findAll();
    }
}
