#!/bin/bash

echo "==== Cleaning old class files ===="
find . -type f -name "*.class" -delete
echo "==== Done cleaning ===="