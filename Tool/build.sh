#!/bin/zsh

#
# Builds the code using maven, runs tests, creates the different jars, and may copy the jars and generate a run script
#   to a desired folder
# Arguments:
#   -o: output directory for the jars, and where the generated script will be stored
#
# Author: Francisco Parrinha
#


# # # # # # # # # # # # # # # # # # # #
#               GLOBALS               #
# # # # # # # # # # # # # # # # # # # #

echo "Initializing..."
echo
TEST_MONITORS_OUTPUT_PATH="monitors-generated"
RUN_SCRIPT_NAME="run-JaTyMon-maven"
INSTALL_LIB_SCRIPT_NAME="install-JaTyMon-maven"
JAR_OUTPUT_PATH=""
VERSION=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)
SCRIPT_DIR=$(cd "$(dirname "$0")" && pwd)


# # # # # # # # # # # # # # # # # # # #
#               SCRIPTS               #
# # # # # # # # # # # # # # # # # # # #

# -------- LIB SCRIPT -------- #

INSTALL_LIB_SCRIPT_CONTENT=$(cat << EOF
#!/bin/zsh

VERSION="$VERSION"
SCRIPT_DIR=\$(cd "\$(dirname "\$0")" && pwd)
LIB_JAR="\$SCRIPT_DIR/lib-$VERSION-jar-with-dependencies.jar"
LIB_GROUP_ID="jatymon"
LIB_ARTIFACT_ID="lib"

# Remove and print what will be removed
find ~/.m2/repository/jatymon/lib -name "*.jar" 2>/dev/null | while read jar; do
    echo "Removing: \$jar"
done
echo
mvn dependency:purge-local-repository \
    -DmanualInclude=jatymon:lib \
    -DreResolve=false \
    -DactTransitively=false \
    -DreResolve=false \
    -Dverbose=true

if [ \$? -ne 0 ]; then
    echo "Error: Failed to remove older versions of lib."
    exit 1
fi

# Finally install
echo
echo "Installing lib \$VERSION to local Maven repo..."
echo
mvn install:install-file \
    -Dfile="\$LIB_JAR" \
    -DgroupId=\$LIB_GROUP_ID \
    -DartifactId=\$LIB_ARTIFACT_ID \
    -Dversion=\$VERSION \
    -Dpackaging=jar \
    -DgeneratePom=true

if [ \$? -ne 0 ]; then
  echo
  echo "Error: Failed to install lib."
  exit 1
fi
EOF
)

# -------- RUN SCRIPT -------- #

RUN_SCRIPT_CONTENT=$(cat << EOF
#!/bin/zsh

VERSION="$VERSION"
SCRIPT_DIR=\$(cd "\$(dirname "\$0")" && pwd)
PROCESSOR_JAR="\$SCRIPT_DIR/core-\$VERSION-jar-with-dependencies.jar"
LIB_JAR="\$SCRIPT_DIR/lib-\$VERSION-jar-with-dependencies.jar"

if [ "\$#" -ne 2 ]; then
    echo "Usage: \$0 <project path> <confidence level>"
    exit 1
fi

if ! [[ "\$2" =~ ^[0-9]+(\.[0-9]+)?$ ]]; then
    echo "Error: confidence level must be a number"
    exit 1
fi

# Convert to absolute path to handle spaces safely
ABS_PROJECT_PATH=\$(cd "\$1" && pwd)
CONFIDENCE_LEVEL="\$2"
SOURCE_PATH="src/main/java"
MONITORS_OUTPUT="\$ABS_PROJECT_PATH/\$SOURCE_PATH"
DEP_DIR="\$ABS_PROJECT_PATH/target/dependency"

echo
echo "Collecting Maven dependencies..."
mvn -f "\$ABS_PROJECT_PATH/pom.xml" \
    org.apache.maven.plugins:maven-dependency-plugin:3.6.0:copy-dependencies \
    -DoutputDirectory="\$DEP_DIR" -q

if [ ! -d "\$DEP_DIR" ]; then
    echo "ERROR: Failed to create dependency folder at \$DEP_DIR"
    exit 1
fi

CP_DEPS=\$(find "\$DEP_DIR" -name "*.jar" | paste -sd ":" -)

echo "Running JaTyMon Processor..."
find "\$ABS_PROJECT_PATH/\$SOURCE_PATH" -name "*.java" -print0 | xargs -0 javac \\
    -processor jatymon.JaTyMonProcessor \\
    -processorpath "\$PROCESSOR_JAR" \\
    -classpath "\$LIB_JAR:\$PROCESSOR_JAR:\$CP_DEPS" \\
    -sourcepath "\$ABS_PROJECT_PATH/\$SOURCE_PATH" \\
    -proc:only \\
    -Aoutput="\$MONITORS_OUTPUT" \\
    -AconfidenceLevel="\$CONFIDENCE_LEVEL"
EOF
)


