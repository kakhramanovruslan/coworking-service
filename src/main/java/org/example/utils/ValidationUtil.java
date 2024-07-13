package org.example.utils;

import org.example.exceptions.NotValidArgumentException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class ValidationUtil {

    private static Validator validator;

    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    /**
     * Validates the given object based on its validation annotations.
     *
     * @param object the object to validate
     * @throws NotValidArgumentException if validation fails, with the validation error message
     */
    public static <T> void validate(T object) throws NotValidArgumentException {
        Set<ConstraintViolation<T>> violations = validator.validate(object);
        if (!violations.isEmpty()) {
            ConstraintViolation<T> violation = violations.iterator().next();
            throw new NotValidArgumentException(violation.getMessage());
        }
    }
}
