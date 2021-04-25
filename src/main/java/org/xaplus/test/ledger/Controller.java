package org.xaplus.test.ledger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xaplus.engine.XAPlusEngine;
import org.xaplus.engine.XAPlusRestServer;
import org.xaplus.engine.XAPlusXid;

@RestController
public class Controller extends Transaction {
    static private final Logger logger = LoggerFactory.getLogger(Controller.class);

    private final XAPlusEngine engine;

    public Controller(@Qualifier("ledger-1") XAPlusRestServer ledger1,
                      @Qualifier("ledger-2") XAPlusRestServer ledger2,
                      @Qualifier("ledger-3") XAPlusRestServer ledger3,
                      XAPlusEngine engine) {
        super(engine.getServerId(), ledger1, ledger2, ledger3);
        this.engine = engine;
    }

    @PostMapping("/transfer")
    public boolean transfer(@RequestParam(value = "rqUid") int rqUid,
                            @RequestParam(value = "fromUser") int fromUser,
                            @RequestParam(value = "toUser") int toUser,
                            @RequestParam(value = "count") int count) throws InterruptedException {
        if (logger.isInfoEnabled()) {
            logger.info("Transfer rqUid={}, fromUser={}, toUser={}, count={}", rqUid, fromUser, toUser, count);
        }
        try {
            engine.begin();

            if (isResponsible(fromUser)) {
                jdbcCredit(engine, rqUid, fromUser, count);
            } else {
                callCredit(engine, rqUid, fromUser, count);
            }

            if (isResponsible(toUser)) {
                jdbcDebet(engine, rqUid, toUser, count);
            } else {
                callDebet(engine, rqUid, toUser, count);
            }

            engine.commit();
            return true;
        } catch (Exception e) {
            logger.warn(e.getMessage());
            engine.rollback();
            return false;
        }
    }

    @PostMapping("/debet")
    public boolean debet(@RequestParam(value = "xid") String xidString,
                         @RequestParam(value = "rqUid") int rqUid,
                         @RequestParam(value = "toUser") int toUser,
                         @RequestParam(value = "count") int count)
            throws InterruptedException {
        if (logger.isInfoEnabled()) {
            logger.info("Transfer rqUid={}, toUser={}, count={}, xid={}", rqUid, toUser, count, xidString);
        }
        try {
            engine.join(XAPlusXid.fromString(xidString));
            if (isResponsible(toUser)) {
                jdbcDebet(engine, rqUid, toUser, count);
            } else {
                throw new IllegalStateException(serverId + " not fit for user=" + toUser);
            }
            engine.commit();
            return true;
        } catch (Exception e) {
            logger.warn(e.getMessage());
            engine.rollback();
            return false;
        }
    }

    @PostMapping("/credit")
    public boolean credit(@RequestParam(value = "xid") String xidString,
                          @RequestParam(value = "rqUid") int rqUid,
                          @RequestParam(value = "fromUser") int fromUser,
                          @RequestParam(value = "count") int count)
            throws InterruptedException {
        if (logger.isInfoEnabled()) {
            logger.info("Credit rqUid={}, fromUser={}, count={}, xid={}", rqUid, fromUser, count, xidString);
        }
        try {
            engine.join(XAPlusXid.fromString(xidString));
            if (isResponsible(fromUser)) {
                jdbcCredit(engine, rqUid, fromUser, count);
            } else {
                throw new IllegalStateException(serverId + " not fit for user=" + fromUser);
            }
            engine.commit();
            return true;
        } catch (Exception e) {
            logger.warn(e.getMessage());
            engine.rollback();
            return false;
        }
    }
}
