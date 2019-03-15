#!/bin/sh

set -e -x

cd github
  ./mvnw clean package
cd ..

cp github/target/articulate-*.jar build-output/articulate.jar
