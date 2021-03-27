package org.xaplus.test.ledger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.xaplus.engine.XAPlusEngine;
import org.xaplus.engine.XAPlusRestServer;
import org.xaplus.engine.XAPlusXid;

import javax.transaction.xa.XAException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Transaction {
    static private final Logger logger = LoggerFactory.getLogger(Transaction.class);

    protected final String serverId;
    protected final XAPlusRestServer ledger1;
    protected final XAPlusRestServer ledger2;
    protected final XAPlusRestServer ledger3;

    public Transaction(String serverId, XAPlusRestServer ledger1, XAPlusRestServer ledger2, XAPlusRestServer ledger3) {
        this.serverId = serverId;
        this.ledger1 = ledger1;
        this.ledger2 = ledger2;
        this.ledger3 = ledger3;
    }

    protected boolean isResponsible(int userUid) {
        if (userUid < 0) {
            throw new IllegalArgumentException("Wrong userUid=" + userUid);
        }
        switch (serverId) {
            case "ledger-1":
                return userUid >= 0 && userUid < 1000;
            case "ledger-2":
                return userUid >= 1000 && userUid < 2000;
            case "ledger-3":
                return userUid >= 2000 && userUid < 3000;
            default:
                throw new IllegalArgumentException("Too big userUid=" + userUid);
        }
    }

    protected void jdbcCredit(XAPlusEngine engine, int userUid, int count) throws SQLException, XAException {
        logger.debug("Credit {} with {}", userUid, count);
        String masterDatabaseName = getMasterDatabaseName(userUid);
        Connection masterConnection = engine.enlistJdbc(masterDatabaseName);
        PreparedStatement masterSql = masterConnection.prepareStatement(
                "INSERT INTO ledger (l_user, l_credit) VALUES('" + userUid + "', '" + count + "');");
        masterSql.executeUpdate();
        String slaveDatabaseName = getSlaveDatabaseName(userUid);
        Connection slaveConnection = engine.enlistJdbc(slaveDatabaseName);
        PreparedStatement slaveSql = slaveConnection.prepareStatement(
                "INSERT INTO ledger (l_user, l_credit) VALUES('" + userUid + "', '" + count + "');");
        slaveSql.executeUpdate();
    }

    protected void jdbcDebet(XAPlusEngine engine, int userUid, int count) throws SQLException, XAException {
        logger.debug("Debet {} with {}", userUid, count);
        String masterDatabaseName = getMasterDatabaseName(userUid);
        Connection masterConnection = engine.enlistJdbc(masterDatabaseName);
        PreparedStatement masterSql = masterConnection.prepareStatement(
                "INSERT INTO ledger (l_user, l_debet) VALUES('" + userUid + "', '" + count + "');");
        masterSql.executeUpdate();
        String slaveDatabaseName = getSlaveDatabaseName(userUid);
        Connection slaveConnection = engine.enlistJdbc(slaveDatabaseName);
        PreparedStatement slaveSql = slaveConnection.prepareStatement(
                "INSERT INTO ledger (l_user, l_debet) VALUES('" + userUid + "', '" + count + "');");
        slaveSql.executeUpdate();
    }

    protected XAPlusXid callCredit(XAPlusEngine engine, int userUid, int count)
            throws XAException, RestClientException {
        logger.debug("CALL credit {} with {}", userUid, count);
        String serviceName = getServiceName(userUid);
        XAPlusXid xid = engine.createXAPlusXid(serviceName);
        RestTemplate restTemplate = new RestTemplate();
        String url = getServiceLocation(userUid) + "/credit?xid=" + xid + "&userUid=" + userUid + "&count=" + count;
        restTemplate.getForObject(url, Boolean.class);
        logger.debug("Call credit {} completed", userUid);
        return xid;
    }

    protected XAPlusXid callDebet(XAPlusEngine engine, int userUid, int count)
            throws XAException, RestClientException {
        logger.debug("Call debet {} with {}", userUid, count);
        String serviceName = getServiceName(userUid);
        XAPlusXid xid = engine.createXAPlusXid(serviceName);
        RestTemplate restTemplate = new RestTemplate();
        String url = getServiceLocation(userUid) + "/debet?xid=" + xid + "&userUid=" + userUid + "&count=" + count;
        boolean result = restTemplate.getForObject(url, Boolean.class);
        if (!result) {
            logger.warn("Credit request to {} rejected", url);
        }
        logger.debug("Call debet {} completed", userUid);
        return xid;
    }

    private String getServiceLocation(int userUid) {
        if (userUid < 0) {
            throw new IllegalArgumentException("Wrong userUid=" + userUid);
        } else if (userUid >= 0 && userUid < 1000) {
            return "http://" + ledger1.getHostname() + ":" + ledger1.getPort();
        } else if (userUid >= 1000 && userUid < 2000) {
            return "http://" + ledger2.getHostname() + ":" + ledger2.getPort();
        } else if (userUid >= 2000 && userUid < 3000) {
            return "http://" + ledger3.getHostname() + ":" + ledger3.getPort();
        } else {
            throw new IllegalArgumentException("Too big userUid=" + userUid);
        }
    }

    private String getServiceName(int userUid) {
        if (userUid < 0) {
            throw new IllegalArgumentException("Wrong userUid=" + userUid);
        }
        if (userUid >= 0 && userUid < 1000) {
            return "ledger-1";
        } else if (userUid >= 1000 && userUid < 2000) {
            return "ledger-2";
        } else if (userUid >= 2000 && userUid < 3000) {
            return "ledger-3";
        } else {
            throw new IllegalArgumentException("Too big userUid=" + userUid);
        }
    }

    protected String getMasterDatabaseName(int userUid) {
        if (userUid < 0) {
            throw new IllegalArgumentException("Wrong userUid=" + userUid);
        }
        if (userUid >= 0 && userUid < 1000) {
            return "database-1";
        } else if (userUid >= 1000 && userUid < 2000) {
            return "database-2";
        } else if (userUid >= 2000 && userUid < 3000) {
            return "database-3";
        } else {
            throw new IllegalArgumentException("Too big userUid=" + userUid);
        }
    }

    protected String getSlaveDatabaseName(int userUid) {
        if (userUid < 0) {
            throw new IllegalArgumentException("Wrong userUid=" + userUid);
        }
        if (userUid >= 0 && userUid < 1000) {
            return "database-4";
        } else if (userUid >= 1000 && userUid < 2000) {
            return "database-5";
        } else if (userUid >= 2000 && userUid < 3000) {
            return "database-6";
        } else {
            throw new IllegalArgumentException("Too big userUid=" + userUid);
        }
    }
}
