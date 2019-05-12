#!/usr/bin/env bash

# This script was used to analyze AI versus AI;
# Agent X versus Agent O, both having their own
# way to sort successors and utility function.
# Passing '--multi N' as an argument would analyze
# N multiple games between the two AI. Omit N for a
# default of 10 games.

java -cp '.;bin' Analysis "$@"