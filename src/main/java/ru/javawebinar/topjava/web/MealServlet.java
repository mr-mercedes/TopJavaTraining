package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.storage.InMemoryMealStorage;
import ru.javawebinar.topjava.util.Constants;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.StorageUtil;

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
    private final InMemoryMealStorage mealStorage = StorageUtil.connect();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meal");
        request.setCharacterEncoding("UTF-8");

        String forward;
        String action = request.getParameter("action");
        String INSERT_OR_EDIT = "/meal.jsp";
        if ("edit".equals(action)) {
            forward = INSERT_OR_EDIT;
            edit(request);
        } else if ("create".equals(action)) {
            forward = INSERT_OR_EDIT;
        } else if ("delete".equals(action)) {
            delete(request, response);
            return;
        } else {
            forward = "/meals.jsp";
            get(request);
        }

        request.getRequestDispatcher(forward).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
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

        Meal meal = new Meal(dateTime, description, calories);
        String mealId = request.getParameter("mealId");
        if (mealId == null) {
            mealStorage.createMeal(meal);
        } else {
            int id = Integer.parseInt(mealId);
            mealStorage.updateMeal(id, meal);
        }
        response.sendRedirect(request.getContextPath() + "/meals");
    }

    private void get(HttpServletRequest request) {
        List<MealTo> meals = MealsUtil.filteredByStreams(
                mealStorage.findAllMeals(),
                Constants.CALORIES_PER_DAY);
        request.setAttribute("meals", meals);
    }

    private void edit(HttpServletRequest request) {
        int mealId = Integer.parseInt(request.getParameter("mealId"));
        MealTo meal = mealStorage.findById(mealId)
                .map(m -> new MealTo(m.getId(), m.getDateTime(), m.getDescription(), m.getCalories(), m.getCalories() > Constants.CALORIES_PER_DAY))
                .orElseThrow(RuntimeException::new);
        request.setAttribute("meal", meal);
    }

    private void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int mealId = Integer.parseInt(request.getParameter("mealId"));
        mealStorage.deleteMeal(mealId);
        response.sendRedirect(request.getContextPath() + "/meals");
    }
}
