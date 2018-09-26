package camunda.community.services;

import lombok.Getter;
import lombok.Setter;
import org.camunda.bpm.engine.ManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

/**
 * Configuration Service
 *
 * This service provides the process instances with a common configuration properties.
 */
@Service
@Getter
@Setter
public class ConfigurationService {

    // Camunda Management Service
    @Autowired
    private ManagementService managementService;

    // Spring Environment
    @Autowired
    private Environment env;

    /**
     * Constructor
     */
    public ConfigurationService() {
        // nothing
    }

    /**
     * Gets a configuration property
     *
     * @param propertyName The name of the property.
     * @return Text value of the property.
     */
    public Optional<String> getProperty(String propertyName) {
        // restrict access to system variables
        if (isProtectedProperty(propertyName)) {
            return Optional.empty();
        }

        // get property from camunda
        if (managementService.getProperties().containsKey(propertyName)) {
            return Optional.ofNullable(managementService.getProperties().get(propertyName));
        }

        // get from spring properties
        if (env.containsProperty(propertyName)) {
            return Optional.ofNullable(env.getProperty(propertyName));
        }

        return Optional.empty();
    }

    /**
     * Sets a configuration property
     *
     * @param propertyName The name of the property.
     */
    public void setProperty(String propertyName, String propertyValue) {
        // restrict access to system variables
        if(isProtectedProperty(propertyName)) {
            throw new RuntimeException("Can't set protected properties! [" + propertyName + "]");
        }

        // set property using the camunda management service
        managementService.setProperty(propertyName, propertyValue);
    }

    /**
     * Fills all properties formattted as ${propertyName} within a string, throws a exception if it still contains unreplaced tokens.
     *
     * @param value the text in which the properties should be replaced
     * @return a string where all properties are replaced with their values if present
     */
    public String fillProperties(String value) {
        // replace ${varName}
        for (Map.Entry<String, String> property : managementService.getProperties().entrySet()) {
            value = value.replace(String.format("${%s}", property.getKey()), property.getValue());
        }
        // replace {{varName}}, can be useful if juel expression can't be used
        for (Map.Entry<String, String> property : managementService.getProperties().entrySet()) {
            value = value.replace(String.format("{{%s}}", property.getKey()), property.getValue());
        }

        if (value.contains("${")) {
            throw new RuntimeException(String.format("Can't replace all variables in string []!", value));
        }

        return value;
    }

    /**
     * Checks if the property is protected
     *
     * @param propertyName
     * @return
     */
    private Boolean isProtectedProperty(String propertyName) {
        if(propertyName.equals("next.dbid")
                || propertyName.equals("historyLevel")
                || propertyName.equals("deployment.lock")
                || propertyName.equals("schema.history")
                || propertyName.equals("history.cleanup.job.lock")
                || propertyName.equals("schema.version")
                || propertyName.equals("startup.lock")) {
            return true;
        }

        return false;
    }

}
