package org.huiche.apt;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import org.huiche.dao.CrudDao;
import org.springframework.stereotype.Repository;

import javax.annotation.Generated;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

/**
 * @author Maning
 */
public class CrudDaoGenerator {
    private static final String NAME = CrudDaoGenerator.class.getCanonicalName();

    public TypeSpec createDao(TypeElement entity) {
        String entityName = entity.getSimpleName().toString();
        String daoName = entityName + "Dao";
        ClassName entityClass = ClassName.get(entity);
        return TypeSpec.classBuilder(daoName)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(AnnotationSpec.builder(Generated.class)
                        .addMember("value", "$S", NAME).build())
                .addAnnotation(AnnotationSpec.builder(Repository.class).build())
                .addJavadoc("generated by huiche-apt")
                .superclass(ParameterizedTypeName.get(ClassName.get(CrudDao.class), entityClass)).build();
    }
}