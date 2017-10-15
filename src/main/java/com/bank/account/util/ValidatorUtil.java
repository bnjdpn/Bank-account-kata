package com.bank.account.util;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import org.apache.commons.collections.CollectionUtils;

public class ValidatorUtil {

    private static final Validator
        VALIDATOR =
        Validation.buildDefaultValidatorFactory().getValidator();

    private ValidatorUtil() {
        // Util
    }

    public static <T> T validate(T o) {
        Set<ConstraintViolation<T>> violations = VALIDATOR.validate(o);

        if (CollectionUtils.isNotEmpty(violations)) {
            throw new IllegalArgumentException("Validation failed : " + violations);
        }

        return o;
    }

}
