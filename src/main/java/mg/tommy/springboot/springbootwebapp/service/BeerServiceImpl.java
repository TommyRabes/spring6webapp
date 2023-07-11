package mg.tommy.springboot.springbootwebapp.service;

import lombok.AllArgsConstructor;
import mg.tommy.springboot.springbootwebapp.domain.embedded.Beer;
import mg.tommy.springboot.springbootwebapp.repository.embedded.BeerRepository;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
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
    public Beer updateById(UUID uuid, Beer beer) {
        Optional<Beer> beerToUpdate = beerRepository.findById(uuid);
        if (beerToUpdate.isEmpty()) {
            // Ideally throw an exception or return a meaningful response
            return null;
        }
        Beer.BeerBuilder builder = beerToUpdate.get().toBuilder();
        if (beer.getBeerName() != null)
            builder.beerName(beer.getBeerName());
        if (beer.getBeerStyle() != null)
            builder.beerStyle(beer.getBeerStyle());
        if (beer.getPrice() != null)
            builder.price(beer.getPrice());
        if (beer.getQuantityOnHand() != null)
            builder.quantityOnHand(beer.getQuantityOnHand());
        if (beer.getUpc() != null)
            builder.upc(beer.getUpc());
        builder.updateDate(LocalDateTime.now());
        return beerRepository.save(builder.build());
    }

    @Override
    public void deleteById(UUID uuid) {
        beerRepository.deleteById(uuid);
    }
}
