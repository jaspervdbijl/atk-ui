package com.acutus.atk.ui.fields;

import com.acutus.atk.ui.fields.enums.Type;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.data.validator.EmailValidator;

import java.util.Optional;

import static com.acutus.atk.ui.fields.enums.Type.EMAIL;

public class ValidatorFactory {

    public static Optional<Validator> getValidator(Type type) {
        switch (type) {
            case EMAIL:
                return Optional.of(new EmailValidator("Invalid email address"));
            default:
                return Optional.empty();
        }
    }
}
