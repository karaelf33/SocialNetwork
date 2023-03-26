CREATE TABLE posts
(
    id         INT PRIMARY KEY AUTO_INCREMENT,
    post_date  TIMESTAMP    NOT NULL,
    author     VARCHAR(255) NOT NULL,
    content    TEXT         NOT NULL,
    view_count INT          NOT NULL
);