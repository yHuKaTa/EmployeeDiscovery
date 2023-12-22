package com.sirma.exam.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

/**
 * Global exception handler for API controllers.
 * Handles various exceptions and provides consistent JSON error responses.
 */
@RestControllerAdvice
@ControllerAdvice
public class ApiExceptionHandler {
    /**
     * Handles EntityNotFoundException and returns a JSON response with a 404 status code.
     *
     * @param ex       The EntityNotFoundException that was thrown.
     * @param response The HttpServletResponse used to send the response.
     * @throws IOException If an I/O error occurs while writing the response.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseBody
    void handlerEntityNotFoundException(EntityNotFoundException ex, HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.NOT_FOUND.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write("{\"error\": \"" + ex.getMessage() + "\"}");
    }

    /**
     * Handles ValidationException and returns a JSON response with a 400 status code.
     *
     * @param e        The ValidationException that was thrown.
     * @param response The HttpServletResponse used to send the response.
     * @throws IOException If an I/O error occurs while writing the response.
     */
    @ExceptionHandler(ValidationException.class)
    @ResponseBody
    public void handleValidationException(ValidationException e, HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(Map.of("errors", e.getMessage()));
        response.getWriter().write(json);
    }

    /**
     * Handles IllegalArgumentException and returns a JSON response with a 400 status code.
     *
     * @param ex        The IllegalArgumentException that was thrown.
     * @param response The HttpServletResponse used to send the response.
     * @throws IOException If an I/O error occurs while writing the response.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    void handleIllegalArgumentException(IllegalArgumentException ex, HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(Map.of("errors:", ex.getMessage()));
        response.getWriter().write(json);
    }

    /**
     * Handles IllegalAccessException and returns a JSON response with a 400 status code.
     *
     * @param ex        The IllegalAccessException that was thrown.
     * @param response The HttpServletResponse used to send the response.
     * @throws IOException If an I/O error occurs while writing the response.
     */
    @ExceptionHandler(IllegalAccessException.class)
    @ResponseBody
    void handleIllegalAccessException(IllegalArgumentException ex, HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(Map.of("errors:", ex.getMessage()));
        response.getWriter().write(json);
    }

    /**
     * Handles Custom ValidationException and returns a JSON response with a 400 status code.
     *
     * @param ex        The ConstraintViolationException that was thrown.
     * @param response The HttpServletResponse used to send the response.
     * @throws IOException If an I/O error occurs while writing the response.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    void handleConstraintViolationException(ConstraintViolationException ex, HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        List<String> errors = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage).toList();

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(Map.of("errors", errors));
        response.getWriter().write(json);
    }

    /**
     * Handles DB exceptions and returns a JSON response with a 400 status code.
     *
     * @param ex        The SQLIntegrityConstraintViolationException that was thrown.
     * @param response The HttpServletResponse used to send the response.
     * @throws IOException If an I/O error occurs while writing the response.
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    @ResponseBody
    void handleDataIntegrityViolationException(SQLIntegrityConstraintViolationException ex, HttpServletResponse response) throws IOException {
        String errorMessage = ex.getMessage();
        int index = errorMessage.indexOf("for key");
        if (index != -1) {
            errorMessage = errorMessage.substring(0, index).trim();

            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(Map.of("errors", errorMessage));
            response.getWriter().write(json);
        }
    }

    /**
     * Handles MethodArgumentNotValidException and returns a JSON response with a 400 status code.
     *
     * @param ex        The MethodArgumentNotValidException that was thrown.
     * @param response The HttpServletResponse used to send the response.
     * @throws IOException If an I/O error occurs while writing the response.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    void handleValidationExceptions2(MethodArgumentNotValidException ex, HttpServletResponse response) throws IOException {
        BindingResult result = ex.getBindingResult();
        List<String> errorMessages = result.getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(Map.of("errors:", errorMessages));
        response.getWriter().write(json);
    }

    /**
     * Handles HttpMessageNotReadableException and returns a JSON response with a 406 status code.
     *
     * @param e        The MissingServletRequestPartException that was thrown.
     * @param response The HttpServletResponse used to send the response.
     * @throws IOException If an I/O error occurs while writing the response.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    void handleMissingBodyException(HttpMessageNotReadableException e, HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write("{\"error\": \"Required body is missing!\"}");
    }

    /**
     * Handles MissingServletRequestPartException and returns a JSON response with a 406 status code.
     *
     * @param e        The MissingServletRequestPartException that was thrown.
     * @param response The HttpServletResponse used to send the response.
     * @throws IOException If an I/O error occurs while writing the response.
     */
    @ExceptionHandler(MissingServletRequestPartException.class)
    void handleMissingPartException(MissingServletRequestPartException e, HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write("{\"error\": \"Required part \"" + e.getRequestPartName() + "\" is missing!\"}");
    }

    /**
     * Handles MissingRequestHeaderException and returns a JSON response with a 406 status code.
     *
     * @param e        The MissingRequestHeaderException that was thrown.
     * @param response The HttpServletResponse used to send the response.
     * @throws IOException If an I/O error occurs while writing the response.
     */
    @ExceptionHandler(MissingRequestHeaderException.class)
    void handleMissingHeaderException(MissingRequestHeaderException e, HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write("{\"error\": \"Required part \"" + e.getHeaderName() + "\" is missing!\"}");
    }

    /**
     * Handles EntityExistsException and returns a JSON response with a 400 status code.
     *
     * @param e        The EntityExistsException that was thrown.
     * @param response The HttpServletResponse used to send the response.
     * @throws IOException If an I/O error occurs while writing the response.
     */
    @ExceptionHandler(EntityExistsException.class)
    void handleExistingEntityException(EntityExistsException e, HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
    }

    /**
     * Handles MethodArgumentTypeMismatchException and returns a JSON response with a 406 status code.
     *
     * @param e        The MethodArgumentTypeMismatchException that was thrown.
     * @param response The HttpServletResponse used to send the response.
     * @throws IOException If an I/O error occurs while writing the response.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    void handleMismatchInputsException(MethodArgumentTypeMismatchException e, HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write("{\"error\": \"Invalid input data. Please check " + e.getParameter().getParameterName() + " and try again.\"}");
    }

    /**
     * Handles DateTimeParseException and returns a JSON response with a 406 status code.
     *
     * @param exception The DateTimeParseException that was thrown.
     * @param response  The HttpServletResponse used to send the response.
     * @throws IOException If an I/O error occurs while writing the response.
     */
    @ExceptionHandler(DateTimeParseException.class)
    public void handleDateTimeParseException(DateTimeParseException exception, HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write("{\"error\": \"Invalid input data. Please check input " + exception.getParsedString() + " and try again with format yyyy-MM-dd.\"}");
    }
}
