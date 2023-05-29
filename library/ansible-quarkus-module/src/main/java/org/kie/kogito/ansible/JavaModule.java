package org.kie.kogito.ansible;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.inject.Inject;

import org.kie.kogito.serverless.workflow.executor.StaticWorkflowApplication;
import org.kie.kogito.serverless.workflow.fluent.WorkflowBuilder;
import org.kie.kogito.serverless.workflow.models.JsonNodeModel;
import org.kie.kogito.serverless.workflow.utils.ServerlessWorkflowUtils;
import org.kie.kogito.serverless.workflow.utils.WorkflowFormat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import io.serverlessworkflow.api.Workflow;

import static org.kie.kogito.serverless.workflow.fluent.StateBuilder.inject;
import static org.kie.kogito.serverless.workflow.fluent.WorkflowBuilder.jsonObject;

@QuarkusMain
public class JavaModule implements QuarkusApplication {

    @Inject
    ObjectMapper objectMapper;

    @Inject
    StaticWorkflowApplication application;

    @Override
    public int run(String... args) throws Exception {
        if (args.length < 1) {
            output(new Response("no arguments from java", null));
            return 1;
        }

        try {
            //Ansible send the input parameters as a json which is stored in the filesystem
            //The input parameter arg[0] is the URI of this Json
            String inputFilePath = args[0];
            JsonNode input = objectMapper.readTree(getFile(inputFilePath));

            JsonNode definition = input.get("definition");
            Workflow workflow = buildWorkflow(definition.asText());
            JsonNodeModel workflowResponse = application.execute(workflow, input);
            output(new Response("Response from java module", workflowResponse.getWorkflowdata()));
            return 0;
        } catch (Exception e) {
            output(new Response(e.getMessage(), null));
            return 1;
        }
    }

    private static File getFile(String inputFilePath) {
        File file = Paths.get(inputFilePath).toFile();
        if (file.exists()) {
            return file;
        } else {
            URL resource = Thread.currentThread().getContextClassLoader().getResource(inputFilePath);
            if (resource == null) {
                return file;
            } else {
                try {
                    return new File(resource.toURI());
                } catch (URISyntaxException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        }
    }

    public Workflow buildWorkflow(String definitionFilePath) {
        File workflow = getFile(definitionFilePath);
        if (!workflow.exists()) {
            throw new IllegalArgumentException("Workflow file not found: " + definitionFilePath);
        }
        try (Reader reader = Files.newBufferedReader(workflow.toPath())) {
            return ServerlessWorkflowUtils.getWorkflow(reader, WorkflowFormat.JSON);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private void output(Response response) {
        try {
            //ansible receives the response from the console output as a Json
            System.out.println(objectMapper.writeValueAsString(response));
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }
}
