package ru.javawebinar.topjava.web.meal;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = "/meals", produces = MediaType.APPLICATION_JSON_VALUE)
public class MealUIController extends AbstractMealController {

    @Override
    @GetMapping("/all")
    public List<MealTo> getAll() {
        return super.getAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        super.delete(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void updateOrCreate(
            @RequestParam(required = false) Integer id,
            @RequestParam LocalDateTime dateTime,
            @RequestParam String description,
            @RequestParam Integer calories
    ) {
        Meal meal = new Meal(dateTime, description, calories);
        if (Objects.isNull(id)) {
            super.create(meal);
        } else {
            super.update(meal, id);
        }
    }

    @GetMapping("/filter")
    public List<MealTo> getBetween(
            @RequestParam @Nullable LocalDate startDate,
            @RequestParam @Nullable LocalTime startTime,
            @RequestParam @Nullable LocalDate endDate,
            @RequestParam @Nullable LocalTime endTime
    ) {
        return super.getBetween(startDate, startTime, endDate, endTime);
    }
}
