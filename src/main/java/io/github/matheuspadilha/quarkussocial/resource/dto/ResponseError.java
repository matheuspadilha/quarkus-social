package io.github.matheuspadilha.quarkussocial.resource.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.ConstraintViolation;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseError {
    private String message;
    private Collection<FieldError> errors;

    public static <T> ResponseError createFromValidation(Set<ConstraintViolation<T>> violations) {
        List<FieldError> errors = violations.stream().map(cv -> new FieldError(cv.getPropertyPath().toString(), cv.getMessage())).toList();

        String message = "Validation Error";

        return new ResponseError(message, errors);
    }
}
