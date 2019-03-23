#!/usr/bin/env bash

if [ "$1" = "--learn" ]; then
  java -cp '.;bin' Learner
else
  java -cp '.;bin' Project3 "$@"
fi
