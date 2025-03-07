/*
 * Copyright 2020-Present The Serverless Workflow Specification Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package events;

import io.serverlessworkflow.api.WorkflowReader;
import com.networknt.workflow.WorkflowApplication;
import com.networknt.workflow.WorkflowDefinition;
import com.networknt.workflow.WorkflowInstance;
import java.io.IOException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventExample {

  private static final Logger logger = LoggerFactory.getLogger(EventExample.class);

  public static void main(String[] args) throws IOException {
    try (WorkflowApplication appl = WorkflowApplication.builder().build()) {
      WorkflowDefinition listenDefinition =
          appl.workflowDefinition(WorkflowReader.readWorkflowFromClasspath("listen.yaml"));
      WorkflowDefinition emitDefinition =
          appl.workflowDefinition(WorkflowReader.readWorkflowFromClasspath("emit.yaml"));
      WorkflowInstance waitingInstance = listenDefinition.instance(Map.of());
      waitingInstance
          .start()
          .thenAccept(node -> logger.info("Waiting instance completed with result {}", node));
      logger.info("Listen instance waiting for proper event, Status {}", waitingInstance.status());
      logger.info("Publishing event with temperature 35");
      emitDefinition.instance(Map.of("temperature", 35)).start().join();
      logger.info(
          "Listen instance still waiting for proper event, Status {}", waitingInstance.status());
      logger.info("Publishing event with temperature 39");
      emitDefinition.instance(Map.of("temperature", 39)).start().join();
    }
  }
}
