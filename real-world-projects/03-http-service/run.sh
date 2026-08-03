#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "$0")" && pwd)"
cd "$ROOT"
mkdir -p out
# shellcheck disable=SC2046
javac --release 25 -d out $(find src -name '*.java' | sort)
java -cp out http.HttpServiceDemo "$@"
