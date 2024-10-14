//package com.me.ecommerce.validators;
//
//import jakarta.validation.ConstraintValidator;
//import jakarta.validation.ConstraintValidatorContext;
//
//public class LuhnCheckValidator implements ConstraintValidator<LuhnCheck, String> {
//
//    @Override
//    public void initialize(LuhnCheck constraintAnnotation) {
//    }
//
//    @Override
//    public boolean isValid(String value, ConstraintValidatorContext context) {
//        if (value == null || value.trim().isEmpty()) {
//            return true; // Consider empty or null as valid; other validators can handle required
//        }
//
//        // Remove spaces and check Luhn
//        String sanitizedValue = value.replaceAll("\\s+", "");
//        return isLuhnValid(sanitizedValue);
//    }
//
//    private boolean isLuhnValid(String number) {
//        int sum = 0;
//        boolean alternate = false;
//        for (int i = number.length() - 1; i >= 0; i--) {
//            int n = Integer.parseInt(number.substring(i, i + 1));
//            if (alternate) {
//                n *= 2;
//                if (n > 9) {
//                    n -= 9;
//                }
//            }
//            sum += n;
//            alternate = !alternate;
//        }
//        return (sum % 10 == 0);
//    }
//}
