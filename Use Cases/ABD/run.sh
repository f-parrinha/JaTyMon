#!/bin/bash

#########################################################
# CONFIGURATION IS REQUIRED

jar_name="ABDQuorum-1.0-SNAPSHOT-jar-with-dependencies.jar"
port=9000
peers_count=5
clients_count=1

#########################################################

jar_path="target/$jar_name"
peers=""
clients=""


# Populates the peers list with their correct info (ip address and port)
populate_peers() {
  if [[ "$peers_count" -eq 0 ]]; then
    peers="EMPTY"
    return
  fi

  peers="localhost:${port}"
  local i=1
  while [ "$i" -lt "$peers_count" ]; do
      peers="${peers},localhost:$((port + i))"
      i=$((i + 1))
  done
}
populate_clients() {
  if [[ "$clients_count" -eq 0 ]]; then
    clients="EMPTY"
    return
  fi

  clients="localhost:$((port + peers_count))"
  local i=1
  while [ "$i" -lt "$clients_count" ]; do
      clients="${clients},localhost:$((port + peers_count + i))"
      i=$((i + 1))
  done
}

# Starts and verifies the execution of one node
start_process() {
  local port=$1
  if [[ ! -f "$jar_path" ]]; then
    echo "Error: JAR file '$jar_path' not found!"
    return 1
  fi

  # Start replica
  java -Dport="$port" -jar $jar_path port="$port" peers="$peers" clients="$clients" &
  local pid=$!
  sleep 1

  # Validate if it started
  if kill -0 $pid 2>/dev/null; then
    echo "Started node with port $port (PID: $pid)"
    echo
  else
    echo "Failed to start node with port $port"
    echo
  fi
}


# Kills all pending node processes
kill_nodes() {
  for pid in $(ps aux | grep "$jar_name" | grep -v "grep" | awk '{print $2}'); do
    kill "$pid"
  done
  echo "Done!"
}


# Trap SIGINT to call kill_nodes before exiting
trap 'kill_nodes; exit' SIGINT

# Compile and run
echo "Compiling.."
echo
if mvn clean package; then
  echo "Compilation successful"
  echo

  populate_peers
  echo "Using peers list: $peers"

  populate_clients
  echo "Using clients list: $clients"

  i=0
  while [ $i -lt $((peers_count + clients_count)) ]; do
    start_process $((port + i))
    i=$((i + 1))
  done


  read -r
  kill_nodes
else
  echo "Maven compilation failed"
  exit 1
fi