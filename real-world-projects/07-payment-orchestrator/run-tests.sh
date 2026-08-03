#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "$0")" && pwd)"
cd "$ROOT"
# Prefer JDK 25 if present but not on PATH
if [[ -d /usr/lib/jvm/java-25-openjdk-amd64 ]]; then
  export JAVA_HOME=/usr/lib/jvm/java-25-openjdk-amd64
  export PATH="$JAVA_HOME/bin:$PATH"
fi
rm -rf out && mkdir -p out
javac --release 25 -d out $(find src -name '*.java')
java -cp out payment.PaymentOrchestratorTests
