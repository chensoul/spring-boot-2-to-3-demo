# Spring Boot 2 → 3 Migration Demo Project

A Maven sample for practicing **Spring Boot 2 → 3** upgrade, covering the main migration scenarios listed in the [spring-boot-2-to-3](https://github.com/chensoul/spring-boot-2-to-3)  skill.

- **Spring Boot**: 2.7.18  
- **Java**: 11  
- **Build**: Maven  

[中文](README.zh-CN.md)

## Scenario overview

| Scenario | Location |
|----------|----------|
| **javax → jakarta** | `Item.java` (javax.persistence), `LoggingInterceptor.java` (javax.servlet), `DemoController.java` (javax.validation) |
| **@Autowired single constructor** | `DemoController` constructor |
| **@RequestMapping(method=)** / **@PathVariable("id")** | `DemoController` |
| **HttpStatus** / **APPLICATION_JSON_UTF8_VALUE** | `DemoController`, `GlobalExceptionHandler` |
| **WebSecurityConfigurerAdapter** | `SecurityConfig` |
| **WebMvcConfigurerAdapter** | Recipe migrates subclasses; this sample already uses `WebMvcConfigurer` for compile |
| **HandlerInterceptorAdapter** | Recipe migrates subclasses; this sample already uses `HandlerInterceptor` |
| **AsyncConfigurerSupport** | `AsyncConfig` |
| **JUnit 4** | `DemoControllerTest` (@RunWith, @Before, public @Test) |
| **Legacy config properties** | `application.properties`: spring.datasource.schema/data/initialization-mode, management.contextPath |
| **API docs** | springdoc-openapi 1.8.0 (Boot 2.7); Boot 3 → springdoc 2.x; `springdoc-openapi-ui` in pom |
| **JPA / Hibernate** | `Item` entity, `ItemRepository` |
| **Flyway** | flyway-core in pom, `db/migration/V1__init.sql` |

## Project layout

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

## Build and run

```bash
# Compile
mvn -q compile

# Test
mvn -q test

# Run
mvn spring-boot:run
```

With Maven Wrapper:

```bash
mvn -N wrapper:wrapper
./mvnw compile test
./mvnw spring-boot:run
```

## Docker

The repo includes a multi-stage `Dockerfile` (Java 11; switch to a Java 21 base image after upgrading to Boot 3). Example:

```bash
docker build -t spring-boot-2-to-3-demo .
docker run -p 8080:8080 spring-boot-2-to-3-demo
```

## After startup

| Description | URL |
|-------------|-----|
| Public API | http://localhost:8080/api/public/items |
| OpenAPI spec | http://localhost:8080/v3/api-docs |
| Swagger UI | http://localhost:8080/swagger-ui.html |
| Actuator | http://localhost:8080/actuator (health only by default) |

Management base path is set in `application.properties` as `management.contextPath=/manage` (Boot 3: `management.server.base-path`).

## Migration steps

1. **Baseline**: Ensure `mvn compile test` passes.
2. **Migrate**: Use the [spring-boot-2-to-3](https://github.com/chensoul/spring-boot-2-to-3)  skill or OpenRewrite recipes.
3. **Cleanup**: Follow `references/manual-fix-checklist.md` in the skill for any remaining issues.
