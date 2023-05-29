/*
 * Copyright 2022 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kie.kogito.ansible.playbook;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.kie.kogito.internal.process.runtime.KogitoWorkItem;
import org.kie.kogito.serverless.workflow.WorkflowWorkItemHandler;

import static java.util.Arrays.asList;

public class PlaybookWorkItemHandler extends WorkflowWorkItemHandler {

    public static final String NAME = "PlaybookWorkItemHandler";

    @Override
    protected Object internalExecute(KogitoWorkItem workItem, Map<String, Object> parameters) {
        System.out.println("execute WIH");
        System.out.println("workItem = " + workItem);
        System.out.println("parameters = " + parameters);
        String playbook = (String) parameters.get("name");
        System.out.println("playbook = " + playbook);
        List<String> stdout = PlaybookExecutor.newExecutor().executeCommand(asList("ansible-navigator", "run", playbook, "-i", "inventory-ee.yaml", "-m", "stdout"));
        stdout.forEach(System.out::println);
        return Collections.singletonMap("playbook_run", "true");
    }

    @Override
    public String getName() {
        return NAME;
    }
}
