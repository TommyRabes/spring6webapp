package mg.tommy.springboot.springbootwebapp.controller.api;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mg.tommy.springboot.springbootwebapp.dto.BeerDto;
import mg.tommy.springboot.springbootwebapp.service.brewing.BeerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(BeerApiController.ROOT_PATH)
public class BeerApiController {
    public static final String ROOT_PATH = "/api/v1/beers";

    private final BeerService beerService;

    @RequestMapping(method = RequestMethod.GET)
    public Iterable<BeerDto> getAllBeers() {
        return beerService.findAll();
    }

    @GetMapping("{uuid}")
    public ResponseEntity<BeerDto> getBeerByUUID(@PathVariable("uuid") UUID uuid) {
        Optional<BeerDto> beerDto = beerService.findById(uuid);
        if (beerDto.isEmpty()) {
            throw new NotFoundException("Beer with uuid : " + uuid + " not found");
        }
        return ResponseEntity.ok().body(beerDto.get());
    }

    @PostMapping
    public ResponseEntity<BeerDto> saveBeer(@RequestBody BeerDto beerDto) {
        BeerDto savedBeer = beerService.save(beerDto);
        return ResponseEntity
                .created(URI.create("/api/v1/beers/" + savedBeer.getId().toString()))
                .body(savedBeer);
    }

    @PutMapping("{uuid}")
    public ResponseEntity updateById(@PathVariable("uuid") UUID uuid, @RequestBody BeerDto beerDto) {
        beerService.overwriteById(uuid, beerDto);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("{uuid}")
    public ResponseEntity patchById(@PathVariable("uuid") UUID uuid, @RequestBody BeerDto beerDto) {
        BeerDto updatedBeer = beerService.updateById(uuid, beerDto);
        if (updatedBeer == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{uuid}")
    public ResponseEntity deleteById(@PathVariable("uuid") UUID uuid) {
        beerService.deleteById(uuid);
        return ResponseEntity.noContent().build();
    }

}
