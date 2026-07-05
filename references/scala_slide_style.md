# Scala Slide Style

Use this only for projects or chapters that teach Scala, Scala-adjacent
functional programming, Cats, Cats Effect, or JVM functional abstractions.

## Scala Syntax

- If using Scala typeclass syntax, explain `given`, `using`, `summon`, and
  context bounds before relying on them.
- Context bound syntax such as `F[_]: Monad` should be named as a context bound.
- Show the full form before shorthand when the shorthand hides the lesson.
- Avoid forward references. For example, do not use `MonadThrow` before the
  audience has seen either in-channel errors or typeclass constraints.

## Typeclasses

- Define typeclasses as capabilities supplied from outside the data type.
- Show explicit instances before lambda shorthand.
- Java analogies such as `Comparator<T>` are useful: Java had the right
  abstraction idea, even if Scala generalizes it.
- Do not use application ports or services as typeclasses. Inject them normally.
  Reserve contextual constraints for real capabilities such as `MonadThrow`.
- When showing experimental Java typeclass features such as witnesses, clearly
  mark them as experimental and not Java today.

## Functional Abstractions

- Tie abstractions back to earlier Java-friendly ideas:
  - `map` points toward `Functor`.
  - `flatMap` points toward `Monad`.
  - independent validation points toward `Applicative`.
- Distinguish `Foldable` from `Traverse`:
  - `Foldable` summarizes a structure into a value.
  - `Traverse` walks a structure while sequencing an effect.
- Do not make `Traverse` sound like another name for `reduce`.

## Higher-Kinded Types

- Build up from concrete shapes such as `Future[User]`, `Option[User]`, and
  `Either[Throwable, User]`.
- Then show one generic program over `F[_]`.
- If a Scala 3 type lambda is needed, use:

```scala
type MyEither = [A] =>> Either[Throwable, A]
```

## Effects

- Explain toy `IO` as a teaching model, not Cats Effect internals.
- A toy `IO` should include at least `Pure`, `Delay`, and `FlatMap` if it is used
  to explain composition.
- Use `Delay` to connect to earlier laziness examples when appropriate.
- Introduce `MonadThrow` as a monad with in-channel error capability.
- Show how to use `MonadThrow`, then use it as a constraint in a larger program.
- For time, show direct time reads as side effects, then show injectable or
  effectful alternatives such as Cats Effect `Clock`.

## Programs and Interpreters

- Separate the roles:
  - Data: domain values, commands, events, and errors.
  - Algebra: capabilities, ports, and business vocabulary.
  - Programs: composition of capabilities into a pipeline.
  - Interpreters: the runtime implementation that actually runs the program.
- Treat application ports such as repositories, orders, or payments as injected
  dependencies, not typeclass constraints.
- A program can be explained as analogous to a work order: it describes what
  should happen before something runs it.
- Use an edge-of-world example such as `unsafeRunAsync` or `unsafeRunSync` to
  show when a described program is actually executed.
