#!/bin/bash

# Current directory
#CURRENT_DIR=$(pwd)
CURRENT_DIR="/Users/newuser/Documents/java-folder-project /from-stratch/sales-app-new/portfolio-service"


# Build artifact directory
BUILD_DIR="$CURRENT_DIR/target"

# Dockerfile location
DOCKERFILE="$CURRENT_DIR/src/main/Docker/Dockerfile.jvm"

# Image name
IMAGE_NAME="portfolio-image"

# Function to check if any source file has been modified since the last build
function is_code_modified() {
  find $CURRENT_DIR -name '*.*' | while read file; do
    if [ $file -nt $BUILD_DIR ]; then
      return 0
    fi
  done
  return 1
}

# Build and package the project if any source file has been modified
if is_code_modified; then
  mvn clean package
  docker build -f $DOCKERFILE -t $IMAGE_NAME .
fi

# Run the docker image
docker run -i --rm -p 8080:8080 $IMAGE_NAME
