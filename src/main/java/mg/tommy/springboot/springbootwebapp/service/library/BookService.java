package mg.tommy.springboot.springbootwebapp.service.library;

import mg.tommy.springboot.springbootwebapp.model.domain.embedded.Book;

public interface BookService {
    Iterable<Book> findAll();
}
