package mg.tommy.springboot.springbootwebapp.service.brewing;

import lombok.RequiredArgsConstructor;
import mg.tommy.springboot.springbootwebapp.model.domain.embedded.Beer;
import mg.tommy.springboot.springbootwebapp.model.domain.embedded.BeerStyle;
import mg.tommy.springboot.springbootwebapp.model.dto.BeerDto;
import mg.tommy.springboot.springbootwebapp.mapper.BeerMapper;
import mg.tommy.springboot.springbootwebapp.repository.embedded.BeerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static java.lang.Math.min;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Service
public class BeerServiceImpl implements BeerService {
    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 25;
    private static final int MAX_PAGE_SIZE = 1000;

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Override
    public Page<BeerDto> find(String beerName, BeerStyle beerStyle, boolean showInventory, Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);
        return (StringUtils.hasText(beerName) && beerStyle != null ? findBeersByNameAndStyle(beerName, beerStyle, pageRequest) :
                StringUtils.hasText(beerName) ? findBeersByName(beerName, pageRequest) :
                beerStyle != null ? findBeersByStyle(beerStyle, pageRequest) :
                        beerRepository.findAll(pageRequest))
                .map(beer -> {
                    if (!showInventory) beer.setQuantityOnHand(null);
                    return beerMapper.toBeerDto(beer);
                });
    }

    private PageRequest buildPageRequest(Integer pageNumber, Integer pageSize) {
        int queryPageNumber = pageNumber != null && pageNumber > 0 ? pageNumber : DEFAULT_PAGE;
        int queryPageSize = pageSize != null && pageSize > 0 ? min(pageSize, MAX_PAGE_SIZE) : DEFAULT_PAGE_SIZE;

        Sort sort = Sort.by(Sort.Order.asc("beerName"));

        return PageRequest.of(queryPageNumber, queryPageSize, sort);
    }

    private Page<Beer> findBeersByNameAndStyle(String beerName, BeerStyle beerStyle, PageRequest pageRequest) {
        return beerRepository.findAllByBeerNameIsLikeIgnoreCaseAndBeerStyle("%" + beerName + "%", beerStyle, pageRequest);
    }

    private Page<Beer> findBeersByName(String beerName, PageRequest pageRequest) {
        return beerRepository.findAllByBeerNameIsLikeIgnoreCase("%" + beerName + "%", pageRequest);
    }

    private Page<Beer> findBeersByStyle(BeerStyle beerStyle, PageRequest pageRequest) {
        return beerRepository.findAllByBeerStyle(beerStyle, pageRequest);
    }

    @Override
    public Optional<BeerDto> findById(UUID id) {
        return beerRepository.findById(id).map(beerMapper::toBeerDto);
    }

    @Override
    public BeerDto save(BeerDto beer) {
        return beerMapper.toBeerDto(
                beerRepository.save(beerMapper.toBeer(beer).toBuilder()
                        .createdDate(LocalDateTime.now())
                        .updateDate(LocalDateTime.now())
                        .build())
        );
    }

    @Override
    public Optional<BeerDto> overwriteById(UUID uuid, BeerDto beerDto) {
        AtomicReference<Optional<BeerDto>> atomicReference = new AtomicReference<>();
        beerRepository.findById(uuid)
                .ifPresentOrElse(beerToUpdate -> {
                    Beer updatedBeer = beerRepository.save(beerToUpdate.toBuilder()
                            .beerName(beerDto.getBeerName())
                            .beerStyle(beerDto.getBeerStyle())
                            .price(beerDto.getPrice())
                            .quantityOnHand(beerDto.getQuantityOnHand())
                            .upc(beerDto.getUpc())
                            .updateDate(LocalDateTime.now())
                            .build());
                    atomicReference.set(Optional.of(beerMapper.toBeerDto(updatedBeer)));
                }, () -> {
                    atomicReference.set(Optional.empty());
                });
        return atomicReference.get();
    }

    @Override
    public Optional<BeerDto> updateById(UUID uuid, BeerDto beerDto) {
        Optional<Beer> beerToUpdate = beerRepository.findById(uuid);
        if (beerToUpdate.isEmpty()) {
            return Optional.empty();
        }
        Beer.BeerBuilder builder = beerToUpdate.get().toBuilder();
        if (StringUtils.hasText(beerDto.getBeerName()))
            builder.beerName(beerDto.getBeerName());
        if (beerDto.getBeerStyle() != null)
            builder.beerStyle(BeerStyle.valueOf(beerDto.getBeerStyle()));
        if (beerDto.getPrice() != null)
            builder.price(beerDto.getPrice());
        if (beerDto.getQuantityOnHand() != null)
            builder.quantityOnHand(beerDto.getQuantityOnHand());
        if (StringUtils.hasText(beerDto.getUpc()))
            builder.upc(beerDto.getUpc());
        builder.updateDate(LocalDateTime.now());
        return ofNullable(beerMapper.toBeerDto(beerRepository.save(builder.build())));
    }

    @Override
    public boolean deleteById(UUID uuid) {
        if (!beerRepository.existsById(uuid))
            return false;

        beerRepository.deleteById(uuid);
        return true;
    }
}