# # # # # # # # # # # # # # # # # # # #
#                 MAIN                #
# # # # # # # # # # # # # # # # # # # #


# Adds -o to the script args as an output to the created Jars
# Uses zsh/bash "getops" command
while getopts "o:" opt; do
  case $opt in
    o) JAR_OUTPUT_PATH="$OPTARG" ;;
    *) echo "Usage: $0 [-o <jar output path>]"; exit 1 ;;
  esac
done

echo "Reading parameters..."
echo "Project version: $VERSION"
echo "Script directory: $SCRIPT_DIR"
echo "Test monitors output path: $TEST_MONITORS_OUTPUT_PATH"
echo "Jar output path: $JAR_OUTPUT_PATH"
echo
echo
echo "Starting build with Maven..."
echo

mvn clean package -Doutput="$SCRIPT_DIR/$TEST_MONITORS_OUTPUT_PATH"
if [ $? -ne 0 ]; then
  echo "Error: Maven build failed."
  exit 1
fi
if [ -z "$(ls -A "$SCRIPT_DIR/$TEST_MONITORS_OUTPUT_PATH" 2>/dev/null)" ]; then
  echo "Error: No monitors were generated in $SCRIPT_DIR/$TEST_MONITORS_OUTPUT_PATH."
  exit 1
fi

echo
echo "Build complete."


# # # # # # # # # # # # # # # # # # # #
# BUILD OUTPUT FOLDER WITH RUN SCRIPT #
# # # # # # # # # # # # # # # # # # # #


if [ -n "$JAR_OUTPUT_PATH" ]; then
  CORE_JAR="core/target/core-$VERSION-jar-with-dependencies.jar"
  LIB_JAR="lib/target/lib-$VERSION-jar-with-dependencies.jar"

  echo
  echo "Returning build output..."
  echo

  # Detect non existing jar files in target folder
  if [ ! -f "$CORE_JAR" ]; then
      echo "Error: Core jar not found at $CORE_JAR."
      exit 1
  fi
  if [ ! -f "$LIB_JAR" ]; then
      echo "Error: Lib jar not found at $LIB_JAR."
      exit 1
  fi

  # Copies the jars
  mkdir -p "$JAR_OUTPUT_PATH"
  cp "$CORE_JAR" "$JAR_OUTPUT_PATH"
  cp "$LIB_JAR" "$JAR_OUTPUT_PATH"
  if [ $? -ne 0 ]; then
      echo "Error: Failed to copy jars to $JAR_OUTPUT_PATH."
      exit 1
  fi
  echo "Jars copied to $JAR_OUTPUT_PATH."

  # Create scripts
  echo "$INSTALL_LIB_SCRIPT_CONTENT" > "$JAR_OUTPUT_PATH/$INSTALL_LIB_SCRIPT_NAME.sh"
  echo "$RUN_SCRIPT_CONTENT" > "$JAR_OUTPUT_PATH/$RUN_SCRIPT_NAME.sh"

  # Add execute permissions
  chmod +x "$JAR_OUTPUT_PATH/$INSTALL_LIB_SCRIPT_NAME.sh"
  echo "$INSTALL_LIB_SCRIPT_NAME.sh generated in $JAR_OUTPUT_PATH."
  chmod +x "$JAR_OUTPUT_PATH/$RUN_SCRIPT_NAME.sh"
  echo "$RUN_SCRIPT_NAME.sh generated in $JAR_OUTPUT_PATH."
  echo
  echo "Build result complete."
fi