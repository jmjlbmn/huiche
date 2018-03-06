package org.huiche.core.validation;

import org.hibernate.validator.HibernateValidator;

import javax.validation.Validation;
import javax.validation.Validator;

/**
 * @author Maning
 */
public class ValidatorUtil {
    public static Validator fastValidator = Validation.byProvider(HibernateValidator.class)
            .configure()
            .failFast(true)
            .buildValidatorFactory().getValidator();
}
