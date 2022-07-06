package org.huiche.codegen;

import org.huiche.annotation.Table;
import org.huiche.codegen.dialect.MySqlDialect;
import org.huiche.codegen.dialect.SqlDialect;
import org.huiche.codegen.domain.ColumnInfo;
import org.huiche.codegen.domain.TableInfo;
import org.huiche.support.ReflectUtil;
import org.springframework.lang.NonNull;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author Maning
 */
public class CodegenDDL {
    private SqlDialect dialect;
    private Consumer<String> consumer = System.out::println;

    public static CodegenDDL ofMysql() {
        return of(MySqlDialect.DEFAULT);
    }

    public static CodegenDDL of(@NonNull SqlDialect dialect) {
        CodegenDDL ddl = new CodegenDDL();
        ddl.dialect = dialect;
        return ddl;
    }

    public CodegenDDL consumer(@NonNull Consumer<String> consumer) {
        this.consumer = consumer;
        return this;
    }

    public List<String> ddl(@NonNull Class<?>... classes) {
        return Arrays.stream(classes).map(this::ddl).collect(Collectors.toList());
    }

    public String ddl(@NonNull Class<?> clazz) {
        String ddl = dialect.createTable(TableInfo.of(clazz), ReflectUtil.scanNormalFields(clazz).stream().map(ColumnInfo::of).collect(Collectors.toList()));
        this.consumer.accept(ddl);
        return ddl;
    }

    public List<String> ddl(@NonNull String... packages) {
        return CodegenUtil.scan(i -> i.isAnnotationPresent(Table.class), packages).stream().map(this::ddl).collect(Collectors.toList());
    }
}
