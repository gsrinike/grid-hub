# Architecture Documentation

`doc.arch` records the architecture decisions, module boundaries, and local
runtime conventions for `grid-hub`.

## Contents

- `DESIGN_DECISIONS.md`: design decisions and principles for module ownership,
  dependency direction, configuration, adapters, local workflow, observability,
  and test scope.
- `MODULE_CLASSIFICATION.md`: module naming and dependency rules for `com`,
  `data`, `map`, `srv`, `gui`, and documentation modules.
- `LOCAL_DEPLOYMENT_AND_ENVIRONMENT.md`: local build, deployment, Docker, and
  runtime environment resolution conventions.

## Maven

This module is included by the root Maven build and packages the Markdown files
as module resources:

```sh
mvn -pl doc.arch package
```
