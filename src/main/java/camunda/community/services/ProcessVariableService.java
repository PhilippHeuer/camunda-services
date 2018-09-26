package camunda.community.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Service;

/**
 * Helper service to work with camunda variables
 */
@Service
public class ProcessVariableService {

    /**
     * Constructor
     */
    public ProcessVariableService() {
        // nothing
    }

    /**
     * Sets a variable
     */
    public void setVariable(DelegateExecution execution, String variableName, Object variableValue) {
        // string 4000 char check
        if (variableValue instanceof String) {
            if (((String) variableValue).length() > 4000 ) {
                throw new RuntimeException("[" + execution.getId() + "] - Can't write more than 4000 chars as string for variable " + variableName);
            }
        }

        // set variable
        execution.setVariable(variableName, variableValue);
    }

    /**
     * Get variable (typed)
     *
     * @param variableName name of the variable
     * @return typed variable
     */
    public <T extends Object> T getVariable(DelegateExecution execution, String variableName, Class<T> returnType) {
        Object variableValue = execution.getVariable(variableName);
        return returnType.cast(variableValue);
    }
}
