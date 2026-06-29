# Thinking Functionally in Java and the JVM

Java has quietly absorbed functional ideas over the last decade. Lambdas, streams, records, sealed types. It has been an amazing journey, but most teams still write code as if none of that really changed anything. This workshop asks a simple question: what if we actually took those features seriously?

In Thinking Functionally in Java, we explore how far disciplined functional design can take us using plain Java with no rewrites, no new language mandates, and no academic detours. Along the way, we address reproducible development environments with Nix and replace exception-driven control flow with explicit error modeling. We uncover why concepts like flatMap, algebraic data types, and composability matter even if you never say the word “monad” out loud.

* Reproducible Java Environments with Nix – Defining a deterministic Java toolchain so “works on my machine” stops being a variable.
* Functional Java: What We Actually Have – Referential transparency, immutability, and total functions using modern Java features, 
* Type-Driven Design: Separating data from behavior (`Show`, `Eq`) and understanding the limits of Java’s type system.
* Composition with `flatMap`: Sequencing computations across Optional, streams, and error-handling types.
* Understanding Monads: Context, short-circuiting, composition, and trade-offs without mysticism.
* Algebraic Data Types in Java: Modeling closed domains with sealed types instead of flags and conditionals.
* Errors as Data: Replacing exception-driven control flow with explicit result types (`Either`, `Try`).
* Putting It Together: Designing a small, readable service that isolates side effects and composes cleanly.
* Breaking the Java Wall: A look at higher-kinded types, effects, and what Java still can’t express in languages other than Java

## Agenda

1. Reproducible Development Environments
    * Nix
    * Docker / Dev Containers
    * SDKMan
2. Functional Java
    * Function
    * Predicate
    * Supplier
    * Consumer
3. Functional Design Principles
    * Referential Transparency
    * Single Responsibility Principle
    * Separate Side Effects
    * Command Query Separation
    * Small Functions, Large Programs
4. Immutable Data
    * Records
    * Value Objects and Project Valhalla
5. Streams and Laziness
    * Delayed Computation
    * Lazy vs. Eager Evaluation
    * Pipelines
6. Function Composition
    * `andThen`
7. Algebraic Data Types
    * Commands
    * Results
    * Errors
    * Collections
    * Language and the Interpreter Pattern
8. Errors as Data
    * `Optional` / `Option`
    * `Result`
    * `Either`
    * `Try`
    * `Validated`
    * Compare to Exceptions
    * Returning Composable Values
       * `Optional`
       * `Validated`
       * `CompletableFuture`
9. Functional Behaviors
    * `map`
    * `flatMap`
        * Monadic Pattern (`flatMap`, `flatMap`, `flatMap`, `map`)
        * In-Channel Errors
        * With `Optional`, with `CompletableFuture`
    * `filter`
    * `reduce`
10. Java Thoughts and Patterns
    * Object behavior
      * `equals`
      * `hashCode`
      * `Comparable` or `Comparator`
    * Composition
      * `StructuredTaskScope`
      * `Vavr` validation
    * Functional Operations
      * `flatMap`
      * `map`
      * `reduce`
11. About Scala
12. Typeclasses
    * `Show`
    * `Eq`
    * `Order`
    * `Comparator`
    * Java's Plan to include Typeclasses
    * Separating behavior from data, data-oriented programming
13. Functional Abstraction Typeclasses
    * `Monoid`
    * `Functor`
    * `Applicative`
    * `Monad`
      * For-Comprehensions
      * In-Channel Errors
    * `Traverse`
14. Higher-Kinded Types
    * `F[A]`
    * Reusable Programs
15. Effectful Programs
    * `Future`
    * `IO`
    * `F[_]`
    * Clocks and Time
    * Programs vs. Values
16. Programs and Interpreters
    * Data
    * Algebra
    * Programs
    * Interpreters
    * Why Effects Exist
    * A note on `@Transactional`
17. Beyond Java
    * Scala
    * Cats Effect
    * Arrow (Cat Style to its own style)
    * The “Direct Style” vs. “Functional Style” discussion
