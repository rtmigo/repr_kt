#!/bin/bash
set -e && cd "${0%/*}"

OUTPUT=$(gradle run -q)

if [ "$OUTPUT" == 'listOf(1, 2, 3)' ]
then
  echo "OK"
  exit 0
else
  echo "Unexpected output: "$OUTPUT""
  exit 1
fi