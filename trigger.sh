#!/bin/bash -e

curl \
  -H "Accept: application/vnd.github.everest-preview+json" \
  -H "Authorization: token $1" \
  --request POST \
  --data '{"event_type": "do-something"}' \
  https://api.github.com/repos/hbfernandes/pipelines/dispatches
