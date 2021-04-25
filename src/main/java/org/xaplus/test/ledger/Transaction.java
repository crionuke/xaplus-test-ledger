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

    static private final String INSERT_CREDIT = "INSERT INTO ledger (l_rq_uid, l_user, l_credit) VALUES(?, ?, ?)";
    static private final String INSERT_DEBET = "INSERT INTO ledger (l_rq_uid, l_user, l_debet) VALUES(?, ?, ?)";

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

    protected void jdbcCredit(XAPlusEngine engine, long rqUid, long fromUser, long count)
            throws SQLException, XAException {
        logger.debug("Credit from {} with {}, rqUid={}", fromUser, count, rqUid);
        Connection connection = engine.enlistJdbc("database");
        PreparedStatement statement = connection.prepareStatement(INSERT_CREDIT);
        statement.setLong(1, rqUid);
        statement.setLong(2, fromUser);
        statement.setLong(3, count);
        statement.executeUpdate();
    }

    protected void jdbcDebet(XAPlusEngine engine, long rqUid, long toUser, long count)
            throws SQLException, XAException {
        logger.debug("Debet to {} with {}, rqUid={}", toUser, count, rqUid);
        Connection connection = engine.enlistJdbc("database");
        PreparedStatement statement = connection.prepareStatement(INSERT_DEBET);
        statement.setLong(1, rqUid);
        statement.setLong(2, toUser);
        statement.setLong(3, count);
        statement.executeUpdate();
    }

    protected XAPlusXid callCredit(XAPlusEngine engine, long rqUid, long fromUser, long count)
            throws XAException, RestClientException {
        logger.debug("CALL credit {} with {}, rqUid={}", fromUser, count, rqUid);
        String serviceName = getServiceName(fromUser);
        XAPlusXid xid = engine.enlistXAPlus(serviceName);
        RestTemplate restTemplate = new RestTemplate();
        String url = getServiceLocation(fromUser) + "/credit" +
                "?xid=" + xid + "&rqUid=" + rqUid + "&fromUser=" + fromUser + "&count=" + count;
        restTemplate.postForObject(url, null, Boolean.class);
        return xid;
    }

    protected XAPlusXid callDebet(XAPlusEngine engine, long rqUid, long toUser, long count)
            throws XAException, RestClientException {
        logger.debug("Call debet {} with {}, rqUid={}", toUser, count, rqUid);
        String serviceName = getServiceName(toUser);
        XAPlusXid xid = engine.enlistXAPlus(serviceName);
        RestTemplate restTemplate = new RestTemplate();
        String url = getServiceLocation(toUser) + "/debet" +
                "?xid=" + xid + "&rqUid=" + rqUid + "&toUser=" + toUser + "&count=" + count;
        restTemplate.postForObject(url, null, Boolean.class);
        return xid;
    }

    protected boolean isResponsible(long user) {
        if (user < 0) {
            throw new IllegalArgumentException("Wrong user=" + user);
        }
        switch (serverId) {
            case "ledger-1":
                return user >= 0 && user < 1000;
            case "ledger-2":
                return user >= 1000 && user < 2000;
            case "ledger-3":
                return user >= 2000 && user < 3000;
            default:
                throw new IllegalArgumentException("Too big user=" + user);
        }
    }

    private String getServiceLocation(long userUid) {
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

    private String getServiceName(long userUid) {
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
}
