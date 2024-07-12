package com.acutus.atk.ui.fields;

import com.acutus.atk.db.AbstractAtkEntity;
import com.acutus.atk.db.AtkEnField;
import com.acutus.atk.form.fields.Email;
import com.acutus.atk.form.fields.Named;
import com.acutus.atk.form.fields.TextArea;
import com.acutus.atk.reflection.Reflect;
import com.acutus.atk.ui.AbstractAtkUI;
import com.acutus.atk.ui.fields.enums.Type;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValidation;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import jakarta.annotation.security.DenyAll;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.parameters.P;

import java.lang.reflect.Field;
import java.util.Optional;

import static com.acutus.atk.util.StringUtils.isEmpty;

public class UIField<R extends AbstractAtkUI> {
    private String fieldName;
    private R uiEntity;
    private Type type;

    private Component component;

    @Getter @Setter
    private String label, hint;

    private TextField textField;
    public UIField(String fieldName,R uiEntity) {
        this.fieldName = fieldName;
        this.uiEntity = uiEntity;
    }

    private Field getSrcField() {
        return Reflect.getFields(uiEntity.getEntity().getBaseClass()).getByName(fieldName).get();
    }

    private Type getType() {
        if (type == null) {
            Email email = getSrcField().getAnnotation(Email.class);
            TextArea textArea = getSrcField().getAnnotation(TextArea.class);

            if (email != null) return Type.EMAIL;
            if (textArea != null) return Type.TEXTAREA;
        }
        return Type.TEXT;
    }

    private void initDefaults() {
        Named named = getSrcField().getAnnotation(Named.class);
        label = label == null ? named.label() : label;
        hint = hint == null ? named.hint() : hint;
        hint = isEmpty(hint)  ? label : hint;
        type = type == null ? getType() : type;

    }
    public Component getComponent() {
        if (component == null) {
            component = new TextField(label);
            Binder.BindingBuilder builder= uiEntity.getBinder().forField((HasValue) component);
            ValidatorFactory.getValidator(type).ifPresent(v -> builder.withValidator(v));
        }
        return component;
    }
}
