package org.kie.kogito.ansible.playbook;

import org.jbpm.ruleflow.core.RuleFlowNodeContainerFactory;
import org.jbpm.ruleflow.core.factory.WorkItemNodeFactory;
import org.kie.kogito.serverless.workflow.functions.WorkItemFunctionNamespace;
import org.kie.kogito.serverless.workflow.parser.ParserContext;

import io.serverlessworkflow.api.Workflow;
import io.serverlessworkflow.api.functions.FunctionRef;

import static org.kie.kogito.ansible.playbook.PlaybookWorkItemHandler.NAME;

public class PlaybookWorkItemFunctionNamespace extends WorkItemFunctionNamespace {

    @Override
    public String namespace() {
        return "ansible";
    }

    @Override
    protected <T extends RuleFlowNodeContainerFactory<T, ?>> WorkItemNodeFactory<T> fillWorkItemHandler(Workflow workflow,
            ParserContext context,
            WorkItemNodeFactory<T> node,
            FunctionRef functionRef) {
        return node.workName(NAME);//.metaData(OPERATION,  getFunctionName(functionRef));
    }

}
