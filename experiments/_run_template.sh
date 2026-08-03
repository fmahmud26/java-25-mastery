#!/usr/bin/env bash
# Shared snippet pattern — each experiment has its own run.sh calling javac/java.
set -euo pipefail
ROOT="$(cd "$(dirname "$0")" && pwd)"
cd "$ROOT"
rm -rf out && mkdir -p out
javac --release 25 -d out $(find src -name '*.java')
exec java -cp out "$@"
