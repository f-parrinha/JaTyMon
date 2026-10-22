#!/usr/bin/env bash
#
# 		gen-benchmarks.sh
#
# Generates one benchmark table per use case, with one row per run, from the
# files collected by run-orchestrator.sh. Mean and standard deviation of each
# column are added at the bottom.
#
# Run i of "monitors on" is paired with run i of "monitors off" in
# chronological order (folders are named <use case>-<timestamp>).
#
# Usage: gen-benchmarks.sh
#
# Output (per use case, in $BENCHMARKS):
#   - <use case>_benchmarks.csv
#   and the same table printed to the console.
#
# The standard deviation is the sample one (n-1).
#
# Requires:
# 	- common.sh
#

set -euo pipefail
source "$(dirname "${BASH_SOURCE[0]}")/common.sh"

# Prints the last number on the last line matching a label (empty if none)
metric() {
    local v
    v="$(grep -i "$2" "$1" | tail -n 1 | grep -oE '[0-9]+\.[0-9]+|[0-9]+' | tail -n 1)" || true
    echo "$v"
}

generate_table() {
    local uc="$1"
    local csv_file="$BENCHMARKS/${uc}_benchmarks.csv"
    local on_files=() off_files=()
    local d f

    # Chronological lists of client files: folders sorted by name, files sorted inside
    while IFS= read -r -d '' d; do
        while IFS= read -r -d '' f; do on_files+=("$f"); done \
            < <(find "$d" -type f -name '*monitors-on*' -print0 | sort -z)
        while IFS= read -r -d '' f; do off_files+=("$f"); done \
            < <(find "$d" -type f -name '*monitors-off*' -print0 | sort -z)
    done < <(find "$BENCHMARKS" -mindepth 1 -maxdepth 1 -type d -iname "${uc}-*" -print0 | sort -z)

    local n_on=${#on_files[@]} n_off=${#off_files[@]}
    local rows=$(( n_on > n_off ? n_on : n_off ))

    if [ "$rows" -eq 0 ]; then
        echo ""
        echo "=== $uc: no benchmark files found in $BENCHMARKS ==="
        return
    fi
    if [ "$n_on" -ne "$n_off" ]; then
        echo "Warning: $uc has $n_on runs with monitors on but $n_off with monitors off; missing cells are N/A" >&2
    fi

    # One CSV line per run: case,on_read,on_write,off_read,off_write
    local body="" i on_r on_w off_r off_w
    for ((i = 0; i < rows; i++)); do
        on_r=""; on_w=""; off_r=""; off_w=""
        if [ "$i" -lt "$n_on" ]; then
            on_r="$(metric "${on_files[$i]}" "Average read latency")"
            on_w="$(metric "${on_files[$i]}" "Average write latency")"
        fi
        if [ "$i" -lt "$n_off" ]; then
            off_r="$(metric "${off_files[$i]}" "Average read latency")"
            off_w="$(metric "${off_files[$i]}" "Average write latency")"
        fi
        body+="$((i + 1)),${on_r:-N/A},${on_w:-N/A},${off_r:-N/A},${off_w:-N/A}"$'\n'
    done
    body="${body%$'\n'}"

    # Mean and sample standard deviation per column (N/A cells are ignored)
    # Uses Welford's algorithm
    local stats
    stats="$(awk -F, '
    {
        for (c = 2; c <= 5; c++) if ($c != "N/A") {
            n[c]++; d = $c - m[c]; m[c] += d / n[c]; s[c] += d * ($c - m[c])
        }
    }
    END {
        printf "Mean"
        for (c = 2; c <= 5; c++) printf ",%s", (n[c] > 0 ? sprintf("%.4f", m[c]) : "N/A")
        printf "\nStd"
        for (c = 2; c <= 5; c++) printf ",%s", (n[c] > 1 ? sprintf("%.4f", sqrt(s[c] / (n[c] - 1))) : "N/A")
        printf "\n"
    }' <<< "$body")"

    # CSV file
    {
        echo "Case,Monitors On Read,Monitors On Write,Monitors Off Read,Monitors Off Write"
        echo "$body"
        echo "$stats"
    } > "$csv_file"

    # Console table
    local fmt="%-6s | %-12s | %-12s | %-12s | %-12s\n"
    echo ""
    echo "=== $uc ($rows runs, saved to $csv_file) ==="
    printf "%-6s | %-27s | %-27s\n" "" "Monitors On" "Monitors Off"
    printf "$fmt" "Case" "Read" "Write" "Read" "Write"
    printf '%s\n' "-------+--------------+--------------+--------------+-------------"
    while IFS=, read -r a b c d e; do printf "$fmt" "$a" "$b" "$c" "$d" "$e"; done <<< "$body"
    printf '%s\n' "-------+--------------+--------------+--------------+-------------"
    while IFS=, read -r a b c d e; do printf "$fmt" "$a" "$b" "$c" "$d" "$e"; done <<< "$stats"
}

# Ensure Benchmarks directory exists
if [ ! -d "$BENCHMARKS" ]; then
    echo "Error: Directory $BENCHMARKS does not exist." >&2
    exit 1
fi

echo "Scanning benchmarks and generating tables..."
for uc in "${USE_CASES[@]}"; do
    generate_table "$uc"
done
