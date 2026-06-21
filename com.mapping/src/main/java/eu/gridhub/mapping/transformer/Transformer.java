package eu.gridhub.mapping.transformer;

import eu.gridhub.mapping.MappingConfiguration;
import eu.gridhub.mapping.MappingService;

/**
 * Common contract for transformer implementations.
 *
 * @param <T> primary transformed model type
 */
public interface Transformer<T> {
    MappingService mappingService();

    MappingConfiguration mappingConfiguration();
}
