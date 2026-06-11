# Thinking Functionally in Java and the JVM

Java has quietly absorbed functional ideas over the last decade. Lambdas, streams, records, sealed types. It has been an amazing journey, but most teams still write code as if none of that really changed anything. This workshop asks a simple question: what if we actually took those features seriously?

In Thinking Functionally in Java, we explore how far disciplined functional design can take us using plain Java with no rewrites, no new language mandates, and no academic detours. Along the way, we address reproducible development environments with Nix, replace exception-driven control flow with explicit error modeling, and uncover why concepts like flatMap, algebraic data types, and composability matter even if you never say the word “monad” out loud.

* Reproducible Java Environments with Nix – Defining a deterministic Java toolchain so “works on my machine” stops being a variable.
* Functional Java: What We Actually Have – Referential transparency, immutability, and total functions using modern Java features.
* Type-Driven Design: Separating data from behavior (Show, Eq) and understanding the limits of Java’s type system.
* Composition with flatMap: Sequencing computations across Optional, streams, and error-handling types.
* Understanding Monads: Context, short-circuiting, composition, and trade-offs without mysticism.
* Algebraic Data Types in Java: Modeling closed domains with sealed types instead of flags and conditionals.
* Errors as Data: Replacing exception-driven control flow with explicit result types (Either, Try).
* Putting It Together: Designing a small, readable service that isolates side effects and composes cleanly.
* Breaking the Java Wall: A look at higher-kinded types, effects, and what Java still can’t express in languages other than Java


## Syllabus

1. Consistent Builds with Nix, Docker/DevContainers, or SDKMan
2. Java Functions

   a. `Function`
   
   b. `Predicate`
   
   c. `Supplier`
   
   d. `Consumer`
   
4. Immutability with Records   
5. Streams, Laziness, and Delayed Computation
6. Composition with `andThen` or `compose`   
7. Mapping, FlatMapping, Filtering, and Reducing - Common Functional Behaviors

   a. `Optional`

   b. `Stream`

   c. `CompletableFuture`
   
9. Algebraic Data Types with `sealed`

    a. Describing Commands

    b. Describing Results

    c. Describing Collections

    d. Describing Errors
   
11. Errors as Data

    a. `Optional`/`Option`
   
    b. `Result`
   
    c. `Either`
   
    d. `Validated`
   
12. Typeclasses
    
    a. `Show`
    
    b. `Eq`
    
    c. `Order`

    d. `Comparator`
    
    e. What does this have to do with Java?

    https://youtu.be/Gz7Or9C0TpM?si=91poKYRiPh6l2YW9&t=1259
    
14. Higher-Kinded Types

    a. `F[A]` 
    
    b. Reusable Programs
    
12. Monoid, Applicatives, Functors, Monads, Traverse, and More    
13. Data(Components + Programs) + Interpreter = Entire System
14. The identity crisis: "Direct-Style" vs. "Functional-Style."
15. Breaking down the Java wall: A look at Effect Systems on the JVM
