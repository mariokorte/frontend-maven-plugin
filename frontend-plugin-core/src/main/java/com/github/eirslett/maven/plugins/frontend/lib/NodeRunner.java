package com.github.eirslett.maven.plugins.frontend.lib;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.eirslett.maven.plugins.frontend.lib.Utils.prepend;

public interface NodeRunner  extends NodeTaskRunner {}

final class DefaultNodeRunner extends NodeTaskExecutor implements NodeRunner {
    private static final String TASK_LOCATION = "";

    DefaultNodeRunner(NodeExecutorConfig config) {
        super(config, TASK_LOCATION);
    }

    @Override
    public void execute(String args, Map<String, String> environment) throws TaskRunnerException {
        final List<String> arguments = getArguments(args);
        logger.info("Running " + taskToString(taskName, arguments) + " in " + config.getWorkingDirectory());

        try {
            Map<String, String> internalEnvironment = new HashMap<>();
            if (environment != null && !environment.isEmpty()) {
                internalEnvironment.putAll(environment);
            }
            if (!proxy.isEmpty()) {
                internalEnvironment.putAll(proxy);
            }
            final int result = new NodeExecutor(config, arguments, internalEnvironment ).executeAndRedirectOutput(logger);
            if (result != 0) {
                throw new TaskRunnerException(taskToString(taskName, arguments) + " failed. (error code " + result + ")");
            }
        } catch (ProcessExecutionException e) {
            throw new TaskRunnerException(taskToString(taskName, arguments) + " failed.", e);
        }
    }
}
