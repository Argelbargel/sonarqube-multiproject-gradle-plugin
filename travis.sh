#!/usr/bin/env bash

./gradlew assemble -Prelease=${TRAVIS_TAG}
./gradlew check -Prelease=${TRAVIS_TAG}

if [ "$TRAVIS_TAG" != "" ]; then
    ./gradlew publish -Prelease=${TRAVIS_TAG}
    ./gradlew publishPlugins -Prelease=${TRAVIS_TAG} -Dgradle.publish.key=${GRADLE_PUBLISH_KEY} -Dgradle.publish.secret=${GRADLE_PUBLISH_SECRET}
fi

