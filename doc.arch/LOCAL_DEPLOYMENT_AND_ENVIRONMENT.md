# Local Deployment And Environment Resolution

## Local Deployment Principle

Local deployment should use the same artifacts produced by the normal build
pipeline:

1. Maven builds Java modules and frontend wrappers.
2. npm builds frontend bundles for `gui.*` modules.
3. Docker Compose starts infrastructure and application containers.
4. Runtime configuration is resolved from environment-specific module files.

The goal is for developers to reproduce the application stack without manual
service setup.

## Local Build

Build without Docker image work:

```sh
mvn -Ddocker.skip.build=true -Ddocker.skip.push=true clean package
```

Run a focused module slice:

```sh
mvn -Ddocker.skip.build=true -Ddocker.skip.push=true -pl <module> -am test
```

Run a frontend build from a GUI module:

```sh
cd gui.<domain>.<app>
npm run build
```

## Docker Compose

Docker Compose should start infrastructure dependencies and application
containers from locally built artifacts.

Typical infrastructure dependencies include:

- search
- object storage
- messaging
- observability
- identity and access management

Docker-enabled modules should configure only their module-specific image name.
The full image name should be assembled consistently from root Maven
properties.

## Docker Maven Lifecycle

The root POM should own shared Docker behavior through Maven properties:

```xml
<docker.registry>docker.io</docker.registry>
<docker.namespace>gsrinike</docker.namespace>
<docker.image.tag>${project.version}</docker.image.tag>
<docker.image.latest-tag>latest</docker.image.latest-tag>
<docker.skip.build>false</docker.skip.build>
<docker.skip.push>false</docker.skip.push>
<maven.deploy.skip>true</maven.deploy.skip>
```

Local development can override these properties to skip Docker build and push
steps while still compiling and testing the code.

## Environment Resolution Principle

Environment selection is centralized and follows this precedence:

1. JVM system property `env`
2. Operating system environment variable `ENV`
3. Default value `local`

The resolved value is normalized and used by application configuration loading.

## Configuration Loading Order

Application configuration is loaded in this order:

1. `base/<module>-application.xml`
2. `base/<module>-infra.xml`
3. `base/<module>-cache-config.yml`
4. `<env>/<module>-application.xml`
5. `<env>/<module>-infra.xml`
6. `<env>/<module>-cache-config.yml`

Later files override earlier files. This keeps base configuration stable while
allowing environment-specific folders to override only changed values.

## Cache Configuration

Cache configuration follows the same environment principle through
`<module>-cache-config.yml`.

Typical cache providers:

- `java`: in-memory Java cache implementation
- `none`: disables cache behavior

Cache implementation details belong in shared utility modules. Configuration
loading belongs in application configuration modules.

## Module Requirements

Runnable applications should:

- set the module name before application startup, or provide it through `MODULE`
- include resources under `base`, `local`, and any supported runtime
  environment folders
- keep secrets outside committed files
- provide secrets through environment variables or deployment configuration
