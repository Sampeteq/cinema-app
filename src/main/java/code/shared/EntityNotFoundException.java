package code.shared;

import org.apache.commons.lang3.StringUtils;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String entityName) {
        super(StringUtils.capitalize(entityName) + " not found");
    }
}
