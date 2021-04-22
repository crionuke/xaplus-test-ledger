CREATE TABLE ledger (
    l_id bigserial PRIMARY KEY,
    l_rq_uid bigint,
    l_user bigint,
    l_debet bigint,
    l_credit bigint
);
CREATE TABLE tlog (
    t_id        bigserial PRIMARY KEY,
    t_timestamp timestamp NOT NULL,
    t_server_id varchar(64) NOT NULL,
    t_gtrid     bytea,
    t_status    boolean NOT NULL
);