package mg.tommy.springboot.springbootwebapp.service.traveling;

import mg.tommy.springboot.springbootwebapp.domain.embedded.Itinerary;
import mg.tommy.springboot.springbootwebapp.domain.embedded.Plan;
import mg.tommy.springboot.springbootwebapp.repository.embedded.ItineraryRepository;
import mg.tommy.springboot.springbootwebapp.repository.embedded.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PlanService {

    private final PlanRepository planRepository;
    private final ItineraryRepository itineraryRepository;

    private final TravelerService travelerService;

    @Autowired
    public PlanService(PlanRepository planRepository, ItineraryRepository itineraryRepository, TravelerService travelerService) {
        this.planRepository = planRepository;
        this.itineraryRepository = itineraryRepository;
        this.travelerService = travelerService;
    }

    public Iterable<Plan> findAll() {
        return planRepository.findAll();
    }

    public Optional<Plan> findById(Long id) {
        return planRepository.findById(id);
    }

    @Transactional
    public Plan save(Plan plan) {
        itineraryRepository.save(plan.getItinerary());
        travelerService.saveAll(plan.getTravelers());
        return planRepository.save(plan);
    }

    @Transactional
    public Plan update(Long id, Plan plan) {
        Optional<Plan> updated = planRepository.findById(id);
        if (updated.isEmpty())
            return null;

        Plan updatedPlan = updated.get();
        if (plan.getName() != null)
            updatedPlan.setName(plan.getName());
        if (plan.getItinerary() != null) {
            Itinerary itineraryToUpdate = updatedPlan.getItinerary();
            itineraryToUpdate.setId(updatedPlan.getId());
            if (plan.getItinerary().getName() != null)
                itineraryToUpdate.setName(plan.getItinerary().getName());
            itineraryRepository.save(itineraryToUpdate);
        }
        if (plan.getTravelers() != null)
            updatedPlan.setTravelers(plan.getTravelers());

        return planRepository.save(updatedPlan);
    }

    @Transactional
    public void delete(Long id) {
        itineraryRepository.deleteById(id);
        planRepository.deleteById(id);
    }
}
