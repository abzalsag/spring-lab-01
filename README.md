<div align="center">

# 🌱 Spring Lab 01

### Web Application Development with Spring Framework

Учебный проект по дисциплине **RWPSF 3305** · IITU

![Java](https://img.shields.io/badge/Java-25-orange?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.x-6DB33F?logo=springboot&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-Wrapper-C71A36?logo=apachemaven&logoColor=white)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-MVC-005F0F?logo=thymeleaf&logoColor=white)
![Git](https://img.shields.io/badge/Git-GitHub-181717?logo=github&logoColor=white)
![Status](https://img.shields.io/badge/status-in%20development-yellow)
![Labs](https://img.shields.io/badge/labs-5%20done-brightgreen)

</div>

---

## 📖 О проекте

Один проект `spring-lab-01`, который **постепенно развивается** от простого
«Hello World» до полноценного Spring MVC-приложения: с каждой лабораторной
работой добавляется новая тема фреймворка. Код предыдущих работ не удаляется,
а дополняется.

> 🚧 **Проект в активной разработке.** README обновляется по мере выполнения
> новых лабораторных работ.

## 🛠 Технологии

| Категория | Стек |
|-----------|------|
| **Язык** | Java 25 |
| **Фреймворк** | Spring Boot 4.1.x |
| **Модули** | Spring Web, Spring AOP, Spring MVC, Actuator |
| **Шаблоны** | Thymeleaf |
| **Сборка** | Maven Wrapper |
| **VCS** | Git / GitHub |

## 🧪 Лабораторные работы

| № | Тема | Что сделано | Статус |
|:-:|------|-------------|:------:|
| **1** | ⚙️ Настройка среды и первый Spring Boot | Spring Initializr, структура проекта, REST (`/api/hello`, `/api/info`), Git | ✅ |
| **2** | 🧩 IoC-контейнер | Beans (`@Component`, `@Bean`), constructor injection, `@Qualifier` / `@Primary` / `@Fallback`, scopes, lifecycle (`@PostConstruct`) | ✅ |
| **3** | 🔧 Конфигурация | YAML + профили (`dev` / `prod`), `@ConfigurationProperties`, Actuator | ✅ |
| **4** | 🎯 Spring AOP | Аспекты (логирование, timing, audit), `@Order`, proxy / self-invocation | ✅ |
| **5** | 🌐 Spring MVC | REST + MVC контроллеры, модель → Thymeleaf, статика, Post/Redirect/Get, статусы HTTP | ✅ |
| **6** | ⏳ _Скоро_ | В разработке | 🕐 |



## 🚀 Запуск

**Требования:** установленная JDK 25. Maven ставить не нужно, используется Maven Wrapper.

```bash
# Обычный запуск
./mvnw spring-boot:run

# Запуск с профилем dev
./mvnw spring-boot:run -Dspring-boot.run.arguments=--spring.profiles.active=dev

# Запуск с профилем prod
./mvnw spring-boot:run -Dspring-boot.run.arguments=--spring.profiles.active=prod
```

После старта приложение доступно на http://localhost:8080

## 🔗 Основные эндпоинты

| Метод | URL | Описание |
|:-----:|-----|----------|
| `GET` | `/api/hello` | Приветствие (ЛР 1) |
| `GET` | `/api/info` | Информация о приложении (ЛР 1) |
| `GET` | `/actuator/health` | Состояние приложения (ЛР 3) |

## 📁 Структура проекта

```text
spring-lab-01
├── src
│   ├── main
│   │   ├── java            # Контроллеры, сервисы, аспекты, конфигурации
│   │   └── resources
│   │       ├── templates   # Thymeleaf-шаблоны
│   │       ├── static      # CSS, JS, изображения
│   │       └── application.yml
│   └── test
├── mvnw
└── pom.xml
```

## 👤 Автор

**Сагындык Абзал** · группа IT-2410 

---

<div align="center">
<sub>IITU · RWPSF 3305 · 2026</sub>
</div>