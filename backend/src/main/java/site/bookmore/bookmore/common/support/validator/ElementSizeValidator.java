package site.bookmore.bookmore.common.support.validator;

import site.bookmore.bookmore.common.support.annotation.ElementSize;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collection;

public class ElementSizeValidator implements ConstraintValidator<ElementSize, Collection<String>> {
    private int min;
    private int max;

    @Override
    public void initialize(ElementSize constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(Collection<String> value, ConstraintValidatorContext context) {
        if (value == null) return true;
        for (String element : value) {
            if (element.length() < min || element.length() > max) return false;
        }
        return true;
    }
}
