package org.huiche.codegen.domain;

import org.springframework.lang.NonNull;

import java.io.Reader;

/**
 * @author Maning
 */
public class TemplateInfo {
    private String suffix;
    private Reader reader;

    public static TemplateInfo of(@NonNull String suffix, @NonNull Reader reader) {
        TemplateInfo info = new TemplateInfo();
        info.suffix = suffix;
        info.reader = reader;
        return info;
    }

    public String getSuffix() {
        return suffix;
    }

    public Reader getReader() {
        return reader;
    }
}
