package com.ombagoes.springrestjwt.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

@Component
@Slf4j
public class ValidationUtil {
    public String doValidation(BindingResult bindingResult) {
        StringBuilder bindErrorBuilder = new StringBuilder();
        List<FieldError> errors = bindingResult.getFieldErrors();
        for (FieldError error : errors ) {
            bindErrorBuilder.append(error.getField()).append(" : ").append(error.getDefaultMessage()).append(", ");
        }
        if(!bindErrorBuilder.isEmpty()) {
            return bindErrorBuilder.substring(0,bindErrorBuilder.length() - 2);
        }
        return "";
    }
}
