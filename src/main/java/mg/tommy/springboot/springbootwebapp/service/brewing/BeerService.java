package mg.tommy.springboot.springbootwebapp.service.brewing;

import mg.tommy.springboot.springbootwebapp.dto.BeerDto;

import java.util.Optional;
import java.util.UUID;

public interface BeerService {
    Iterable<BeerDto> findAll();
    Optional<BeerDto> findById(UUID id);

    BeerDto save(BeerDto beer);

    Optional<BeerDto> overwriteById(UUID uuid, BeerDto beer);

    Optional<BeerDto> updateById(UUID uuid, BeerDto beer);

    boolean deleteById(UUID uuid);
}
