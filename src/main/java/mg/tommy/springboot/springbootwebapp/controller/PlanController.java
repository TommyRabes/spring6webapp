package mg.tommy.springboot.springbootwebapp.controller;

import mg.tommy.springboot.springbootwebapp.domain.embedded.Plan;
import mg.tommy.springboot.springbootwebapp.exception.PlanNotFoundException;
import mg.tommy.springboot.springbootwebapp.service.PlanService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/plans")
public class PlanController {

    private final PlanService planService;

    public PlanController(PlanService planService) {
        this.planService = planService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Iterable<Plan> listPlans() {
        return planService.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Plan getPlan(@PathVariable("id") Long id) {
        Optional<Plan> plan = planService.findById(id);
        if (plan.isEmpty()) {
            throw new PlanNotFoundException("Plan of id : " + id + " not found");
        }
        return plan.get();
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public Plan savePlan(@RequestBody Plan plan) {
        return planService.save(plan);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Plan updatePlan(@PathVariable("id") Long id, @RequestBody Plan plan) {
        return planService.update(id, plan);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deletePlan(@PathVariable("id") Long id) {
        planService.delete(id);
    }

}
