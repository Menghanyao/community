create table QUESTION
(
    ID            INT auto_increment,
    TITLE         VARCHAR(50),
    DESCRIPTION   TEXT,
    GMT_CREATE    BIGINT,
    GMT_MODIFIED  BIGINT,
    CREATOR       INT,
    COMMENT_COUNT INT,
    VIEW_COUNT    INT,
    LIKE_COUNT    INT,
    TAG           VARCHAR(256),
    constraint QUESTION_PK
        primary key (ID)
);