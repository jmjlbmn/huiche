package org.huiche.test.sql.builder;

import org.huiche.extra.sql.builder.SqlBuilder;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Maning
 */
public class SqlBuilderTest {
    private SqlBuilder builder;

    @Before
    public void init() {
        builder = SqlBuilder.init(Config.URL, Config.USER, Config.PASSWORD);
    }

    @Test
    public void run() {
        builder.run("org.huiche");
    }
}