package eu.gridhub.infra;

import eu.gridhub.infra.document.DocumentAdapter;
import eu.gridhub.infra.document.DocumentRepositoryService;
import eu.gridhub.infra.event.EventPublisherService;
import eu.gridhub.infra.storage.ObjectStorageService;

/**
 * Factory boundary used by service modules to obtain infrastructure adapters.
 *
 * The consumer supplies application-specific adapters where needed, and the
 * utility module chooses the concrete technology implementation.
 */
public interface InfrastructureAdapterFactory {
    <T> DocumentRepositoryService<T> documentRepository(DocumentAdapter<T> adapter);

    ObjectStorageService objectStorageService();

    EventPublisherService eventPublisher();
}
