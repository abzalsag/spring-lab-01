# Lab 12 — Evidence

## 1. `./mvnw clean verify`
```
Tests run: __, Failures: 0, Errors: 0, Skipped: 0
```

## 2. Coverage (package: kz.iitu.springlab.catalog)
| | Line | Branch |
|---|---|---|
| Before extra test | __ % | __ % |
| After extra test  | __ % | __ % |

Yellow line: `BookService.findAll` — `author == null || author.isBlank()`
Extra test: `nullAuthorReturnsEveryBook`

## 3. Still uncovered branch
(2 sentences: test it or delete it, and why.)

## 4. Security in the slice test
I used `@WithMockUser` (+ `csrf()` for POST) because ...

## 5. If Docker was unavailable
Command: ...
Error: ...
What the container test would prove that @DataJpaTest does not: ...