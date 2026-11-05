#!/usr/bin/env bash
#
# 		clear-benchmarks.sh
#
# Removes all collected benchmarks
#
# Usage: clear-benchmarks.sh
#

set -euo pipefail
source "$(dirname "${BASH_SOURCE[0]}")/common.sh"

if [ "$#" -gt 0 ]; then
echo "Usage: $0" >&2
    exit 1
fi


# ----------------- Main Execution ----------------- #


rm -rf -- "$BENCHMARKS"/*
