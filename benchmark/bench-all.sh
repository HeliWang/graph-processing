#!/bin/bash

# Runs all the benchmarks. If something fails, it will
# just continue to the next experiment.
#
# The mass-benchmarking scripts are quite primitive,
# simply because things can and will fail and it's
# usually easier to intervene manually.

read -p "Press enter to continue..."

cd "$(dirname "${BASH_SOURCE[0]}")"
source ./common/get-hosts.sh
source ./common/get-dirs.sh

# start (or restart) Hadoop
./hadoop/restart-hadoop.sh
hadoop dfsadmin -safemode wait > /dev/null

# must cd to the correct directory each time
echo "Running Giraph experiments..."
cd ./giraph/
./bench${nodes}.sh

echo "Running GPS experiments..."
cd ../gps/
./bench${nodes}.sh

echo "Running GraphLab experiments..."
cd ../graphlab/
./bench${nodes}.sh

echo "Running Mizan experiments..."
cd ../mizan/
./bench${nodes}.sh