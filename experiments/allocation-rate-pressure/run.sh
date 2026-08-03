#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"
rm -rf out && mkdir out
javac --release 25 -d out $(find src -name '*.java')
# GC logging for observation — still not a formal benchmark suite
java -Xms128m -Xmx128m \
  -Xlog:gc*:file=gc.log:time,uptime,level,tags \
  -cp out exp.AllocationRatePressure "$@"
echo "--- gc.log (tail) ---"
tail -n 40 gc.log || true
