-- Active: 1713424437406@@127.0.0.1@3306@mykakao
show tables;

drop table if EXISTS kakao_board_comment_like;
drop table if EXISTS kakao_board_comment;
drop table if EXISTS kakao_board_donation;
drop table if EXISTS kakao_board_like;
drop table if EXISTS kakao_board_file;
drop table if EXISTS kakao_board;
drop table if EXISTS kakao_board_category;
drop table if EXISTS kakao_user;
drop table if EXISTS certification;

CREATE TABLE kakao_user(
    idx             INT PRIMARY KEY AUTO_INCREMENT COMMENT 'pk',
    username        VARCHAR(30)   NOT NULL    COMMENT '아이디',
    password        VARCHAR(255)  NOT NULL    COMMENT '비밀번호',
    type            VARCHAR(10)   NOT NULL    COMMENT '로그인타입',
    name            VARCHAR(10)   NOT NULL    COMMENT '사용자이름',
    name_num        INT           NOT NULL    COMMENT '번호',
    role            VARCHAR(10)   NOT NULL    COMMENT '권한',
    email           VARCHAR(255)   NULL        COMMENT '이메일',
    profile_img     VARCHAR(500)  NULL        COMMENT '프로필사진',
    create_date     TIMESTAMP     DEFAULT CURRENT_TIMESTAMP COMMENT '가입일',
    last_login_date TIMESTAMP     DEFAULT CURRENT_TIMESTAMP COMMENT '접속일'
) COMMENT '회원';

CREATE TABLE certification (
  username VARCHAR(30) PRIMARY KEY,
  email VARCHAR(255) NOT NULL,
  certification_number VARCHAR(6) NOT NULL
)

CREATE TABLE kakao_board_category(
    idx         INT PRIMARY KEY AUTO_INCREMENT COMMENT 'pk',
    category    VARCHAR(10)   NOT NULL    COMMENT '카테고리명'
) COMMENT '카테고리';

INSERT INTO kakao_board_category (category)
VALUES ('자원봉사');
INSERT INTO kakao_board_category (category)
VALUES ('어려운이웃');
INSERT INTO kakao_board_category (category)
VALUES ('교육지원');
INSERT INTO kakao_board_category (category)
VALUES ('지구촌');
INSERT INTO kakao_board_category (category)
VALUES ('우리사회');
INSERT INTO kakao_board_category (category)
VALUES ('청년');
INSERT INTO kakao_board_category (category)
VALUES ('아동|청소년');
INSERT INTO kakao_board_category (category)
VALUES ('여성');
INSERT INTO kakao_board_category (category)
VALUES ('이주민|다문화');
INSERT INTO kakao_board_category (category)
VALUES ('실버세대');
INSERT INTO kakao_board_category (category)
VALUES ('독거노인');
INSERT INTO kakao_board_category (category)
VALUES ('장애인');
INSERT INTO kakao_board_category (category)
VALUES ('저소득가정');

CREATE TABLE kakao_board(
    idx           INT PRIMARY KEY AUTO_INCREMENT COMMENT 'pk',
    user_fk       INT       NOT NULL      COMMENT 'user_fk',
    category_fk   INT       NOT NULL      COMMENT 'category_fk',
    thumbnail     VARCHAR(500)   NULL          COMMENT '썸네일',
    tag1          VARCHAR(20)   NOT NULL      COMMENT '테그1',
    tag2          VARCHAR(20)   NULL          COMMENT '테그2',
    tag3          VARCHAR(20)   NULL          COMMENT '테그3',
    title         VARCHAR(100)   NOT NULL      COMMENT '제목',
    content       TEXT      NOT NULL      COMMENT '내용',
    start_date    TIMESTAMP NOT NULL      COMMENT '시작일',
    end_date      TIMESTAMP NOT NULL      COMMENT '종료일',
    current_price INT       DEFAULT 0     COMMENT '현재금액',
    target_price  INT       NOT NULL      COMMENT '목표금액',
    read_count    INT       DEFAULT 0     COMMENT '조회수',
    like_count    INT       DEFAULT 0     COMMENT '응원수',
    comment_count INT       DEFAULT 0     COMMENT '댓글수',
    donate_count  INT       DEFAULT 0     COMMENT '후원수',
    ip              VARCHAR(255) NOT NULL COMMENT 'ip',
    create_date   TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일'
) COMMENT '게시글';

