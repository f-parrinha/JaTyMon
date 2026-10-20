#!/usr/bin/env bash
#
# 	run.sh
#
# Usage: ./run.sh <jar path> [options]
#
# Arguments:
#   <jar path>      jar-with-dependencies to run (required)
#
# Options:
#   --no-build      do not run Maven
#   --skip-tests    build, but pass -DskipTests to Maven (ignored with --no-build)
#   -h, --help      show this help
#
# Environment:
#  	- PORT   	(default 9000)
#  	- REPLICAS   	(default 5)
#	- CLIENTS	(default 1)
#	- BENCHMARKS	(default ./Benchmarks)
#	- MONITORS	(default on)
#	- FAULTS	(default off)

port="${PORT:-9000}"
replicas_count="${REPLICAS:-5}"
clients_count="${CLIENTS:-1}"
benchmarks_dir="${BENCHMARKS:-./Benchmarks}"
monitors="${MONITORS:-on}"
faults="${FAULTS:-off}"

jar_path=""
jar_name=""
build=1
skip_tests=0
replicas=""
clients=""
pids=()

usage() { sed -n '/^# Usage/,/^$/p' "$0" | sed 's/^# \{0,1\}//'; }

while [ $# -gt 0 ]; do
  case "$1" in
    --no-build)   build=0; shift ;;
    --skip-tests) skip_tests=1; shift ;;
    -h|--help)    usage; exit 0 ;;
    -*) echo "Unknown option: $1" >&2; usage >&2; exit 1 ;;
    *)  [ -z "$jar_path" ] || { echo "Unexpected argument: $1" >&2; exit 1; }
        jar_path="$1"; shift ;;
  esac
done

[ -n "$jar_path" ] || { echo "Error: jar path is required" >&2; usage >&2; exit 1; }

mkdir -p "$benchmarks_dir"

[ "$monitors" = "on" ] && use_monitors="true" || use_monitors="false"
[ "$faults" = "on" ] && fault_test_on="true" || fault_test_on="false"


#
# populate_replicas()
# 
# Creates the replicas string list
#

populate_replicas() {
  if [[ "$replicas_count" -eq 0 ]]; then
    replicas="EMPTY"
    return
  fi
  replicas="localhost:${port}"
  local i=1
  while [ "$i" -lt "$replicas_count" ]; do
      replicas="${replicas},localhost:$((port + i))"
      i=$((i + 1))
  done
}


#
# populate_clients()
#
# Creates the clients string list
#
populate_clients() {
  if [[ "$clients_count" -eq 0 ]]; then
    clients="EMPTY"
    return
  fi
  clients="localhost:$((port + replicas_count))"
  local i=1
  while [ "$i" -lt "$clients_count" ]; do
      clients="${clients},localhost:$((port + replicas_count + i))"
      i=$((i + 1))
  done
}

#
# start_replica()
#
# Start peer nodes with live prints to console
#
# Receives:
# 	- p: port
#

start_replica() {
  local p=$1
  java -Dport="$p" -jar "$jar_path" \
                        port="$p" \
                        replicas="$replicas" \
                        clients="$clients" \
                        use_monitors="$use_monitors" \
                        fault_test_on="$fault_test_on" &
  local pid=$!
  pids+=("$pid")
  sleep 1
  if kill -0 $pid 2>/dev/null; then
    echo "Started peer node with port $p (PID: $pid)"
  else
    echo "Failed to start peer node with port $p"
  fi
}


#
# start_client()
#
# Start client nodes with live prints to console and creates a benchmarks file
#
# Receives:
# 	- p: port
#

start_client() {
  local p=$1
  local bench_file="$benchmarks_dir/Benchmarks-client-$p-monitors-$monitors"
  echo "run: $(basename "$benchmarks_dir")" > "$bench_file"
  java -Dport="$p" -jar "$jar_path" \
                        port="$p" \
                        replicas="$replicas" \
                        clients="$clients" \
                        use_monitors="$use_monitors" \
                        fault_test_on="$fault_test_on" \
                        > >(tee >(grep --line-buffered -E "Total writes|Bad writes|Bad write percentage|Average (write|read) latency|Elapsed time" >> "$bench_file")) 2>&1 &
  
  local pid=$!
  pids+=("$pid")
  sleep 1
  if kill -0 $pid 2>/dev/null; then
    echo "Started client node with port $p (PID: $pid)"
  else
    echo "Failed to start client node with port $p"
  fi
}


#
# kill_processes()
#
# Kills all processes
#

kill_processes() {
  echo ""
  echo "Stopping all nodes..."
  for pid in "${pids[@]}"; do
    kill -9 "$pid" 2>/dev/null || true
  done
  echo "Done!"
}

# Catch Ctrl+C (SIGINT) and SIGTERM immediately
trap 'kill_processes; exit 130' SIGINT SIGTERM

#
# Main Execution
#


# Build source code (optional)
if [ "$build" -eq 1 ]; then
  mvn_args=(-B clean package)
  [ "$skip_tests" -eq 1 ] && mvn_args+=(-DskipTests)
  echo "Compiling.."
  echo
  mvn "${mvn_args[@]}" || { echo "Maven compilation failed"; exit 1; }
  echo "Compilation successful"
  echo
else
  echo "Skipping build (--no-build)"
fi

# Make sure Jar exists
[ -f "$jar_path" ] || { echo "Error: jar not found: $jar_path" >&2; exit 1; }
jar_path="$(cd "$(dirname "$jar_path")" && pwd)/$(basename "$jar_path")"
jar_name="$(basename "$jar_path")"

# Populate clients and replicas lists
populate_replicas
echo "Using replicas list: $replicas"
populate_clients
echo "Using clients list: $clients"
echo
echo "Starting benchmark."
echo "---------------------------------------------------"


# Start replicas (live prints, no log files)
for ((i=0; i<replicas_count; i++)); do
  start_replica $((port + i))
done

# Start clients (live prints + Benchmarks-<p> file generation)
client_start=$((port + replicas_count))
client_end=$((port + replicas_count + clients_count - 1))
for ((p=client_start; p<=client_end; p++)); do
  start_client "$p"
done

# Wait until all clients output their final statistics into their Benchmarks files
for ((p=client_start; p<=client_end; p++)); do
  until grep -qE "Elapsed time|Total writes" "$benchmarks_dir/Benchmarks-client-$p-monitors-$monitors" 2>/dev/null; do
    sleep 1
  done
done

echo "---------------------------------------------------"
echo "Benchmark finished. Benchmarks files are ready."

kill_processes
exit 0
