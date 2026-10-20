#!/usr/bin/env bash
#
# 		build-tool.sh
#
# Builds the tool to some directory using the tool's build.sh (default: /opt/jatymon)
# First script to be ran
#
# Usage: ./build-tool.sh
# Requires:
# 	- common.sh
#
# Author: Francisco Parrinha
#

set -euo pipefail
source "$(dirname "${BASH_SOURCE[0]}")/common.sh"

cd "$ROOT/Tool"

bash ./build.sh -o "$JATYMON_OUT"

# Install lib (run from Tool/ so Maven has a project context for the purge step)
bash "$JATYMON_OUT/install-JaTyMon-maven.sh"

echo "[build-tool] done: $JATYMON_OUT"
