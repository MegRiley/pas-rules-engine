BEGIN TRANSACTION;




    CREATE TABLE IF NOT EXISTS Rules (
        "system" varchar,
        "code" varchar,
        "topic" varchar,
        "rule" varchar,
        "timestamp" datetime DEFAULT CURRENT_TIMESTAMP,
        CONSTRAINT pk_rules PRIMARY KEY ("system", "code")
    );

    CREATE TABLE IF NOT EXISTS Audit (
        "id" varchar,
        "type" varchar,
        "action" varchar,
        "outcome" varchar,
        "what" varchar,
        "query" varchar,
        "ip" varchar,
        "timestamp" datetime DEFAULT CURRENT_TIMESTAMP,
        "resource" clob
    );

    CREATE TABLE IF NOT EXISTS Client (
        "id" varchar PRIMARY KEY,
        "jwks" varchar DEFAULT NULL,
        "jwks_url" varchar DEFAULT NULL,
        "token" varchar DEFAULT NULL,
        "timestamp" datetime DEFAULT CURRENT_TIMESTAMP,
        "organization" clob
    );

COMMIT;