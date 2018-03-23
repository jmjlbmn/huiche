package org.huiche.extra.sql.builder.naming;

/**
 * @author Maning
 */
public class CamelCaseNamingRule implements NamingRule {
    private static final String UNDERLINE = "_";

    @Override
    public String javaName2SqlName(String javaName) {
        if (javaName == null || javaName.isEmpty()) {
            return "";
        }
        StringBuilder column = new StringBuilder();
        column.append(javaName.substring(0, 1).toLowerCase());
        for (int i = 1; i < javaName.length(); i++) {
            String s = javaName.substring(i, i + 1);
            if (!Character.isDigit(s.charAt(0)) && s.equals(s.toUpperCase())) {
                column.append(UNDERLINE);
            }
            column.append(s.toLowerCase());
        }
        return column.toString();
    }

    @Override
    public String sqlName2JavaName(String sqlName) {
        StringBuilder result = new StringBuilder();
        if (sqlName == null || sqlName.isEmpty()) {
            return "";
        } else if (!sqlName.contains(UNDERLINE)) {
            return sqlName.substring(0, 1).toLowerCase() + sqlName.substring(1);
        } else {
            String[] columns = sqlName.split(UNDERLINE);
            for (String columnSplit : columns) {
                if (columnSplit.isEmpty()) {
                    continue;
                }
                if (result.length() == 0) {
                    result.append(columnSplit.toLowerCase());
                } else {
                    result.append(columnSplit.substring(0, 1).toUpperCase()).append(columnSplit.substring(1).toLowerCase());
                }
            }
            return result.toString();
        }
    }

    private static final CamelCaseNamingRule INSTANCE = new CamelCaseNamingRule() {

    };

    public static NamingRule getInstance() {
        return INSTANCE;
    }

    private CamelCaseNamingRule() {
    }
}
