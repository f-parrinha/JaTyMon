#!/usr/bin/env bash
#
# 		run-orchestrator.sh
#
# Runs the benchmarks of the use cases through each use case's run.sh,
# passing it the jar built by build-usecases.sh.
#
# Requires:
# 		- common.sh
#
# Usage: run-orchestrator.sh <ABD|MultiPaxos|both> <number > 0> <number > 0> <on|off>
#
# Environment:
#   REPLICAS    number of replicas   (default: 5)
#   CLIENTS     number of clients    (default: 1)
#   MONITORS    on|off               (default: on)
#   FAULTS      on|off               (default: off)
#
# Author: Francisco Parrinha
#

set -euo pipefail
source "$(dirname "${BASH_SOURCE[0]}")/common.sh"

if [ "$#" -lt 1 ]; then
echo "Usage: $0 <ABD|MultiPaxos|both>" >&2
    exit 1
fi

UC_ARG="$1"
RUNS="${2:-10}"
PAUSE_BETWEEN_RUNS="${3:-2}"
PRINT="${4:-off}"
MONITORS="${MONITORS:-on}"
FAULTS="${FAULTS:-off}"
REPLICAS="${REPLICAS:-5}"
CLIENTS="${CLIENTS:-1}"

case "$UC_ARG" in
  both|all) SELECTED=("${USE_CASES[@]}") ;;
  *)
    SELECTED=()
    for uc in "${USE_CASES[@]}"; do [ "$uc" = "$UC_ARG" ] && SELECTED=("$uc"); done
    [ "${#SELECTED[@]}" -gt 0 ] || { echo "Unknown use case: $UC_ARG (expected ${USE_CASES[*]} or both)" >&2; exit 1; }
    ;;
esac
case "$MONITORS" in on|off) ;; *) echo "Monitors must be 'on' or 'off'" >&2; exit 1 ;; esac
case "$FAULTS" in on|off) ;; *) echo "Faults must be 'on' or 'off'" >&2; exit 1 ;; esac

# Prints the path of the use case jar; fails unless exactly one exists
find_jar() {
  local dir="$USE_CASES_DIR/$1"
  local jars=()
  while IFS= read -r j; do jars+=("$j"); done < <(find "$dir/target" -maxdepth 1 -name '*-jar-with-dependencies.jar' 2>/dev/null)
  if [ "${#jars[@]}" -ne 1 ]; then
    echo "[run] ERROR: expected 1 jar-with-dependencies in $dir/target, found ${#jars[@]}. Run build-usecases.sh" >&2
    return 1
  fi
  echo "${jars[0]}"
}

run_one() {
  local uc="$1"
  local mon="$2"
  local run_id=$3
  local dir="$USE_CASES_DIR/$uc"
  local out="$BENCHMARKS/$run_id"
  local run_sh="$SCRIPTS_DIR/run.sh"
  local jar

  [ -f "$run_sh" ] || { echo "[run] ERROR: $run_sh not found" >&2; return 1; }
  jar="$(find_jar "$uc")" || return 1
  mkdir -p "$out"

  echo "[run] Starting $uc (run_id: $run_id, jar: $(basename "$jar"), replicas=$REPLICAS, clients=$CLIENTS, monitors=$mon, faults=$FAULTS)"

  (
    cd "$dir"
    REPLICAS="$REPLICAS" \
    CLIENTS="$CLIENTS" \
    MONITORS="$mon" \
    FAULTS="$FAULTS" \
    BENCHMARKS="$out" \
    PRINT="$PRINT" \
    bash "$run_sh" "$jar" --no-build
  )
}

bash "$ROOT/Scripts/clear-benchmarks.sh"

# Run each selected use case in sequence; keep going if one fails
failed=0
for idx in "${!SELECTED[@]}"; do
  for ((i=0; i<RUNS; i++)); do
    if [ "$idx" -gt 0 ]; then sleep "$PAUSE_BETWEEN_RUNS"; fi    # let ports be released
    
    # 1. Run with monitors ON
    status=0
    uc="${SELECTED[$idx]}"
    run_id="$uc-$(date +%Y%m%dT%H%M%S)"
    run_one "$uc" on "$run_id" || status=$?
    if [ "$status" -ne 0 ]; then
      echo "[run] ${SELECTED[$idx]} with monitors (on) failed (exit $status)" >&2
      failed=1
      break
    fi
    
    echo "Waiting for ports to be closed (1s)"
    sleep 1
    
    # 2. Run with monitors OFF
    status=0
    run_one "${SELECTED[$idx]}" off "$run_id" || status=$?
    if [ "$status" -ne 0 ]; then
      echo "[run] ${SELECTED[$idx]} without monitors (off) failed (exit $status)" >&2
      failed=2
      break
    fi 
  done
done

exit "$failed"
