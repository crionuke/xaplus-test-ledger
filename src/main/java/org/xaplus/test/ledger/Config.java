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

    private XAPlusEngine xaPlusEngine;

    public Config(XAPlusEngine xaPlusEngine) {
        this.xaPlusEngine = xaPlusEngine;
    }

    @Bean
    public XAPlusEngine getXAPlusEngine(@Value("${xaplus.serverId}") String serverId,
                                        @Value("${xaplus.defaultTimeoutInSeconds}") int defaultTimeoutInSeconds) {
        XAPlus xaPlus = new XAPlus(serverId, defaultTimeoutInSeconds);
        xaPlus.start();
        return xaPlus.getEngine();
    }

    @Bean("database-1")
    public XADataSource getDatabase1(XAPlusEngine engine) {
        PGXADataSource pgxaDataSource = new PGXADataSource();
        pgxaDataSource.setUser("ledger");
        pgxaDataSource.setPassword("ledger");
        pgxaDataSource.setUrl("jdbc:postgresql://127.0.0.1:10001/ledger");
        org.apache.tomcat.jdbc.pool.XADataSource xaDataSource = new org.apache.tomcat.jdbc.pool.XADataSource();
        xaDataSource.setDataSource(pgxaDataSource);
        engine.register(xaDataSource, "database-1");
        return xaDataSource;
    }

    @Bean("database-2")
    public XADataSource getDatabase2(XAPlusEngine engine) {
        PGXADataSource pgxaDataSource = new PGXADataSource();
        pgxaDataSource.setUser("ledger");
        pgxaDataSource.setPassword("ledger");
        pgxaDataSource.setUrl("jdbc:postgresql://127.0.0.1:10002/ledger");
        org.apache.tomcat.jdbc.pool.XADataSource xaDataSource = new org.apache.tomcat.jdbc.pool.XADataSource();
        xaDataSource.setDataSource(pgxaDataSource);
        engine.register(xaDataSource, "database-2");
        return xaDataSource;
    }

    @Bean("database-3")
    public XADataSource getDatabase3(XAPlusEngine engine) {
        PGXADataSource pgxaDataSource = new PGXADataSource();
        pgxaDataSource.setUser("ledger");
        pgxaDataSource.setPassword("ledger");
        pgxaDataSource.setUrl("jdbc:postgresql://127.0.0.1:10003/ledger");
        org.apache.tomcat.jdbc.pool.XADataSource xaDataSource = new org.apache.tomcat.jdbc.pool.XADataSource();
        xaDataSource.setDataSource(pgxaDataSource);
        engine.register(xaDataSource, "database-3");
        return xaDataSource;
    }

    @Bean("database-4")
    public XADataSource getDatabas4(XAPlusEngine engine) {
        PGXADataSource pgxaDataSource = new PGXADataSource();
        pgxaDataSource.setUser("ledger");
        pgxaDataSource.setPassword("ledger");
        pgxaDataSource.setUrl("jdbc:postgresql://127.0.0.1:10004/ledger");
        org.apache.tomcat.jdbc.pool.XADataSource xaDataSource = new org.apache.tomcat.jdbc.pool.XADataSource();
        xaDataSource.setDataSource(pgxaDataSource);
        engine.register(xaDataSource, "database-4");
        return xaDataSource;
    }

    @Bean("database-5")
    public XADataSource getDatabase5(XAPlusEngine engine) {
        PGXADataSource pgxaDataSource = new PGXADataSource();
        pgxaDataSource.setUser("ledger");
        pgxaDataSource.setPassword("ledger");
        pgxaDataSource.setUrl("jdbc:postgresql://127.0.0.1:10005/ledger");
        org.apache.tomcat.jdbc.pool.XADataSource xaDataSource = new org.apache.tomcat.jdbc.pool.XADataSource();
        xaDataSource.setDataSource(pgxaDataSource);
        engine.register(xaDataSource, "database-5");
        return xaDataSource;
    }

    @Bean("database-6")
    public XADataSource getDatabase6(XAPlusEngine engine) {
        PGXADataSource pgxaDataSource = new PGXADataSource();
        pgxaDataSource.setUser("ledger");
        pgxaDataSource.setPassword("ledger");
        pgxaDataSource.setUrl("jdbc:postgresql://127.0.0.1:10006/ledger");
        org.apache.tomcat.jdbc.pool.XADataSource xaDataSource = new org.apache.tomcat.jdbc.pool.XADataSource();
        xaDataSource.setDataSource(pgxaDataSource);
        engine.register(xaDataSource, "database-6");
        return xaDataSource;
    }

    @Bean("ledger-1")
    public XAPlusRestServer getLedger1() {
        XAPlusRestServer xaPlusRestServer = new XAPlusRestServer("127.0.0.1", 8001);
        xaPlusEngine.register(xaPlusRestServer, "ledger-1");
        return xaPlusRestServer;
    }

    @Bean("ledger-2")
    public XAPlusRestServer getLedger2() {
        XAPlusRestServer xaPlusRestServer = new XAPlusRestServer("127.0.0.1", 8002);
        xaPlusEngine.register(xaPlusRestServer, "ledger-2");
        return xaPlusRestServer;
    }

    @Bean("ledger-3")
    public XAPlusRestServer getLedger3() {
        XAPlusRestServer xaPlusRestServer = new XAPlusRestServer("127.0.0.1", 8003);
        xaPlusEngine.register(xaPlusRestServer, "ledger-3");
        return xaPlusRestServer;
    }
}
