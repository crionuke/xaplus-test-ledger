package com.crionuke.xaplus.test.ledger;

import com.crionuke.xaplus.XAPlusEngine;
import com.crionuke.xaplus.XAPlusRestServer;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.postgresql.xa.PGXADataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.XADataSource;

@Configuration
public class Config {

    private XAPlusEngine xaPlusEngine;

    public Config(XAPlusEngine xaPlusEngine) {
        this.xaPlusEngine = xaPlusEngine;
    }

    @Bean("tlog")
    public DataSource tlog() {
        DataSource dataSource = new DataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUsername("tlog");
        dataSource.setPassword("qwe123");
        dataSource.setUrl("jdbc:postgresql://127.0.0.1:10000/tlog");
        xaPlusEngine.setTLogDataSource(dataSource);
        return dataSource;
    }

    @Bean("database1")
    public XADataSource database1() {
        PGXADataSource pgxaDataSource = new PGXADataSource();
        pgxaDataSource.setUser("ledger");
        pgxaDataSource.setPassword("qwe123");
        pgxaDataSource.setUrl("jdbc:postgresql://127.0.0.1:10001/ledger");
        org.apache.tomcat.jdbc.pool.XADataSource xaDataSource = new org.apache.tomcat.jdbc.pool.XADataSource();
        xaDataSource.setDataSource(pgxaDataSource);
        xaPlusEngine.register(xaDataSource, "database1");
        return xaDataSource;
    }

    @Bean("database2")
    public XADataSource database2() {
        PGXADataSource pgxaDataSource = new PGXADataSource();
        pgxaDataSource.setUser("ledger");
        pgxaDataSource.setPassword("qwe123");
        pgxaDataSource.setUrl("jdbc:postgresql://127.0.0.1:10002/ledger");
        org.apache.tomcat.jdbc.pool.XADataSource xaDataSource = new org.apache.tomcat.jdbc.pool.XADataSource();
        xaDataSource.setDataSource(pgxaDataSource);
        xaPlusEngine.register(xaDataSource, "database2");
        return xaDataSource;
    }

    @Bean("database3")
    public XADataSource database3() {
        PGXADataSource pgxaDataSource = new PGXADataSource();
        pgxaDataSource.setUser("ledger");
        pgxaDataSource.setPassword("qwe123");
        pgxaDataSource.setUrl("jdbc:postgresql://127.0.0.1:10003/ledger");
        org.apache.tomcat.jdbc.pool.XADataSource xaDataSource = new org.apache.tomcat.jdbc.pool.XADataSource();
        xaDataSource.setDataSource(pgxaDataSource);
        xaPlusEngine.register(xaDataSource, "database3");
        return xaDataSource;
    }

    @Bean("database4")
    public XADataSource database4() {
        PGXADataSource pgxaDataSource = new PGXADataSource();
        pgxaDataSource.setUser("ledger");
        pgxaDataSource.setPassword("qwe123");
        pgxaDataSource.setUrl("jdbc:postgresql://127.0.0.1:10004/ledger");
        org.apache.tomcat.jdbc.pool.XADataSource xaDataSource = new org.apache.tomcat.jdbc.pool.XADataSource();
        xaDataSource.setDataSource(pgxaDataSource);
        xaPlusEngine.register(xaDataSource, "database4");
        return xaDataSource;
    }

    @Bean("database5")
    public XADataSource database5() {
        PGXADataSource pgxaDataSource = new PGXADataSource();
        pgxaDataSource.setUser("ledger");
        pgxaDataSource.setPassword("qwe123");
        pgxaDataSource.setUrl("jdbc:postgresql://127.0.0.1:10005/ledger");
        org.apache.tomcat.jdbc.pool.XADataSource xaDataSource = new org.apache.tomcat.jdbc.pool.XADataSource();
        xaDataSource.setDataSource(pgxaDataSource);
        xaPlusEngine.register(xaDataSource, "database5");
        return xaDataSource;
    }

    @Bean("database6")
    public XADataSource database6() {
        PGXADataSource pgxaDataSource = new PGXADataSource();
        pgxaDataSource.setUser("ledger");
        pgxaDataSource.setPassword("qwe123");
        pgxaDataSource.setUrl("jdbc:postgresql://127.0.0.1:10006/ledger");
        org.apache.tomcat.jdbc.pool.XADataSource xaDataSource = new org.apache.tomcat.jdbc.pool.XADataSource();
        xaDataSource.setDataSource(pgxaDataSource);
        xaPlusEngine.register(xaDataSource, "database6");
        return xaDataSource;
    }

    @Bean("ledger-1")
    public XAPlusRestServer ledger1() {
        XAPlusRestServer xaPlusRestServer = new XAPlusRestServer("127.0.0.1", 8001);
        xaPlusEngine.register(xaPlusRestServer, "ledger-1");
        return xaPlusRestServer;
    }

    @Bean("ledger-2")
    public XAPlusRestServer ledger2() {
        XAPlusRestServer xaPlusRestServer = new XAPlusRestServer("127.0.0.1", 8002);
        xaPlusEngine.register(xaPlusRestServer, "ledger-2");
        return xaPlusRestServer;
    }

    @Bean("ledger-3")
    public XAPlusRestServer ledger3() {
        XAPlusRestServer xaPlusRestServer = new XAPlusRestServer("127.0.0.1", 8003);
        xaPlusEngine.register(xaPlusRestServer, "ledger-3");
        return xaPlusRestServer;
    }
}
