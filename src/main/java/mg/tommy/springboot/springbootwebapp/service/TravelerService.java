package mg.tommy.springboot.springbootwebapp.service;

import mg.tommy.springboot.springbootwebapp.domain.embedded.Traveler;
import mg.tommy.springboot.springbootwebapp.repository.embedded.TravelerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TravelerService {

    private final TravelerRepository travelerRepository;

    @Autowired
    public TravelerService(TravelerRepository travelerRepository) {
        this.travelerRepository = travelerRepository;
    }

    public Iterable<Traveler> saveAll(Set<Traveler> travelers) {
        return travelerRepository.saveAll(travelers.stream().filter(traveler -> traveler.getId() == null).collect(Collectors.toSet()));
    }
}
