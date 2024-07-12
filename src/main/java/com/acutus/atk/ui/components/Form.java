package com.acutus.atk.ui.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.data.binder.Binder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public abstract class Form {
    private List<Binder> binders = new ArrayList<>();
    private FormLayout formLayout;

    public FormLayout build() {
        if (formLayout == null) {

        }
        return formLayout;
    }

}
