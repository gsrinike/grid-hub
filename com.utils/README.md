# com.utils

`com.utils` contains generic utility code shared by multiple modules. The current implementation provides cache contracts under `eu.gridhub.utils.cache`, environment resolution under `eu.gridhub.utils.env`, and configuration loading under `eu.gridhub.utils.config`.

Configuration loading reads `CacheConfigurationService` from each module's cache configuration and creates the cache through `CacheServiceFactory`. The current provider values are `java` for the in-memory Java cache and `none` to disable caching without changing consuming code.
