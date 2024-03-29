package org.xaplus.test.ledger;

import org.postgresql.xa.PGXADataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.xaplus.engine.XAPlus;
import org.xaplus.engine.XAPlusEngine;
import org.xaplus.engine.XAPlusRestServer;

import javax.sql.XADataSource;

@Configuration
public class Config {

    @Bean
    public XAPlus getXAPlus(@Value("${xaplus.serverId}") String serverId,
                            @Value("${xaplus.transactionsTimeoutInSeconds}") int transactionsTimeoutInSeconds,
                            @Value("${xaplus.recoveryTimeoutInSeconds}") int recoveryTimeoutInSeconds,
                            @Value("${xaplus.recoveryPeriodInSeconds}") int recoveryPeriodInSeconds) {
        return new XAPlus(serverId, transactionsTimeoutInSeconds, recoveryTimeoutInSeconds, recoveryPeriodInSeconds);
    }

    @Bean
    public XAPlusEngine getXAPlusEngine(XAPlus xaPlus) {
        return xaPlus.construct();
    }

    @Bean
    public XADataSource getDatabase(XAPlusEngine engine, @Value("${db.url}") String url,
                                    @Value("${db.user}") String user, @Value("${db.password}") String password) {
        PGXADataSource pgxaDataSource = new PGXADataSource();
        pgxaDataSource.setUser(user);
        pgxaDataSource.setPassword(password);
        pgxaDataSource.setUrl(url);
        org.apache.tomcat.jdbc.pool.XADataSource xaDataSource = new org.apache.tomcat.jdbc.pool.XADataSource();
        xaDataSource.setDataSource(pgxaDataSource);
        xaDataSource.setValidationQuery("SELECT 1");
        xaDataSource.setValidationQueryTimeout(1000);
        xaDataSource.setTestOnBorrow(true);
        xaDataSource.setRemoveAbandoned(true);
        engine.register(xaDataSource, "database");
        engine.setTLogDataSource(xaDataSource);
        return xaDataSource;
    }

    @Bean("ledger-1")
    public XAPlusRestServer getLedger1(XAPlusEngine engine, @Value("${ledger-1.hostname}") String hostname,
                                       @Value("${ledger-1.port}") int port) {
        XAPlusRestServer xaPlusRestServer = new XAPlusRestServer(hostname, port);
        engine.register(xaPlusRestServer, "ledger-1");
        return xaPlusRestServer;
    }

    @Bean("ledger-2")
    public XAPlusRestServer getLedger2(XAPlusEngine engine, @Value("${ledger-2.hostname}") String hostname,
                                       @Value("${ledger-2.port}") int port) {
        XAPlusRestServer xaPlusRestServer = new XAPlusRestServer(hostname, port);
        engine.register(xaPlusRestServer, "ledger-2");
        return xaPlusRestServer;
    }

    @Bean("ledger-3")
    public XAPlusRestServer getLedger3(XAPlusEngine engine, @Value("${ledger-3.hostname}") String hostname,
                                       @Value("${ledger-3.port}") int port) {
        XAPlusRestServer xaPlusRestServer = new XAPlusRestServer(hostname, port);
        engine.register(xaPlusRestServer, "ledger-3");
        return xaPlusRestServer;
    }
}
