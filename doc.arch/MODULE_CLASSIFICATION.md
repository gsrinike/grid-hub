# Module Classification

This document classifies project modules and defines the dependency rules for
each module family.

## `com.*` Modules

`com.*` modules provide reusable cross-cutting capabilities.

Examples:

- `com.env`: environment resolution
- `com.utils`: shared utilities and cache abstractions
- `com.app.config`: module configuration loading
- `com.infra`: infrastructure adapters
- `com.auth`: authentication and authorization services
- `com.mapping`: generic object mapping support

Rules:

- Keep APIs generic.
- Hide technology dependencies behind services, adapters, or factories.
- Avoid importing service modules.
- Avoid domain-specific workflow orchestration unless the capability is truly
  shared.

## `data.*` Modules

`data.*` modules define stable DTOs, enums, validation rules, and domain
vocabulary.

Rules:

- Do not depend on Spring MVC, persistence adapters, RabbitMQ, MinIO,
  Elasticsearch, or UI code.
- Keep storage, messaging, web, and presentation behavior out of data modules.
- Isolate specialized domain engines or standards behind stable DTO projections
  when they are required.

## `map.*` Modules

`map.*` modules transform between data models.

Rules:

- Depend on source and target `data.*` modules.
- Use shared mapping utilities for generic field transfer.
- Keep technology adapters and API orchestration outside mapping modules.

## `srv.*` Modules

`srv.*` modules are backend services. They expose APIs, orchestrate workflows,
apply validation, call infrastructure adapters, and publish domain events.

Rules:

- Use Java package names that reflect the service domain and capability.
- Depend only on required `data.*`, `map.*`, `com.app.config`, and `com.infra`
  modules.
- Use Spring Boot only in runnable service modules.
- Use standard logging and OpenTelemetry where runtime telemetry is emitted.

## `gui.*` Modules

`gui.*` modules are frontend applications wrapped by Maven for consistent build
and Docker lifecycle behavior.

Rules:

- Use HTTP API contracts rather than importing Java backend code.
- Keep UI workflows feature-complete and responsive.
- Use Maven as the wrapper for npm build, test, package, and Docker lifecycle.

## `doc.*` Modules

`doc.*` modules own project documentation, architecture records, operational
notes, and developer guidance.

Rules:

- Keep documentation close to the Maven module graph so it is visible in normal
  project navigation.
- Avoid runtime dependencies.
- Link to module READMEs for implementation details.
- Package Markdown files as resources when the documentation module participates
  in Maven builds.

## Naming Examples

- Common capability: `com.audit`
- Data model: `data.market`
- Mapping module: `map.market.iidm`
- Backend service: `srv.grid.analysis`
- Frontend app: `gui.grid.analysis`
- Architecture documentation: `doc.arch`
