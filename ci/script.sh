#!/bin/bash

# Exit on any failure
set -e

sbt cli/assembly

VERSION=`sbt version | tail -n 1 | awk '{print $2}'`

if [ $TRAVIS_OS_NAME = windows ]; then
  cp "cli/target/scala-2.13/cli-assembly-$VERSION.jar" phony.jar
  #find target -iname "*.jar" -exec cp {} phony.jar \;
  ci/windows.bat
elif [ $TRAVIS_OS_NAME = osx ]; then
  native-image --verbose --no-fallback -H:IncludeResources='locales/.*.json' -jar "cli/target/scala-2.13/cli-assembly-$VERSION.jar" phony
else
  native-image --verbose --static --no-fallback -H:IncludeResources='locales/.*.json' -jar "cli/target/scala-2.13/cli-assembly-$VERSION.jar" phony
fi

mkdir release

if [ $TRAVIS_OS_NAME = windows ]; then
  mv phony.exe release
else
  mv phony "release/phony-$TRAVIS_OS_NAME"
fi
