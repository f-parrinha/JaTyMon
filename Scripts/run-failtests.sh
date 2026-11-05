#!/usr/bin/env bash
#
# 		run-failtests.sh
#
# 	Runs JaTyMon on the failure example projects (ill-formed typestates) and checks
# 		that each well-formedness rule is enforced with the expected diagnostic.
#
# 	Usage: bash run-failtests.sh [-v (verbose)] [rule numbers...]     	(after build-tool.sh)
# 		-	Example 1: bash run-failtests.sh            		# all nine rules
# 		- Example 2: bash run-failtests.sh 1 2 6 7    		# only rules 1, 2, 6 and 7
# Options: 
#		- '-v' 	(verbose, prints the execution of tests)
# 	Requires: common.sh
#
#	Author: Francisco Parrinha
#

set -euo pipefail
source "$(dirname "${BASH_SOURCE[0]}")/common.sh"

# Script configs
VERBOSE=0
CONF_LEVEL="${CONF_LEVEL:-0.95}"
RUNNER="$JATYMON_OUT/run-JaTyMon-maven.sh"

# 	Array with test information
# 		TESTS[n]="<folder>;<expected diagnostic text>;<expected number of errors>"
#		Supports regular expression operators in <expected diagnostic text>, like: "Unknown Assignment|Unknown Predicate" 
TESTS[1]="Rule1_NoDuplicateStateName;Duplicate state;1"
TESTS[2]="Rule2_ValidRatioSum;Invalid ratio sum value;2"
TESTS[3]="Rule3_EnumerateAllDecisions;All decisions in decision states must be enumerated;2"
TESTS[4]="Rule4_ClosedAssignmentsAndPredicates;Unknown assignment|Unknown predicate;2"
TESTS[5]="Rule5_WellScopedExpressions;Unknown field with name;3"
TESTS[6]="Rule6_ReadOnlyConstants;error;1"
TESTS[7]="Rule7_WellTypedRelationalTerms;error;1"
TESTS[8]="Rule8_UsefulStates;error;1"
TESTS[9]="Rule9_Deterministic;error;1"


# ---------------------- Main Execution ---------------------- #


# Parse verbose option
while getopts "v" opt; do
  case "$opt" in
    v) VERBOSE=1 ;;
    *) echo "Usage: $0 [-v] [rule numbers...]" >&2; exit 2 ;;
  esac
done
shift $((OPTIND - 1))  	 # remove the flag (-v), leaving only the rule numbers in "$@"


# Verify that the JaTyMon runner exists
[ -f "$RUNNER" ] || { echo "Missing $RUNNER" >&2; exit 1; }

# Add every test to script args if no argument is given
if [ "$#" -eq 0 ]; then
  set -- 1 2 3 4 5 6 7 8 9
fi

# Verify that selected tests are valid before running anything
for n in "$@"; do
  if ! [[ "$n" =~ ^[1-9]$ ]] || [ -z "${TESTS[$n]:-}" ]; then
    echo "Unknown rule number: '$n' (valid: 1-9)" >&2
    exit 2
  fi
done


total=$#		# Total number of tests
pass=0			# Number of tests that have passed
fail=0			# Number of tests that have failed
i=0				# Just a loop index

# Execute tests
for n in "$@"; do
  IFS=';' read -r folder fragment expected <<<"${TESTS[$n]}"
  i=$((i+1))

  out=$(bash "$RUNNER" "$FAILTESTS_DIR/$folder" "$CONF_LEVEL" 2>&1 || true)
  if [ "$VERBOSE" -eq 1 ]; then
    echo "$out"
  fi
  
  found=$(grep -cE "$fragment" <<<"$out" || true)
  errors=$(grep -c 'ERROR' <<<"$out" || true)					# This stuff (error text) contains ANSI chars, be careful
  
  if [ "$found" -eq "$expected" ] && [ "$errors" -eq "$expected" ]; then
    printf '[%d/%d] PASS  Rule %s (%s)\n' "$i" "$total" "$n" "$folder"
    printf '        expected: %d x "%s"   observed: %d\n' "$expected" "$fragment" "$found"
    grep -E "$fragment" <<<"$out" | sed 's/^/        > /'
    pass=$((pass+1))
  else
    printf '[%d/%d] FAIL  Rule %s (%s)\n' "$i" "$total" "$n" "$folder"
    printf '        expected: %d x "%s"   found: %d matching, %d errors total\n' \
      "$expected" "$fragment" "$found" "$errors"
    fail=$((fail+1))
  fi
done

# Print results
echo
echo "Summary: $pass/$total ill-formed typestates rejected with the expected diagnostics"
if [ "$fail" -eq 0 ]; then
  echo "Success: every expected error was reported"
else
  echo "Failure: $fail mismatches"
fi
exit $((fail > 0))
