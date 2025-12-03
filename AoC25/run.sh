#!/bin/bash

echo "==== Cleaning old class files ===="
find . -type f -name "*.class" -delete

echo "==== Compiling Java sources ===="
javac -cp . $(find . -type f -name "*.java")

if [ $? -ne 0 ]; then
    echo "Compilation failed."
    exit 1
fi

echo "==== Running Main ===="
java Main
