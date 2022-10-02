package project.phoneshop.util;

import project.phoneshop.model.payload.request.discountProgram.DiscountProgramRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DateCheckValidator implements ConstraintValidator<DateCheck, DiscountProgramRequest>{

        @Override
        public void initialize(DateCheck date) {
            // Nothing here
        }

        @Override
        public boolean isValid(DiscountProgramRequest dto, ConstraintValidatorContext constraintValidatorContext) {
            if (dto.getFromDate() == null || dto.getToDate() == null) {
                return true;
            }
            return dto.getFromDate().before(dto.getToDate());
        }
}
