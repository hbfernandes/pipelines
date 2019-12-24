# Testing Github Actions
This repos serves to test github actions to perform orchestrated builds with multiple repositories.

## Runner
The provided docker-compose allows to launch a docker runner and scale it to any number with a few constraits.

- Docker must be available on the host
- The path `/workspace` must exist on the host and have write permission for uid:gid `1000:1000`.

An `env` file is required in the current directory to provide the following variables:
- GITHUB_REPOSITORY: URL for the repository the runner is connecting to, for example `https://github.com/hbfernandes/pipelines`.
- GITHUB_TOKEN: The token that allows adding runners. This needs to be picked up on the repo settins in the actions section.


To start `N` runners use the command:
```
docker-compose up -d --scale runner=N
```