package com.acutus.atk.ui;

import com.acutus.atk.db.*;
import com.acutus.atk.db.driver.AbstractDriver;
import com.acutus.atk.db.processor.AtkEntity;
import com.acutus.atk.entity.AbstractAtk;
import com.acutus.atk.entity.AtkFieldList;
import com.acutus.atk.reflection.Reflect;
import com.acutus.atk.util.StringUtils;
import com.acutus.atk.util.Strings;
import com.vaadin.flow.data.binder.Binder;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import javax.persistence.Table;
import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.acutus.atk.db.Query.getOneToMany;
import static com.acutus.atk.db.Query.getOneToOneOrManyToOne;
import static com.acutus.atk.util.AtkUtil.handle;

public abstract class AbstractAtkUI<T extends AbstractAtkUI, O extends AbstractAtkEntity> extends AbstractAtk<T, O> {

    private Binder binder;

    public abstract O getEntity();

    public Binder<O> getBinder() {
        if (binder == null) {
            binder = new Binder(getEntity().getClass());
        }
        return binder;
    }

}
