#!/usr/bin/env bash

parent_path=$( cd "$(dirname "${BASH_SOURCE[0]}")" ; pwd -P )
cd "$parent_path"

./gradlew build

java -jar ./server/build/libs/server-all.jar -config=./application.yaml