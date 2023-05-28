package ru.tsu.hits.kosterror.palindromepartitioning.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import ru.tsu.hits.kosterror.palindromepartitioning.dto.ApiError;
import ru.tsu.hits.kosterror.palindromepartitioning.exception.IncorrectInputStringException;

import java.time.LocalDateTime;

@ControllerAdvice
public class ExceptionHandlerAdvice {

    private final Logger logger;

    public ExceptionHandlerAdvice() {
        logger = LoggerFactory.getLogger(ExceptionHandlerAdvice.class);
    }

    @ExceptionHandler(IncorrectInputStringException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleIncorrectInputStringException(HttpServletRequest request,
                                                            IncorrectInputStringException exception
    ) {
        logger.error(exception.getMessage(), exception);

        ApiError apiError = new ApiError(
                request.getMethod(),
                request.getRequestURI(),
                400,
                exception.getMessage(),
                LocalDateTime.now()
        );

        return buildModelAndView(apiError);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleException(HttpServletRequest request,
                                        Exception exception) {
        logger.error(exception.getMessage(), exception);

        ApiError apiError = new ApiError(
                request.getMethod(),
                request.getRequestURI(),
                500,
                "Непредвиденная внутренняя ошибка сервера",
                LocalDateTime.now()
        );

        return buildModelAndView(apiError);
    }

    private ModelAndView buildModelAndView(ApiError error) {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("method", error.getMethod());
        modelAndView.addObject("path", error.getPath());
        modelAndView.addObject("statusCode", error.getStatusCode());
        modelAndView.addObject("message", error.getMessage());
        modelAndView.addObject("timestamp", error.getTimestamp());
        modelAndView.setViewName("error_page");

        return modelAndView;
    }

}
