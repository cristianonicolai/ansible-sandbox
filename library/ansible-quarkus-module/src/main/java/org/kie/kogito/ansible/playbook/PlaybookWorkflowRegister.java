package org.kie.kogito.ansible.playbook;

import org.kie.kogito.serverless.workflow.executor.StaticWorkflowApplication;
import org.kie.kogito.serverless.workflow.executor.StaticWorkflowRegister;

import io.serverlessworkflow.api.Workflow;

public class PlaybookWorkflowRegister implements StaticWorkflowRegister {

    @Override
    public void register(StaticWorkflowApplication application, Workflow workflow) {
        application.registerHandler(new PlaybookWorkItemHandler());
    }

}
