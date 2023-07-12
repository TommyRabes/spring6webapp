package mg.tommy.springboot.springbootwebapp.service.library;

import mg.tommy.springboot.springbootwebapp.domain.embedded.Book;

public interface BookService {
    Iterable<Book> findAll();
}
