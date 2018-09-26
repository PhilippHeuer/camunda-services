package camunda.community.services;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.runtime.Execution;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LoggingService {

    /**
     * Log trace messages
     *
     * @param execution execution or null for system messages
     * @param message log message
     */
	public void trace(Execution execution, String message) {
        String[] serviceName = Thread.currentThread().getStackTrace()[2].getClassName().split("\\.");
        log.trace(renderMessage(execution == null ? "SYSTEM" : execution.getProcessInstanceId(), serviceName[serviceName.length - 1], message));
	}

    /**
     * Log debug messages
     *
     * @param execution execution or null for system messages
     * @param message log message
     */
	public void debug(Execution execution, String message) {
        String[] serviceName = Thread.currentThread().getStackTrace()[2].getClassName().split("\\.");
        log.debug(renderMessage(execution == null ? "SYSTEM" : execution.getProcessInstanceId(), serviceName[serviceName.length - 1], message));
	}

    /**
     * Log info messages
     *
     * @param execution execution or null for system messages
     * @param message log message
     */
	public void info(Execution execution, String message) {
        String[] serviceName = Thread.currentThread().getStackTrace()[2].getClassName().split("\\.");
        log.info(renderMessage(execution == null ? "SYSTEM" : execution.getProcessInstanceId(), serviceName[serviceName.length - 1], message));
	}

    /**
     * Log warn messages
     *
     * @param execution execution or null for system messages
     * @param message log message
     */
	public void warn(Execution execution, String message) {
        String[] serviceName = Thread.currentThread().getStackTrace()[2].getClassName().split("\\.");
        log.warn(renderMessage(execution == null ? "SYSTEM" : execution.getProcessInstanceId(), serviceName[serviceName.length - 1], message));
	}

    /**
     * Log error messages
     *
     * @param execution execution or null for system messages
     * @param message log message
     */
	public void error(Execution execution, String message) {
        String[] serviceName = Thread.currentThread().getStackTrace()[2].getClassName().split("\\.");
        log.error(renderMessage(execution == null ? "SYSTEM" : execution.getProcessInstanceId(), serviceName[serviceName.length - 1], message));
	}

	private String renderMessage(String origin, String processService, String message) {
		return String.format("[%s][Service:%s] - %s", origin, processService, message);
	}

}