CREATE TABLE kakao_board_file(
    idx       INT PRIMARY KEY AUTO_INCREMENT COMMENT 'pk',
    board_fk  INT          NOT NULL    COMMENT 'board_fk',
    file_name VARCHAR(500) NOT NULL    COMMENT '파일이름'
);

CREATE TABLE kakao_board_like(
    idx         INT PRIMARY KEY AUTO_INCREMENT COMMENT 'pk',
    user_fk     INT       NOT NULL    COMMENT 'user_fk',
    board_fk    INT       NOT NULL    COMMENT 'board_fk',
    ip          VARCHAR(255) NOT NULL COMMENT 'ip'
) COMMENT '게시글응원';

CREATE TABLE kakao_board_comment(
    idx         INT PRIMARY KEY AUTO_INCREMENT COMMENT 'pk',
    user_fk     INT           NOT NULL        COMMENT 'user_fk',
    board_fk    INT           NOT NULL        COMMENT 'board_fk',
    content     VARCHAR(500)  NOT NULL        COMMENT '댓글',
    like_count  INT           DEFAULT 0       COMMENT '공감수',
    ip          VARCHAR(255)  NOT NULL        COMMENT 'ip',
    create_date TIMESTAMP     DEFAULT CURRENT_TIMESTAMP COMMENT '생성일'
) COMMENT '댓글';

CREATE TABLE kakao_board_comment_like(
    idx         INT PRIMARY KEY AUTO_INCREMENT COMMENT 'pk',
    user_fk     INT             NOT NULL    COMMENT 'user_fk',
    comment_fk  INT             NOT NULL    COMMENT 'comment_fk',
    ip          VARCHAR(255)    NOT NULL    COMMENT 'ip'
) COMMENT '댓글공감';

CREATE TABLE kakao_board_donation(
    idx             INT PRIMARY KEY AUTO_INCREMENT COMMENT 'pk',
    user_fk         INT             NOT NULL    COMMENT 'user_fk',
    board_fk        INT             NOT NULL    COMMENT 'board_fk',
    donate          INT             NOT NULL    COMMENT '후원금액',
    donate_comment  VARCHAR(500)    NULL        COMMENT '응원',
    ip              VARCHAR(255)    NOT NULL    COMMENT 'ip',
    create_date     TIMESTAMP       DEFAULT CURRENT_TIMESTAMP COMMENT '생성일'
) COMMENT '후원';



-- =================================================================
-- 제약조건
-- =================================================================

ALTER TABLE kakao_board
  ADD CONSTRAINT FK_kakao_user_TO_kakao_board
    FOREIGN KEY (user_fk)
    REFERENCES kakao_user(idx)
    ON DELETE CASCADE
;

ALTER TABLE kakao_board
  ADD CONSTRAINT FK_kakao_board_category_TO_kakao_board
    FOREIGN KEY (category_fk)
    REFERENCES kakao_board_category(idx)
;

ALTER TABLE kakao_board_file
  ADD CONSTRAINT FK_kakao_board_TO_kakao_board_file
    FOREIGN KEY (board_fk)
    REFERENCES kakao_board(idx)
    ON DELETE CASCADE
;

ALTER TABLE kakao_board_like
  ADD CONSTRAINT FK_kakao_user_TO_kakao_board_like
    FOREIGN KEY (user_fk)
    REFERENCES kakao_user(idx)
    ON DELETE CASCADE
;

ALTER TABLE kakao_board_like
  ADD CONSTRAINT FK_kakao_board_TO_kakao_board_like
    FOREIGN KEY (board_fk)
    REFERENCES kakao_board(idx)
    ON DELETE CASCADE
;

ALTER TABLE kakao_board_comment
  ADD CONSTRAINT FK_kakao_user_TO_kakao_comment
    FOREIGN KEY (user_fk)
    REFERENCES kakao_user(idx)
    ON DELETE CASCADE
;

ALTER TABLE kakao_board_comment
  ADD CONSTRAINT FK_kakao_board_TO_kakao_comment
    FOREIGN KEY (board_fk)
    REFERENCES kakao_board(idx)
    ON DELETE CASCADE
;


ALTER TABLE kakao_board_comment_like
  ADD CONSTRAINT FK_kakao_user_TO_kakao_comment_like
    FOREIGN KEY (user_fk)
    REFERENCES kakao_user(idx)
    ON DELETE CASCADE
;

ALTER TABLE kakao_board_comment_like
  ADD CONSTRAINT FK_kakao_comment_TO_kakao_comment_like
    FOREIGN KEY (comment_fk)
    REFERENCES kakao_board_comment(idx)
    ON DELETE CASCADE
;
