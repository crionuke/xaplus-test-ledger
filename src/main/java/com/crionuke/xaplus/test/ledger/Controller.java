	package com.crionuke.xaplus.test.ledger;

import com.crionuke.xaplus.XAPlusEngine;
import com.crionuke.xaplus.XAPlusRestServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping("/recovery")
    public boolean recovery() throws InterruptedException {
        engine.recovery();
        return true;
    }

    @RequestMapping("/transfer")
    public boolean transfer(@RequestParam(value="fromUserUid") int fromUserUid,
                            @RequestParam(value="toUserUid1") int toUserUid1,
                            @RequestParam(value="count1") int count1,
                            @RequestParam(value="toUserUid2") int toUserUid2,
                            @RequestParam(value="count2") int count2) throws InterruptedException {
        try {
            engine.begin();
            if (isResponsible(fromUserUid)) {
                jdbcCredit(engine, fromUserUid, count1 + count2);
            } else {
                callCredit(engine, fromUserUid, count1 + count2);
            }

            if (isResponsible(toUserUid1)) {
                jdbcDebet(engine, toUserUid1, count1);
            } else {
                callDebet(engine, toUserUid1, count1);
            }

            if (isResponsible(toUserUid2)) {
                jdbcDebet(engine, toUserUid2, count2);
            } else {
                callDebet(engine, toUserUid2, count2);
            }
            engine.commit();
        } catch (Exception e) {
            logger.warn(e.getMessage());
            engine.rollback();
        }
        return true;
    }

    @RequestMapping("/debet")
    public boolean debet(@RequestParam(value="xid") String xid,
                      @RequestParam(value="userUid") int userUid, @RequestParam(value="count") int count)
            throws InterruptedException {
        try {
            engine.join(xid);
            if (isResponsible(userUid)) {
                jdbcDebet(engine, userUid, count);
            } else {
                throw new IllegalStateException(serverId + " not fit for userUid=" + userUid);
            }
            engine.commit();
        } catch (Exception e) {
            logger.warn(e.getMessage());
            engine.rollback();
        }
        return true;
    }

    @RequestMapping("/credit")
    public boolean credit(@RequestParam(value="xid") String xid,
                       @RequestParam(value="userUid") int userUid, @RequestParam(value="count") int count)
            throws InterruptedException {
        try {
            engine.join(xid);
            if (isResponsible(userUid)) {
                jdbcCredit(engine, userUid, count);
            } else {
                throw new IllegalStateException(serverId + " not fit for userUid=" + userUid);
            }
            engine.commit();
        } catch (Exception e) {
            logger.warn(e.getMessage());
            engine.rollback();
        }
        return true;
    }
}
