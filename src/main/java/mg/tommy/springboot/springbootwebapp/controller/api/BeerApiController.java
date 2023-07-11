package mg.tommy.springboot.springbootwebapp.controller.api;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mg.tommy.springboot.springbootwebapp.domain.embedded.Beer;
import mg.tommy.springboot.springbootwebapp.service.BeerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/beers")
public class BeerApiController {
    private final BeerService beerService;

    @RequestMapping(method = RequestMethod.GET)
    public Iterable<Beer> getAllBeers() {
        return beerService.findAll();
    }

    @GetMapping("{uuid}")
    public ResponseEntity<Beer> getBeerByUUID(@PathVariable("uuid") UUID uuid) {
        Optional<Beer> beer = beerService.findById(uuid);
        if (beer.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(beer.get());
    }

    @PostMapping
    public ResponseEntity<Beer> saveBeer(@RequestBody Beer beer) {
        Beer savedBeer = beerService.save(beer);
        return ResponseEntity
                .created(URI.create("/api/v1/beers/" + savedBeer.getId().toString()))
                .body(savedBeer);
    }

    @PutMapping("{uuid}")
    public ResponseEntity updateById(@PathVariable("uuid") UUID uuid, @RequestBody Beer beer) {
        Beer updatedBeer = beerService.updateById(uuid, beer);
        if (updatedBeer == null) {
            beerService.save(beer);
        }
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("uuid")
    public ResponseEntity patchById(@PathVariable("uuid") UUID uuid, @RequestBody Beer beer) {
        Beer updatedBeer = beerService.updateById(uuid, beer);
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
