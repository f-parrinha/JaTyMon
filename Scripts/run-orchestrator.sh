#!/usr/bin/env bash
#
# 		run-orchestrator.sh
#
# Runs the benchmarks of the use cases through each use case's run.sh,
# passing it the jar built by build-usecases.sh.
#
# Usage: run-orchestrator.sh <use_case:ABD|MultiPaxos|both> <runs:int> <client_ops:int>
# Requires:
# 		- common.sh
#
# Environment:
# 			- REPLICAS    					number of replicas   													(default: 5)
#   		- CLIENTS    					number of clients    													(default: 1)
#   		- MONITORS    					on|off|both           													(default: on)
#   		- FAULTS      					List:ConnectionDrop|MessageDrop|MessageTinker		(default: "". For example, FAULTS=MessageDrop,MessageTinker)
#   		- PAUSE_BETWEEN_RUNS  	seconds               													(default: 1)
#   		- PRINT      					on|off               													(default: off)
#
# Author: Francisco Parrinha
#

set -euo pipefail
source "$(dirname "${BASH_SOURCE[0]}")/common.sh"

if [ "$#" -lt 1 ]; then
echo "Usage: $0 <ABD|MultiPaxos|both> <runs:int> <client_ops:int>" >&2
    exit 1
fi

if [ "$#" -gt 3 ]; then
echo "Usage: $0 <ABD|MultiPaxos|both> <runs:int> <client_ops:int>" >&2
    exit 1
fi

UC_ARG="$1"
RUNS="${2:-10}"
CLIENT_OPS="${3:-10000}"

REPLICAS="${REPLICAS:-5}"
CLIENTS="${CLIENTS:-1}"
MONITORS="${MONITORS:-both}"
FAULTS="${FAULTS:-''}"
PAUSE_BETWEEN_RUNS="${PAUSE_BETWEEN_RUNS:-1}"
PRINT="${PRINT:-off}"

# Validate arguments
case "$UC_ARG" in
  both|all) SELECTED=("${USE_CASES[@]}") ;;
  *)
    SELECTED=()
    for uc in "${USE_CASES[@]}"; do [ "$uc" = "$UC_ARG" ] && SELECTED=("$uc"); done
    [ "${#SELECTED[@]}" -gt 0 ] || { echo "Unknown use case: $UC_ARG (expected ${USE_CASES[*]} or both)" >&2; exit 1; }
    ;;
esac
case "$MONITORS" in on|off|both) ;; *) echo "Monitors must be 'on', 'off' or 'both'" >&2; exit 1 ;; esac


# ----------------- METHODS ----------------- #


#
# Prints the path of the use case jar; fails unless exactly one exists
# 		$1: Use case folder
#
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

#
# Executes a single use case
#		$1: Use case folder name (e.g., ABD, MultiPaxoss)
#		$2: Monitor state (i.e., "on" or "off")
#		$3: Identification of this new run
#
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
    CLIENT_OPS="$CLIENT_OPS" \
    bash "$run_sh" "$jar" --no-build
  )
}


# ----------------- Main Execution ----------------- #


# Clear previous benchmarks
bash "$ROOT/Scripts/clear-benchmarks.sh"


# Run each selected use case in sequence; keep going if one fails
failed=0
for idx in "${!SELECTED[@]}"; do
  for ((i=0; i<RUNS; i++)); do
    if [ "$idx" -gt 0 ]; then sleep "$PAUSE_BETWEEN_RUNS"; fi    # let ports be released
    
    uc="${SELECTED[$idx]}"
    run_id="$uc-$i"
	
		# Setup monitor modes
		if [ "$MONITORS" = "both" ]; then
				MONITOR_MODES=(on off)
		else
				MONITOR_MODES=("$MONITORS")
		fi
	
		# Test each monitor mode
		for monitor_mode in "${MONITOR_MODES[@]}"; do
				status=0
				run_one "$uc" "$monitor_mode" "$run_id" || status=$?

				if [ "$status" -ne 0 ]; then
				    echo
				    echo "[run] ${SELECTED[$idx]} with monitors ($monitor_mode) failed (exit $status)" >&2
				    failed=1
				    break
				fi
		done
    
    # Skip RUNS if failed is no longer 0
    [ "$failed" -eq 0 ] || break
  done
done

exit "$failed"
