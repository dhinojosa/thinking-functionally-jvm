# Slide Style

Use this for any technical workshop slide deck, regardless of language.

## Project Style

This is a technical workshop, not a marketing deck. Slides should be minimal,
direct, information rich, and instructor friendly. Prefer small claims backed by
nearby code examples over long explanatory prose.

The audience is assumed to be experienced developers. Do not over-explain basic
syntax, but do not introduce a new abstraction without a concrete example.

## Slide Voice

- Use concise bullets.
- Prefer precise technical language over motivational language.
- Avoid "AI-sounding" filler such as vague meta-commentary.
- Avoid comments inside code snippets when a slide bullet can explain the point.
- Use examples that teach the shape of the idea, not just a library trick.
- If a term is new, define it before relying on it in later slides.
- Avoid forward references unless the slide explicitly says a later chapter will
  name or deepen the concept.

## Slide Structure

- When a code block directly demonstrates a bullet point, chain it to that bullet
  with `+`.
- When a slide has one standalone code block at the bottom that demonstrates the
  whole slide, leave it as its own block.
- Put admonitions such as `NOTE:` and `WARNING:` near the bottom of a slide when
  possible. They should read like important footnotes, not interrupt the main
  teaching flow.
- Use `NOTE:` for short one-line notes. Avoid large admonition blocks unless the
  content is genuinely multi-paragraph.
- Use bold labels for before/after or paired comparisons, for example
  `* *Before:* ...` and `* *After:* ...`.
- Demo and lab signifier slides are allowed, but do not create lab material
  unless explicitly asked.

## Code Examples

- Keep examples short, but real enough to teach.
- Show the full form before shorthand when the shorthand hides the lesson.
- Prefer before/after examples when teaching refactoring or abstraction.
- Keep code close to the bullet it supports.
- Use compact examples with strong naming.
- When showing experimental features, clearly mark them as experimental and not
  available today.

## Aesthetic Preferences

- Slides should feel clean and deliberate, not crowded.
- Prefer tables only when they clarify contrast.
- Avoid decorative text. Every bullet should earn its place.
- If a slide becomes dense, split it into multiple slides instead of shrinking
  the idea.

## Asciidoctor / Reveal.js

- Keep Asciidoctor syntax simple and predictable.
- Use `[source,java]`, `[source,scala]`, `[source,kotlin]`, etc. for code blocks.
- Use `+` continuation carefully so related blocks stay attached to bullets.
- For two-column demo slides, follow the existing project convention with
  `[.columns]`, `[.column]`, and the lab/demo image.

## Validation

After slide edits, run the project render command when available. In this repo:

```bash
cd slides
./setup-docs.sh
```

Use `./setup-docs.sh --decktape` only when a PDF render is explicitly requested
or visual PDF output must be validated.

The normal render validates Asciidoctor structure, includes, and generated HTML.
It does not guarantee visual overflow is acceptable. For dense slides, inspect
the rendered HTML or PDF visually.
