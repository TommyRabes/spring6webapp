package mg.tommy.springboot.springbootwebapp.service.brewing;

import lombok.AllArgsConstructor;
import mg.tommy.springboot.springbootwebapp.domain.embedded.Beer;
import mg.tommy.springboot.springbootwebapp.repository.embedded.BeerRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class BeerServiceImpl implements BeerService {
    private final BeerRepository beerRepository;

    @Override
    public Iterable<Beer> findAll() {
        return beerRepository.findAll();
    }

    @Override
    public Optional<Beer> findById(UUID id) {
        return beerRepository.findById(id);
    }

    @Override
    public Beer save(Beer beer) {
        return beerRepository.save(beer.toBuilder()
                .version(1)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build());
    }

    @Override
    public Beer overwriteById(UUID uuid, Beer beer) {
        Optional<Beer> beerToUpdate = beerRepository.findById(uuid);
        if (beerToUpdate.isEmpty()) {
            return beerRepository.save(beer);
        }
        Beer.BeerBuilder builder = beerToUpdate.get().toBuilder()
                .beerName(beer.getBeerName())
                .beerStyle(beer.getBeerStyle())
                .price(beer.getPrice())
                .quantityOnHand(beer.getQuantityOnHand())
                .upc(beer.getUpc())
                .updateDate(LocalDateTime.now());
        return beerRepository.save(builder.build());
    }

    @Override
    public Beer updateById(UUID uuid, Beer beer) {
        Optional<Beer> beerToUpdate = beerRepository.findById(uuid);
        if (beerToUpdate.isEmpty()) {
            // Ideally throw an exception or return a meaningful response
            return null;
        }
        Beer.BeerBuilder builder = beerToUpdate.get().toBuilder();
        if (StringUtils.hasText(beer.getBeerName()))
            builder.beerName(beer.getBeerName());
        if (beer.getBeerStyle() != null)
            builder.beerStyle(beer.getBeerStyle());
        if (beer.getPrice() != null)
            builder.price(beer.getPrice());
        if (beer.getQuantityOnHand() != null)
            builder.quantityOnHand(beer.getQuantityOnHand());
        if (StringUtils.hasText(beer.getUpc()))
            builder.upc(beer.getUpc());
        builder.updateDate(LocalDateTime.now());
        return beerRepository.save(builder.build());
    }

    @Override
    public void deleteById(UUID uuid) {
        beerRepository.deleteById(uuid);
    }
}
