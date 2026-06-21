# Design Decisions

This document describes the main architecture decisions adopted for `grid-hub`.
The decisions are organized around module ownership, dependency direction,
configuration, runtime boundaries, local deployment, observability, testing, and
documentation.

## 1. Use Narrow Module Ownership

Each module should have a clear purpose and a small ownership boundary.

- `com.*` modules provide shared capabilities such as environment resolution,
  configuration loading, infrastructure adapters, mapping support, utilities,
  cache abstractions, authentication, and authorization.
- `data.*` modules define shared data contracts, DTOs, validation rules, enums,
  and domain vocabulary.
- `map.*` modules transform between data models.
- `srv.*` modules expose backend APIs and orchestrate use cases.
- `gui.*` modules provide user-facing frontend applications.
- `doc.*` modules own project and architecture documentation.

This keeps service modules focused on workflow orchestration instead of making
them responsible for all technical details.

## 2. Keep Dependencies Pointing Toward Stable Contracts

Dependencies should flow from use-case modules toward stable contracts and
cross-cutting utilities.

- Service modules may depend on required `data.*`, `map.*`, `com.app.config`,
  and `com.infra` modules.
- Mapping modules may depend on source and target data modules plus shared
  mapping utilities.
- Data modules must not depend on infrastructure, web frameworks, message
  brokers, object storage, search engines, or UI code.
- GUI modules communicate through HTTP contracts rather than importing backend
  Java code.
- Documentation modules should not introduce runtime dependencies.

The root Maven POM manages module inclusion and shared build defaults without
forcing every module to inherit unnecessary technology dependencies.

## 3. Prefer Configuration Over Code

Runtime behavior should be driven by external configuration rather than
hard-coded environment decisions.

Module configuration follows this shape:

- `base/<module>-application.xml`
- `base/<module>-infra.xml`
- `base/<module>-cache-config.yml`
- `<env>/<module>-application.xml`
- `<env>/<module>-infra.xml`
- `<env>/<module>-cache-config.yml`

Base configuration defines stable defaults. Environment folders override only
the values that differ for `local`, `prod`, or another supported environment.

## 4. Hide Technology Behind Adapters and Factories

Technology-specific behavior should sit behind service interfaces, adapters, and
factory boundaries.

Examples of this pattern include:

- repository services hiding search engine access
- object storage services hiding object store access
- event publisher services hiding message broker access
- infrastructure adapter factories resolving concrete adapters
- mapping services hiding object mapping implementations

This keeps backend services easier to test and makes infrastructure replacement
possible without rewriting use-case orchestration.

## 5. Align Domain Vocabulary Without Locking Services to Runtime Engines

Domain data modules may align with a domain-specific engine, standard, or
external model, but service APIs should consume stable DTO projections instead
of runtime engine objects.

When a specialized runtime is needed, isolate it behind data, mapping, or
adapter modules. Services should depend on stable contracts and projections so
API and GUI contracts remain stable.

## 6. Optimize for Local-First Development

The project should be reproducible locally with Maven and Docker Compose.

- Maven compiles, tests, and packages Java modules.
- Frontend modules use Maven as a wrapper around npm build, test, package, and
  Docker lifecycle tasks.
- Docker Compose starts infrastructure and application containers.
- Docker image build and push behavior is controlled through Maven properties.

Targeted module builds should be possible with:

```sh
mvn -pl <module> -am test
```

Docker image work can be disabled for local Java checks:

```sh
mvn -Ddocker.skip.build=true -Ddocker.skip.push=true test
```

## 7. Add Observability to Runnable Services

Runnable backend modules should include standard logging and OpenTelemetry when
they emit runtime telemetry. Library, DTO, mapping, and documentation modules
should avoid runtime observability dependencies unless they directly own
observable behavior.

## 8. Match Test Scope to Risk

Tests should stay close to the behavior being changed.

- Data modules test parsing, validation, catalog alignment, and invariants.
- Mapping modules test transformations between source and target models.
- Service modules test controller validation, orchestration, and adapter-facing
  behavior.
- GUI modules use TypeScript builds and focused component tests.
- Documentation modules should build through Maven so broken module wiring is
  caught early.

## 9. Keep Documentation Beside the Module

Each module should own a `README.md` that explains purpose, contents,
implementation notes, and developer commands. Architecture documentation should
link to module READMEs rather than duplicating every implementation detail.
