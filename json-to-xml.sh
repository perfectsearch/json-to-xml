#!/bin/sh
# ============================================================================
# Adapt this script, which simply executes the built binary in ./target, to
# whatever environment you wish.
# ============================================================================
JAVA_INFO=$(java -version 2>&1 >/dev/null)
#echo "java -version yields $JAVA_INFO"

# isolate version...
QUOTED_VERSION=`echo $JAVA_INFO | awk '{ print $3 }'`
#echo "QUOTED_VERSION=$QUOTED_VERSION"

# smoke the surrounding quotes...
VERSION=`echo $QUOTED_VERSION | sed s/\"//g`
#echo "VERSION=$VERSION"

# isolate the major version...
MAJOR=`echo $VERSION | awk -F'.' '{ print $2 }'`
#echo "MAJOR=$MAJOR"

if [ "$MAJOR" != "8" ]; then
	JAVA=$JAVA8
else
	JAVA=java
fi

if [ -z "$JAVA" ]; then
	echo "
  Please export path to Java 8 binary as variable JAVA8 before invoking, e.g:
  export JAVA8=/home/user/dev/jdk1.8.0_25/bin/java
	"
	exit 0
fi

${JAVA} -jar ./target/json-to-xml-1.0.0-SNAPSHOT-jar-with-dependencies.jar $*
# vim: set tabstop=2 shiftwidth=2 noexpandtab:
