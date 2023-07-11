package mg.tommy.springboot.springbootwebapp.service;

import mg.tommy.springboot.springbootwebapp.domain.embedded.Book;

public interface BookService {
    Iterable<Book> findAll();
}
