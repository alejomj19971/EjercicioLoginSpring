package com.tienda.login.validation;

import com.tienda.login.service.IUserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExistsByUserNameValidation implements ConstraintValidator<ExistsByUserName,String> {
    @Autowired
    private IUserService userService;


    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        return !userService.existsByUsername(username);
    }

}
