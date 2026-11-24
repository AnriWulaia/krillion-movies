-- Registered account (email + password only)
CREATE TABLE app_user (
    id            BIGSERIAL PRIMARY KEY,
    email         VARCHAR(254) NOT NULL UNIQUE,
    password_hash VARCHAR(100) NOT NULL,
    display_name  VARCHAR(30)  NOT NULL,
    created_at    TIMESTAMPTZ  NOT NULL DEFAULT now()
);

-- Every participant is a player. Anonymous players have user_id = NULL.
CREATE TABLE player (
    id         UUID PRIMARY KEY,
    user_id    BIGINT UNIQUE REFERENCES app_user(id),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- A question and the rule that defines its valid answers
CREATE TABLE question (
    id                 BIGSERIAL PRIMARY KEY,
    text               TEXT        NOT NULL,
    rule_type          VARCHAR(40) NOT NULL,
    rule_params        JSONB       NOT NULL,
    accepted_type      VARCHAR(10) NOT NULL,
    anchor_key         VARCHAR(80) NOT NULL,
    question_hash      VARCHAR(64) NOT NULL UNIQUE,
    valid_answer_count INT         NOT NULL,
    created_at         TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- The full set of valid answers, fetched from TMDB at generation time
CREATE TABLE question_valid_answer (
    question_id BIGINT        NOT NULL REFERENCES question(id) ON DELETE CASCADE,
    media_type  VARCHAR(10)   NOT NULL,
    tmdb_id     BIGINT        NOT NULL,
    title       VARCHAR(300)  NOT NULL,
    year        INT,
    popularity  NUMERIC(10,3) NOT NULL DEFAULT 0,
    PRIMARY KEY (question_id, media_type, tmdb_id)
);

-- One row per calendar day
CREATE TABLE daily_set (
    id         BIGSERIAL PRIMARY KEY,
    play_date  DATE        NOT NULL UNIQUE,
    status     VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',  -- ACTIVE | FINALIZED
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE daily_set_question (
    daily_set_id BIGINT   NOT NULL REFERENCES daily_set(id) ON DELETE CASCADE,
    question_id  BIGINT   NOT NULL REFERENCES question(id),
    position     SMALLINT NOT NULL CHECK (position BETWEEN 1 AND 7),
    PRIMARY KEY (daily_set_id, position),
    UNIQUE (daily_set_id, question_id)
);

-- One attempt per player per question: holds the server-side timer and the result
CREATE TABLE question_attempt (
    id                 BIGSERIAL PRIMARY KEY,
    player_id          UUID        NOT NULL REFERENCES player(id),
    question_id        BIGINT      NOT NULL REFERENCES question(id),
    play_date          DATE        NOT NULL,
    status             VARCHAR(12) NOT NULL DEFAULT 'IN_PROGRESS',
    started_at         TIMESTAMPTZ NOT NULL,
    deadline_at        TIMESTAMPTZ NOT NULL,
    bonuses_used       SMALLINT    NOT NULL DEFAULT 0 CHECK (bonuses_used >= 0),
    wrong_guess_count  INT         NOT NULL DEFAULT 0,
    solved_media_type  VARCHAR(10),
    solved_tmdb_id     BIGINT,
    points_provisional INT         NOT NULL DEFAULT 0,
    points_final       INT,
    finished_at        TIMESTAMPTZ,
    UNIQUE (player_id, question_id)
);

-- Log of every distinct guess in an attempt
CREATE TABLE guess (
    id          BIGSERIAL PRIMARY KEY,
    attempt_id  BIGINT      NOT NULL REFERENCES question_attempt(id) ON DELETE CASCADE,
    media_type  VARCHAR(10) NOT NULL,
    tmdb_id     BIGINT      NOT NULL,
    is_valid    BOOLEAN     NOT NULL,
    guessed_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    UNIQUE (attempt_id, media_type, tmdb_id)
);

-- How many players solved each question with each answer (correct answers only)
CREATE TABLE answer_stat (
    question_id BIGINT      NOT NULL REFERENCES question(id) ON DELETE CASCADE,
    media_type  VARCHAR(10) NOT NULL,
    tmdb_id     BIGINT      NOT NULL,
    pick_count  INT         NOT NULL DEFAULT 0,
    PRIMARY KEY (question_id, media_type, tmdb_id)
);

CREATE INDEX idx_attempt_player_date ON question_attempt(player_id, play_date);
CREATE INDEX idx_attempt_question    ON question_attempt(question_id);
CREATE INDEX idx_guess_attempt       ON guess(attempt_id);
CREATE INDEX idx_question_anchor     ON question(anchor_key);
CREATE INDEX idx_daily_set_date      ON daily_set(play_date);