package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.javawebinar.topjava.model.Meal;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
@RequestMapping(value = "/meals")
public class JspMealController extends AbstractMealController {

    @GetMapping
    public String meals(Model model) {
        log.info("meals");
        model.addAttribute("meals", getAll());
        return "meals";
    }

    @GetMapping("/filter")
    public String getFilteredMeals(HttpServletRequest request, Model model) {
        log.info("filtering meals");
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
        model.addAttribute("meals",
                getBetween(startDate, startTime, endDate, endTime));
        return "meals";
    }

    @GetMapping("/form")
    public String toCreate(Model model) {
        log.info("to create meals");
        final Meal meal =
                new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        model.addAttribute("meal", meal);
        return "/mealForm";
    }

    @GetMapping("/form/{id}")
    public String toUpdate(@PathVariable Integer id, Model model) {
        log.info("to update meals");
        final Meal meal = get(id);
        model.addAttribute("meal", meal);
        return "/mealForm";
    }

    @PostMapping()
    public String createMeal(HttpServletRequest request, Model model) {
        log.info("create meals");
        final Meal meal = getMealFromRequest(request);
        model.addAttribute("meal", create(meal));
        return "redirect:/meals";
    }

    private Meal getMealFromRequest(HttpServletRequest request) {
        return new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
    }

    @PostMapping(value = "/{id}")
    public String updateMeal(@PathVariable Integer id, HttpServletRequest request) {
        log.info("update meals");
        final Meal meal = getMealFromRequest(request);
        update(meal, id);
        return "redirect:/meals";
    }

    @GetMapping("/delete/{id}")
    public String deleteMeal(@PathVariable Integer id) {
        log.info("delete meals");
        delete(id);
        return "redirect:/meals";
    }
}
