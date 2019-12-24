#!/usr/bin/env bash

# Verify repo URL and token have been given, otherwise we must be interactive mode.
if [ -z "$GITHUB_REPOSITORY" ] || [ -z "$GITHUB_TOKEN" ]; then
  echo "GITHUB_REPOSITORY and GITHUB_TOKEN cannot be empty"
  exit 1
fi

if [ -z "$RUNNER_NAME" ]; then
	export RUNNER_NAME="$(hostname)"
fi

printf "Configuring GitHub Runner for $GITHUB_REPOSITORY\n"
printf "\tRunner Name: $RUNNER_NAME\n\tWorking Directory: $WORK_DIR\n"
/app/config.sh --url $GITHUB_REPOSITORY --token $GITHUB_TOKEN --work $WORK_DIR/$RUNNER_NAME --name $RUNNER_NAME

printf "Executing GitHub Runner for $GITHUB_REPOSITORY\n"
/app/run.sh