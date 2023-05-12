package com.github.eirslett.maven.plugins.frontend.lib;

public interface SassRunner  extends NodeTaskRunner {}

final class DefaultSassRunner extends NodeTaskExecutor implements SassRunner {
    private static final String TASK_LOCATION = "node_modules/sass/sass.js";

    DefaultSassRunner(NodeExecutorConfig config) {
        super(config, TASK_LOCATION);
    }
}
