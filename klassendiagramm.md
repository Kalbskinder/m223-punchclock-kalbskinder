# Klassendiagramm

```mermaid
classDiagram
    direction LR

    class Entry {
        -Long id
        -LocalDateTime checkIn
        -LocalDateTime checkOut
        -Category category
        -Set~Tag~ tags
    }

    class Category {
        -Long id
        -String title
    }

    class Tag {
        -Long id
        -String title
    }

    Entry "0..*" --> "0..1" Category : category
    Entry "0..*" --> "0..*" Tag : tags
```

## Beziehungen

| Beziehung | Multiplizität | JPA | Bedeutung |
|---|---|---|---|
| Entry → Category | `0..*` zu `0..1` | `@ManyToOne` | Ein Entry hat höchstens eine Kategorie (oder keine). Eine Kategorie kann von beliebig vielen Entries verwendet werden. |
| Entry → Tag | `0..*` zu `0..*` | `@ManyToMany` (Zwischentabelle `entry_tag`) | Ein Entry kann beliebig viele Tags haben, ein Tag kann an beliebig vielen Entries hängen. |

Die Pfeile zeigen die Navigationsrichtung: Nur `Entry` kennt seine Kategorie und Tags. `Category` und `Tag` haben keine Liste ihrer Entries (unidirektionale Beziehungen).
