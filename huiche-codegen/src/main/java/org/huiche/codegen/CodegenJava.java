package org.huiche.codegen;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import org.huiche.annotation.Table;
import org.huiche.codegen.domain.EntityInfo;
import org.huiche.codegen.domain.TemplateInfo;
import org.springframework.core.io.ClassPathResource;
import org.springframework.lang.NonNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Maning
 */
public class CodegenJava {
    private Map<String, Template> map;
    private String dist;

    public static CodegenJava of() {
        CodegenJava codegen = new CodegenJava();
        codegen.map = new HashMap<>(2);
        try {
            codegen.map.put("Api.java", Mustache.compiler().escapeHTML(false).compile(new InputStreamReader(new ClassPathResource("templates/api.mustache").getInputStream())));
            codegen.map.put("Service.java", Mustache.compiler().escapeHTML(false).compile(new InputStreamReader(new ClassPathResource("templates/service.mustache").getInputStream())));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        codegen.dist = "";
        return codegen;
    }

    public static CodegenJava of(@NonNull TemplateInfo... templates) {
        CodegenJava codegen = new CodegenJava();
        codegen.map = new HashMap<>(templates.length);
        for (TemplateInfo info : templates) {
            codegen.map.put(info.getSuffix(), Mustache.compiler().escapeHTML(false).compile(info.getSuffix()));
        }
        codegen.dist = "";
        return codegen;
    }

    public CodegenJava dist(@NonNull String dist) {
        this.dist = dist;
        File file = new File(dist);
        if (!file.exists() && file.mkdirs()) {
            throw new RuntimeException("不支持的路径:" + dist);
        }
        return this;
    }

    public void codegen(@NonNull Class<?>... classes) {
        for (Class<?> clazz : classes) {
            codegen(clazz);
        }
    }

    public void codegen(@NonNull Class<?> clazz) {
        map.forEach((suffix, template) -> {
            try {
                Files.write(Paths.get(dist, clazz.getSimpleName() + suffix), template.execute(EntityInfo.of(clazz)).getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void codegen(@NonNull String... packages) {
        CodegenUtil.scan(i -> i.isAnnotationPresent(Table.class), packages).forEach(this::codegen);
    }
}
