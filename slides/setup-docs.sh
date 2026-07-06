#!/bin/zsh

set -euo pipefail

cd "$(dirname "$0")"

basename=$(basename "$PWD")
repo_root=$(dirname "$PWD")
image="asciidoctor/docker-asciidoctor"
revealjsdir="https://cdn.jsdelivr.net/npm/reveal.js"
run_decktape=false
pdf_name="thinking_functionally_jvm.pdf"

for arg in "$@"; do
  case "$arg" in
    --decktape) run_decktape=true ;;
    *) echo "Unknown option: $arg"; exit 1 ;;
  esac
done

mkdir -p "$repo_root/lab_book/images"
cp images/stop.png "$repo_root/lab_book/images/"

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
  -v "$repo_root":/documents \
  "$image" \
  asciidoctor \
    -b html5 \
    -o lab_book/index.html \
    slides/lab_book.adoc

if $run_decktape; then
  docker run --rm \
    -v "$repo_root":/workspace \
    astefanutti/decktape \
    --size '1920x1080' \
    --pause 1000 \
    reveal \
    "file:///workspace/slides/${basename}.html" \
    "/workspace/${pdf_name}"
fi
