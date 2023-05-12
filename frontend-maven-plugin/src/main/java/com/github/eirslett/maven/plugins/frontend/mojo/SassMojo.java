package com.github.eirslett.maven.plugins.frontend.mojo;

import com.github.eirslett.maven.plugins.frontend.lib.FrontendPluginFactory;
import com.github.eirslett.maven.plugins.frontend.lib.TaskRunnerException;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.sonatype.plexus.build.incremental.BuildContext;

@Mojo(name = "sass", defaultPhase = LifecyclePhase.GENERATE_RESOURCES, threadSafe = true)
public final class SassMojo extends AbstractFrontendMojo {

    /**
     * sass arguments. Default is empty (runs just the "sass" command).
     */
    @Parameter(property = "frontend.sass.arguments")
    private String arguments;

    /**
     * Files that should be checked for changes, in addition to the srcdir files.
     * Defaults to sassfile.js in the {@link #workingDirectory}.
     */
    @Parameter(property = "triggerfiles")
    private List<File> triggerfiles;

    /**
     * The directory containing front end files that will be processed by sass.
     * If this is set then files in the directory will be checked for
     * modifications before running sass.
     */
    @Parameter(property = "srcdir")
    private File srcdir;

    /**
     * The directory where front end files will be output by sass. If this is
     * set then they will be refreshed so they correctly show as modified in
     * Eclipse.
     */
    @Parameter(property = "outputdir")
    private File outputdir;

    /**
     * Skips execution of this mojo.
     */
    @Parameter(property = "skip.sass", defaultValue = "${skip.sass}")
    private boolean skip;

    @Component
    private BuildContext buildContext;

    @Override
    protected boolean skipExecution() {
        return this.skip;
    }

    @Override
    public synchronized void execute(FrontendPluginFactory factory) throws TaskRunnerException {
        if (shouldExecute()) {
            factory.getSassRunner().execute(arguments, environmentVariables);

            if (outputdir != null) {
                getLog().info("Refreshing files after sass: " + outputdir);
                buildContext.refresh(outputdir);
            }
        } else {
            getLog().info("Skipping sass as no modified files in " + srcdir);
        }
    }

    private boolean shouldExecute() {
        if (triggerfiles == null || triggerfiles.isEmpty()) {
            triggerfiles = Arrays.asList(new File(workingDirectory, "sassfile.js"));
        }

        return MojoUtils.shouldExecute(buildContext, triggerfiles, srcdir);
    }

}
