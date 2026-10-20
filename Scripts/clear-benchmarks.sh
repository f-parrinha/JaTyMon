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

sudo rm -rf -- "$BENCHMARKS"
