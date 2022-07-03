package org.huiche.apt;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import org.huiche.annotation.Table;
import org.ifinalframework.auto.service.annotation.AutoProcessor;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;

/**
 * @author Maning
 */
@AutoProcessor
public class HuicheAnnotationProcessor extends AbstractProcessor {
    private final CrudDaoGenerator daoGenerator = new CrudDaoGenerator();
    private Filer filer;
    private Messager messager;
    private Elements elementUtils;
    private QuerydslMapperGenerator mapperGenerator;

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(Table.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
        elementUtils = processingEnv.getElementUtils();
        mapperGenerator = new QuerydslMapperGenerator(elementUtils, messager, processingEnv.getTypeUtils());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(Table.class)) {
            boolean isType = element instanceof TypeElement;
            if (!isType) {
                continue;
            }
            TypeElement entityType = (TypeElement) element;
            String packageName = elementUtils.getPackageOf(element).getQualifiedName().toString();
            String fatherPackage = packageName.substring(0, packageName.lastIndexOf("."));
            Table table = element.getAnnotation(Table.class);
            if (table.generateMapper()) {
                String mapperPackage = fatherPackage + ".mapper";
                writeFile(mapperPackage, mapperGenerator.createMapper(entityType, mapperPackage));
            }
            if (table.generateDao()) {
                writeFile(fatherPackage + ".dao", daoGenerator.createDao(entityType));
            }

        }
        return false;
    }

    private void writeFile(String packageName, TypeSpec clazz) {
        try {
            JavaFile javaFile = JavaFile.builder(packageName, clazz).build();
            javaFile.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
            messager.printMessage(Diagnostic.Kind.ERROR, "write file fail:" + e.getLocalizedMessage());
        }
    }
}
