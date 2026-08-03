#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "$0")" && pwd)"
cd "$ROOT"
rm -rf out
mkdir -p out
javac --release 25 -d out $(find src -name '*.java')
java -cp out shortener.ShortenerDemo
