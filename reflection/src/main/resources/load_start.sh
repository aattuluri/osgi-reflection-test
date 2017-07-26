#!/bin/bash
tps=$1
ctr=1
echo "Running with TPS: $tps"

while [ $ctr -le $tps ]
do
  curl -s http://localhost:10081/test?[1-10000000] &> /dev/null &
  pidlist="$pidlist $!"
  ((ctr++))
done
