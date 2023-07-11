package mg.tommy.springboot.springbootwebapp.service;

import mg.tommy.springboot.springbootwebapp.domain.embedded.Beer;

import java.util.Optional;
import java.util.UUID;

public interface BeerService {
    Iterable<Beer> findAll();
    Optional<Beer> findById(UUID id);

    Beer save(Beer beer);

    Beer updateById(UUID uuid, Beer beer);

    void deleteById(UUID uuid);
}
