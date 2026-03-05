# Spring Boot 2 迁移示例项目

用于演练 **Spring Boot 2 → 3** 升级的 Maven 示例，覆盖 [spring-boot-2-to-3](https://github.com/chensoul/spring-boot-2-to-3) skill 中列出的主要迁移场景。

[English](README.md)

- **Spring Boot**: 2.7.18  
- **Java**: 11  
- **构建**: Maven  

## 场景对照

| 场景 | 位置 |
|------|------|
| **javax → jakarta** | `Item.java` (javax.persistence)、`LoggingInterceptor.java` (javax.servlet)、`DemoController.java` (javax.validation) |
| **@Autowired 单构造器** | `DemoController` 构造器 |
| **@RequestMapping(method=)** / **@PathVariable("id")** | `DemoController` |
| **HttpStatus** / **APPLICATION_JSON_UTF8_VALUE** | `DemoController`、`GlobalExceptionHandler` |
| **WebSecurityConfigurerAdapter** | `SecurityConfig` |
| **WebMvcConfigurerAdapter** | 配方会迁移继承该类的代码；本示例已用 `WebMvcConfigurer` 以通过编译 |
| **HandlerInterceptorAdapter** | 配方会迁移继承该类的代码；本示例已用 `HandlerInterceptor` 以通过编译 |
| **AsyncConfigurerSupport** | `AsyncConfig` |
| **JUnit 4** | `DemoControllerTest`（@RunWith、@Before、public @Test） |
| **旧配置属性** | `application.properties`：spring.datasource.schema/data/initialization-mode、management.contextPath |
| **API 文档** | springdoc-openapi 1.8.0（Boot 2.7 兼容；Springfox 与 2.7 不兼容）→ Boot 3 升级到 springdoc 2.x；pom 中 `springdoc-openapi-ui` |
| **JPA / Hibernate** | `Item` 实体、`ItemRepository` |
| **Flyway** | pom 中 flyway-core、`db/migration/V1__init.sql` |

## 项目结构

```
spring-boot-2-to-3-demo/
├── pom.xml
├── Dockerfile          # Multi-stage build (Java 11); after Boot 3 upgrade use Java 21 base image
├── .dockerignore
├── src/main/java/com/example/demomigration/
│   ├── DemoApplication.java
│   ├── config/
│   │   ├── AsyncConfig.java       # AsyncConfigurerSupport
│   │   ├── SecurityConfig.java    # WebSecurityConfigurerAdapter
│   │   └── WebMvcConfig.java      # WebMvcConfigurer
│   ├── persistence/
│   │   ├── Item.java              # javax.persistence
│   │   └── ItemRepository.java
│   └── web/
│       ├── DemoController.java
│       ├── GlobalExceptionHandler.java
│       └── LoggingInterceptor.java
├── src/main/resources/
│   ├── application.properties
│   └── db/
│       ├── schema.sql, data.sql
│       └── migration/V1__init.sql
└── src/test/.../DemoControllerTest.java  # JUnit 4
```

## 构建与运行

```bash
# 编译
mvn -q compile

# 测试
mvn -q test

# 运行
mvn spring-boot:run
```

生成 Maven Wrapper 后可使用 `./mvnw`：

```bash
mvn -N wrapper:wrapper
./mvnw compile test
./mvnw spring-boot:run
```

## Docker

项目根目录提供多阶段构建的 `Dockerfile`（当前 Java 11；升级到 Boot 3 后需改为 Java 21 基础镜像）。构建与运行示例：

```bash
docker build -t spring-boot-2-to-3-demo .
docker run -p 8080:8080 spring-boot-2-to-3-demo
```

## 运行后访问

| 说明 | URL |
|------|-----|
| 公开 API | http://localhost:8080/api/public/items |
| OpenAPI 文档 | http://localhost:8080/v3/api-docs |
| Swagger UI | http://localhost:8080/swagger-ui.html |
| Actuator | http://localhost:8080/actuator（默认仅 health） |

管理端点基路径在 `application.properties` 中为 `management.contextPath=/manage`（Boot 3 中迁移为 `management.server.base-path`）。

## 迁移步骤建议

1. **确认基线**：`mvn compile test` 通过。
2. **执行迁移**：使用本仓库的 [spring-boot-2-to-3](https://github.com/chensoul/spring-boot-2-to-3)  skill 或 OpenRewrite 配方。
3. **收尾**：按 skill 中 `references/manual-fix-checklist.md` 处理剩余问题。
