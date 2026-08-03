#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"
rm -rf out && mkdir out
javac --release 25 -d out $(find src -name '*.java')
java -cp out exp.StreamLazyShortcircuit "$@"
