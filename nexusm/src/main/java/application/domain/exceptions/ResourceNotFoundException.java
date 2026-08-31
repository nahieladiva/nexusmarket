package application.domain.exceptions;

/**
 * Se lanza cuando un recurso solicitado no existe.
 */
public class ResourceNotFoundException extends DomainException {

    private final String resourceType;
    private final String resourceId;

    public ResourceNotFoundException(String resourceType, String resourceId) {
        super(resourceType + " no encontrado con id: " + resourceId);
        this.resourceType = resourceType;
        this.resourceId = resourceId;
    }

    public String getResourceType() {
        return resourceType;
    }

    public String getResourceId() {
        return resourceId;
    }
}