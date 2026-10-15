```markdown
# Spring Lab 01 — Web Application Development with Spring Framework

Учебный проект по дисциплине **RWPSF 3305** (IITU).  
Один проект `spring-lab-01` последовательно развивается через лабораторные работы.

## Технологии

- **Java 25** + **Spring Boot 4.1.x**
- Spring Web, Spring AOP, Spring MVC, Thymeleaf
- Maven Wrapper
- Git / GitHub

## Лабораторные работы

| № | Тема | Что сделано |
|---|------|-------------|
| **1** | Настройка среды и первый Spring Boot | Spring Initializr, структура проекта, REST (`/api/hello`, `/api/info`), Git |
| **2** | IoC-контейнер | Beans (`@Component`, `@Bean`), constructor injection, `@Qualifier` / `@Primary` / `@Fallback`, scopes, lifecycle (`@PostConstruct`) |
| **3** | Конфигурация | YAML + профили (`dev`/`prod`), `@ConfigurationProperties`, Actuator |
| **4** | Spring AOP | Аспекты (логирование, timing, audit), `@Order`, proxy / self-invocation |
| **5** | Spring MVC | REST + MVC контроллеры, модель → Thymeleaf, статика, Post/Redirect/Get, статусы HTTP |

## Запуск

```bash
./mvnw spring-boot:run
# или с профилем
./mvnw spring-boot:run -Dspring-boot.run.arguments=--spring.profiles.active=dev
```

## Структура веток

- `main` — стабильная версия
- `lab01` … `lab05` — ветки с выполненными лабораторными

---
Проект постепенно развивается.
```