package com.acutus.atk.ui.processor;

import com.acutus.atk.db.annotations.FieldFilter;
import com.acutus.atk.db.annotations.Index;
import com.acutus.atk.db.annotations.Sequence;
import com.acutus.atk.db.processor.AtkEntity;
import com.acutus.atk.entity.processor.Atk;
import com.acutus.atk.entity.processor.AtkProcessor;
import com.acutus.atk.reflection.Reflect;
import com.acutus.atk.util.Strings;
import com.acutus.atk.util.collection.Tuple4;
import com.google.auto.service.AutoService;
import com.sun.source.tree.MethodTree;
import com.sun.source.util.Trees;
import lombok.SneakyThrows;

import javax.annotation.processing.Processor;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.acutus.atk.util.AtkUtil.handle;

@SupportedAnnotationTypes("com.acutus.atk.ui.processor.AtkUI")
@SupportedSourceVersion(SourceVersion.RELEASE_21)
@AutoService(Processor.class)
public class AtkUIProcessor extends AtkProcessor {


    @Override
    public boolean isPrimitive(Element e) {
        return super.isPrimitive(e) || e.getAnnotation(Enumerated.class) != null;
    }

    @Override
    protected String getClassNameLine(Element element, String... removeStrings) {

        String className = super.getClassNameLine(element
                , "@Table", "@javax.persistence.Table", "@com.acutus.atk.ui.processor.AtkUI");

        String interFaces = getInterfaces(element).removeEmpty().toString(",");

        if (!interFaces.isEmpty()) {
            interFaces = "implements  " + interFaces;
        }

        return className.substring(0, className.indexOf("public class "))
                + String.format("public class %s extends AbstractAtkUI<%s,%s> %s{"
                , getClassName(element), getClassName(element), getSimpleEntityName(element), interFaces);

    }

    @Override
    protected String getClassName(Element element) {
        AtkUI atk = element.getAnnotation(AtkUI.class);
        return atk.className().isEmpty() ? element.getSimpleName() + atk.classNameExt() :
                atk.className();
    }

    private String getEntityName(Element element) {
        try {
            AtkUI atk = element.getAnnotation(AtkUI.class);
            return atk.entityClass().getName();
        } catch( MirroredTypeException mte ) {
            return mte.getTypeMirror().toString();
        }
    }

    private String getSimpleEntityName(Element element) {
        String name = getEntityName(element);
        return name.substring(name.lastIndexOf(".")+1);
    }

    @Override
    protected Strings getImports(Element element) {
        return super.getImports(element)
                .plus("import com.acutus.atk.ui.fields.*;")
                .plus("import com.acutus.atk.ui.AbstractAtkUI;")
                .plus("import "+getEntityName(element)+";")
                .removeDuplicates();
    }

    @Override
    protected Strings getStaticFields(Element parent) {
        return super.getStaticFields(parent);
    }

    private String getUIField(Element parent, Field field) {
        String fName = field.getName().replace("_","");
        return String.format("\tpublic final UIField<%s> %s = new UIField<>(%s,this);",
                getClassName(parent),fName,"\""+fName+"\"");
    }

    @Override
    protected Strings getExtraFields(Element parent) {
        return handle(() -> {
            Class entityClass = Class.forName(getEntityName(parent));
            List<java.lang.reflect.Field> staticFields = Arrays.stream(entityClass.getDeclaredFields())
                    .filter(f -> f.getName().startsWith("_")).collect(Collectors.toList());
            Strings fields = new Strings("\tprivate "+ getSimpleEntityName(parent) + " entity = new "+getSimpleEntityName(parent)+"();");
            staticFields.forEach(f -> fields.add(getUIField(parent,f)));
            return fields;
        });
    }

    @Override
    protected Strings getMethods(String className, Element element) {
        Strings methods =  super.getMethods(className, element);
        methods.add(String.format("\tpublic %s getEntity() {return entity;}",getSimpleEntityName(element)));
        return methods;
    }
}
