package org.example.utils;

import org.example.dto.AuthRequest;
import org.example.exceptions.NotValidArgumentException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

public class ValidatorUtil {

    public static void validate(Object object, Object request){
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<AuthRequest>> violations = validator.validate(request);
        for (ConstraintViolation<AuthRequest> violation : violations) {
            throw new NotValidArgumentException(violation.getMessage());
        }
    }
}
