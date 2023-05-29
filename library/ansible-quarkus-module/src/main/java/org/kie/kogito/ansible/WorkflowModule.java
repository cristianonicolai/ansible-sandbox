package org.kie.kogito.ansible;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import org.kie.kogito.serverless.workflow.executor.StaticWorkflowApplication;

@ApplicationScoped
public class WorkflowModule {
    @Produces
    StaticWorkflowApplication application() {
        return StaticWorkflowApplication.create();
    }
}
