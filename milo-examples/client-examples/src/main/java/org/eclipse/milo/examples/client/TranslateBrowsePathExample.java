/*
 * Copyright (c) 2022 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.examples.client;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowsePath;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowsePathResult;
import org.eclipse.milo.opcua.stack.core.types.structured.RelativePath;
import org.eclipse.milo.opcua.stack.core.types.structured.RelativePathElement;
import org.eclipse.milo.opcua.stack.core.types.structured.TranslateBrowsePathsToNodeIdsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TranslateBrowsePathExample implements ClientExample {

    public static void main(String[] args) throws Exception {
        TranslateBrowsePathExample example = new TranslateBrowsePathExample();

        new ClientExampleRunner(example).run();
    }

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void run(OpcUaClient client, CompletableFuture<OpcUaClient> future) throws Exception {
        // synchronous connect
        client.connect().get();

        TranslateBrowsePathsToNodeIdsResponse response = client.translateBrowsePaths(List.of(new BrowsePath(
            NodeIds.ObjectsFolder,
            new RelativePath(new RelativePathElement[]{
                new RelativePathElement(
                    NodeIds.HierarchicalReferences,
                    false,
                    true,
                    new QualifiedName(2, "HelloWorld")
                ),
                new RelativePathElement(
                    NodeIds.HierarchicalReferences,
                    false,
                    true,
                    new QualifiedName(2, "ScalarTypes")
                ),
                new RelativePathElement(
                    NodeIds.HierarchicalReferences,
                    false,
                    true,
                    new QualifiedName(2, "UInt64")
                )
            })
        ))).get();

        BrowsePathResult result = List.of(response.getResults()).get(0);
        StatusCode statusCode = result.getStatusCode();
        logger.info("Status={}", statusCode);

        List.of(result.getTargets()).forEach(target -> logger.info("TargetId={}", target.getTargetId()));

        future.complete(client);
    }

}
