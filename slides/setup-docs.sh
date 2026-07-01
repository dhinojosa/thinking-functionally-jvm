#!/bin/zsh

set -euo pipefail

cd "$(dirname "$0")"

basename=$(basename "$PWD")
image="asciidoctor/docker-asciidoctor"
revealjsdir="https://cdn.jsdelivr.net/npm/reveal.js"

docker run --rm \
  -v "$PWD":/documents \
  "$image" \
  asciidoctor-revealjs \
    -a revealjsdir="$revealjsdir" \
    -a customcss=styles/custom.css \
    -a highlightjs-theme=styles/color-brewer.css \
    -o "$basename.html" \
    main.adoc

docker run --rm \
  -v "$PWD":/documents \
  "$image" \
  asciidoctor \
    -b html5 \
    -o lab_book.html \
    lab_book.adoc
