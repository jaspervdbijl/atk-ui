package com.acutus.atk.ui.processor;

import com.acutus.atk.db.AbstractAtkEntity;
import com.acutus.atk.entity.processor.Atk;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.temporal.ChronoUnit;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AtkUI {

    String className() default "";

    Class<? extends AbstractAtkEntity> entityClass();

    String classNameExt() default "UI";


}
