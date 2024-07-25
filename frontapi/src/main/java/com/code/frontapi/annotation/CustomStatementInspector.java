package com.code.frontapi.annotation;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.resource.jdbc.spi.StatementInspector;

@Slf4j
public class CustomStatementInspector implements StatementInspector {

    @Override
    public String inspect(String sql) {
        log.debug("SQL Statement: {}", sql);
        return sql;
    }
}
