//package com.me.ecommerce.validators;
//
//import jakarta.validation.Constraint;
//import jakarta.validation.Payload;
//import java.lang.annotation.ElementType;
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//import java.lang.annotation.Target;
//import org.hibernate.validator.internal.constraintvalidators.hv.LuhnCheckValidator;
//
//@Constraint(validatedBy = LuhnCheckValidator.class)
//@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
//@Retention(RetentionPolicy.RUNTIME)
//public @interface LuhnCheck {
//    String message() default "Invalid credit card number";
//    Class<?>[] groups() default {};
//    Class<? extends Payload>[] payload() default {};
//}
//
