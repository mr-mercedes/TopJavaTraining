package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.repository.CrudRepository;
import ru.javawebinar.topjava.repository.InMemoryMealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger log = getLogger(MealServlet.class);
    private static final int CALORIES_PER_DAY = 2000;
    public static final String INSERT_OR_EDIT = "/meal.jsp";
    private CrudRepository<Meal, Integer> mealRepository;

    @Override
    public void init() {
        this.mealRepository = new InMemoryMealRepository();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to get meal");
        request.setCharacterEncoding("UTF-8");

        String forward;
        String action = request.getParameter("action");
        if ("edit".equals(action)) {
            log.debug("forward to edit meal");
            forward = INSERT_OR_EDIT;
            String mealId = request.getParameter("mealId");
            if (mealId != null) {
                edit(request, mealId);
            } else {
                log.debug("forward to create meal");
            }
        } else if ("delete".equals(action)) {
            log.debug("redirect to delete meal");
            delete(request, response);
            return;
        } else {
            log.debug("forward to meals");
            forward = "/meals.jsp";
            get(request);
        }

        request.getRequestDispatcher(forward).forward(request, response);
    }

    private void edit(HttpServletRequest request, String mealId) {
        int id = Integer.parseInt(mealId);
        MealTo meal = mealRepository.findById(id)
                .map(m -> new MealTo(
                        m.getId(),
                        m.getDateTime(),
                        m.getDescription(),
                        m.getCalories(),
                        m.getCalories() > CALORIES_PER_DAY)
                )
                .orElseThrow(RuntimeException::new);
        request.setAttribute("meal", meal);
        log.debug("success redirect to edit meal");
    }

    private void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int mealId = Integer.parseInt(request.getParameter("mealId"));
        mealRepository.delete(mealId);
        response.sendRedirect(request.getContextPath() + "/meals");
        log.debug("success delete meal");
    }

    private void get(HttpServletRequest request) {
        List<MealTo> meals = MealsUtil.filteredByStreams(
                mealRepository.findAll(),
                CALORIES_PER_DAY);
        request.setAttribute("meals", meals);
        log.debug("success get meals meal");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.debug("redirect to post meal");
        request.setCharacterEncoding("UTF-8");
        LocalDateTime dateTime = null;
        int calories = 0;
        String description = request.getParameter("description");

        try {
            dateTime = LocalDateTime.parse(request.getParameter("dateTime"));
            calories = Integer.parseInt(request.getParameter("calories"));
        } catch (DateTimeParseException | NumberFormatException e) {
            log.debug(e.getLocalizedMessage());
        }

        String mealId = request.getParameter("mealId");
        if (mealId == null || mealId.isEmpty()) {
            log.debug("post create meal");
            Meal meal = Meal.of(dateTime, description, calories);
            mealRepository.create(meal);
            log.debug("success create meal");
        } else {
            log.debug("post edit meal");
            int id = Integer.parseInt(mealId);
            Meal updatedMeal = Meal.of(id, dateTime, description, calories);
            mealRepository.update(updatedMeal);
            log.debug("success update meal");
        }
        response.sendRedirect(request.getContextPath() + "/meals");
    }
}
