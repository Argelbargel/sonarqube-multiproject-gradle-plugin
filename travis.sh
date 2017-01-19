#!/usr/bin/env bash
TAG=$1
KEY=$2
SECRET=$3

./gradlew check -Prelease=${TAG}

if [ "$TAG" != "" ]; then
    ./gradlew publish -Prelease=${TAG}
    ./gradlew publishPlugins -Prelease=${TAG} -Dgradle.publish.key=${KEY} -Dgradle.publish.secret=${SECRET}
fi

