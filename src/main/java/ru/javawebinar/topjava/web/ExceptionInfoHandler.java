package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.util.exception.ErrorInfo;
import ru.javawebinar.topjava.util.exception.ErrorType;
import ru.javawebinar.topjava.util.exception.IllegalRequestDataException;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static ru.javawebinar.topjava.util.exception.ErrorType.*;

@RestControllerAdvice(annotations = RestController.class)
@Order(Ordered.HIGHEST_PRECEDENCE + 5)
public class ExceptionInfoHandler {

    @Autowired
    private MessageSource messageSource;

    private static final Logger log = LoggerFactory.getLogger(ExceptionInfoHandler.class);
    private static final Map<String, String> CONSTRAINS_I18N_MAP = Map.of(
            "users_unique_email_idx", "common.user.email.unique",
            "meal_unique_user_datetime_idx", "common.meals.unique");


    //  http://stackoverflow.com/a/22358422/548473
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(NotFoundException.class)
    public ErrorInfo notFoundError(HttpServletRequest req, NotFoundException e) {
        return logAndGetErrorInfo(req, e, false, DATA_NOT_FOUND, List.of(e.getMessage()));
    }

    @ResponseStatus(HttpStatus.CONFLICT)  // 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ErrorInfo conflict(HttpServletRequest req, DataIntegrityViolationException e) {
        return logAndGetErrorInfo(req, e, true, DATA_ERROR, Collections.emptyList());
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(BindException.class)
    public ErrorInfo bind(HttpServletRequest req, BindException e) {
        List<String> errors = e.getBindingResult().getFieldErrors().stream()
                .map(fe -> String.format("[%s] %s", fe.getField(), fe.getDefaultMessage()))
                .toList();
        return logAndGetErrorInfo(req, e, false, VALIDATION_ERROR, errors);
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(IllegalRequestDataException.class)
    public ErrorInfo illegalRequest(HttpServletRequest req, IllegalRequestDataException e) {
        List<String> errors = resolveConstraintErrors(e);
        return logAndGetErrorInfo(req, e, false, VALIDATION_ERROR, errors);
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler({MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class})
    public ErrorInfo requestFormat(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, false, VALIDATION_ERROR, List.of(e.getMessage()));
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorInfo internal(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, true, APP_ERROR, List.of(e.getMessage()));
    }

    private ErrorInfo logAndGetErrorInfo(HttpServletRequest req, Exception e, boolean logException, ErrorType type, List<String> errors) {
        Throwable root = ValidationUtil.getRootCause(e);
        if (logException) {
            log.error("{} at request {}", type, req.getRequestURL(), root);
        } else {
            log.warn("{} at request {}: {}", type, req.getRequestURL(), root.toString());
        }
        return new ErrorInfo(req.getRequestURL(), type, errors);
    }

    private List<String> resolveConstraintErrors(IllegalRequestDataException e) {
        Throwable root = ValidationUtil.getRootCause(e);

        String message = root.getMessage();
        if (message == null) return Collections.emptyList();

        String lower = message.toLowerCase();
        List<String> errors = new ArrayList<>();
        for (var entry : CONSTRAINS_I18N_MAP.entrySet()) {
            if (lower.contains(entry.getKey())) {
                errors.add(messageSource.getMessage(entry.getValue(), null, LocaleContextHolder.getLocale()));
            }
        }
        if (root instanceof IllegalRequestDataException ill) {
            errors.add(ill.getMessage());
        }
        return errors;
    }
}