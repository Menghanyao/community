##萌汉曜の社区

##项目来源
[Github](https://github.com/codedrinker/community)

[B站视频](https://www.bilibili.com/video/av50200264)

##资料
[Spring](https://spring.io/guides)

[Spring Web](https://spring.io/guides/gs/serving-web-content/)

[Github Deploy Key](https://developer.github.com/v3/guides/managing-deploy-keys/#deploy-keys)

[Bootstrap](https://v3.bootcss.com/getting-started/)

[Github OAuth](https://developer.github.com/apps/building-oauth-apps/creating-an-oauth-app/)

[Github OAuth Work Flow](https://developer.github.com/apps/building-oauth-apps/authorizing-oauth-apps/)

[OKHttp](https://square.github.io/okhttp/)

[FastJson](https://mvnrepository.com/search?q=fastjson)

[Spring boot + Mybatis](http://mybatis.org/spring-boot-starter/mybatis-spring-boot-autoconfigure/)

[Spring](https://docs.spring.io/spring-boot/docs/2.0.0.RC1/reference/htmlsingle/#boot-features-embedded-database-support)

[Thymeleaf](https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#setting-attribute-values)

[Spring Dev Tools](https://github.com/codedrinker/community)

[Spring MVC](https://docs.spring.io/spring/docs/5.0.3.RELEASE/spring-framework-reference/web.html#mvc-handlermapping-interceptor)


##工具
[Git](https://git-scm.com/download)

[Visual Paradigm](https://www.visual-paradigm.com/cn/)

[lombok](https://projectlombok.org)

##Script
```sql
create table USER
(
    ID           INT auto_increment,
    ACCOUNT_ID   VARCHAR(100),
    NAME         VARCHAR(50),
    TOKEN        CHAR(36),
    GMT_CREATE   BIGINT,
    GMT_MODIFIED BIGINT,
    AVATAR_URL   VARCHAR(100),
    constraint USER_PK
        primary key (ID)
);
```
```sql
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
```