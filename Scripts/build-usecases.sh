#!/usr/bin/env bash
#
# 		build-usecases.sh
#
# Builds each use case (ABD and MultiPaxos). Must be executed after the monitors
# are generated (using gen-monitors.sh)
#
# Usage: bash build-usecases.sh
# Requires:
# 		- common.sh
#
# Author: Francisco Parrinha
#

set -euo pipefail
source "$(dirname "${BASH_SOURCE[0]}")/common.sh"

if [ "$#" -gt 0 ]; then
echo "Usage: $0" >&2
    exit 1
fi

# ----------------- Main Execution ----------------- #


for uc in "${USE_CASES[@]}"; do
    echo "[build-usecases] $uc"
    (cd "$USE_CASES_DIR/$uc" && mvn -B clean package -DskipTests)
done

echo "[build-usecases] done"
