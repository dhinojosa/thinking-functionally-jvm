# Lab Style

Use this for lab-book pages, exercises, and demo/lab signifier slides.

## Lab Philosophy

- Labs should reinforce the slide sequence, not introduce unrelated concepts.
- Keep instructions direct and task oriented.
- Prefer small, complete exercises over large open-ended assignments.
- Avoid creating lab material unless the user explicitly asks for it.
- Demo and lab signifier slides are fine without supporting handout material.

## Lab Book Writing

- State the goal of the lab plainly.
- Give enough setup context for the learner to start without hunting.
- Keep steps ordered and executable.
- Prefer concrete file paths, package names, and commands.
- Avoid long explanations inside steps; use short notes before or after a step.

## Code and Tests

- Labs should have a visible feedback loop, preferably a test to run.
- Use existing project conventions, packages, and build tools.
- Do not add unrelated abstractions just to make the lab feel larger.
- If a lab is about refactoring, preserve behavior first and then improve shape.

## Asciidoctor Formatting

- Use code blocks for commands and snippets.
- Use `NOTE:` for short footnotes or reminders.
- Keep admonitions near the bottom of a section when possible.
- If a code block directly supports a bullet, chain it with `+`.

## Instructor Fit

- Labs should be teachable live. Avoid cleverness that requires lengthy
  explanation.
- Make expected outcomes explicit enough that attendees know when they are done.
- Keep optional extensions clearly separate from required work.
