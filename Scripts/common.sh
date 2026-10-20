
#
# 		common.sh
#
# Sourced by the other scripts; not meant to be executed. Contains common attributes
#
# Author: Francisco Parrinha
#

# BASH_SOURCE (not $0) so it resolves to this file even when sourced.
: "${ROOT:=$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)}"
: "${JATYMON_OUT:=/opt/jatymon}"
: "${USE_CASES_DIR:=$ROOT/Use Cases}"
: "${SCRIPTS_DIR:=$ROOT/Scripts}"
: "${BENCHMARKS:=$ROOT/Benchmarks}"      
USE_CASES=(ABD MultiPaxos)

export ROOT JATYMON_OUT USE_CASES_DIR SCRIPTS_DIR BENCHMARKS
