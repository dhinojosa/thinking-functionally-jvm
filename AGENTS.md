# AGENTS.md

Guidance for AI agents working on this repository.

This repo is a technical workshop built from Asciidoctor/reveal.js slides and
lab material. Read the relevant reference files before making content changes:

- `references/slide_style.md` for general slide voice, structure, aesthetics,
  Asciidoctor formatting, and validation.
- `references/scala_slide_style.md` for Scala-specific slides, typeclasses,
  higher-kinded types, effects, programs, and interpreters.
- `references/lab_style.md` for lab-book and exercise style.

## Core Rules

- If the user says "no edits" or "review only", do not modify files.
- Read the target file before editing; the user often makes manual edits quickly.
- Preserve the owner's presentation voice: pragmatic, direct, and technically
  careful.
- Push back when an example would teach the wrong design habit.
- After slide edits, render when practical:

```bash
cd slides
./setup-docs.sh
```

The normal render validates Asciidoctor structure and generated HTML. It does
not guarantee visual overflow is acceptable; inspect HTML/PDF visually for dense
slides.
