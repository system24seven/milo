/*
 * Copyright (c) 2022 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

import org.eclipse.milo.opcua.sdk.core.types.DataType;
import org.eclipse.milo.opcua.sdk.core.types.DataTypeTree;
import org.eclipse.milo.opcua.stack.client.UaStackClient;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.serialization.UaResponseMessage;
import org.eclipse.milo.opcua.stack.core.types.OpcUaDefaultBinaryEncoding;
import org.eclipse.milo.opcua.stack.core.types.OpcUaDefaultJsonEncoding;
import org.eclipse.milo.opcua.stack.core.types.OpcUaDefaultXmlEncoding;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseDirection;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseResultMask;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.DataTypeDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.eclipse.milo.opcua.stack.core.util.FutureUtils;
import org.eclipse.milo.opcua.stack.core.util.Tree;
import org.eclipse.milo.opcua.stack.core.util.Unit;
import org.jetbrains.annotations.Nullable;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

/**
 * Builds a {@link DataTypeTree} by recursively browsing the DataType hierarchy starting at
 * {@link NodeIds#BaseDataType}.
 */
public final class DataTypeTreeBuilder {

    private DataTypeTreeBuilder() {}

    /**
     * Build a {@link DataTypeTree} by recursively browsing the DataType hierarchy starting at
     * {@link NodeIds#BaseDataType}.
     *
     * @param client a connected {@link OpcUaClient}.
     * @return a {@link DataTypeTree}.
     * @throws UaException if an unrecoverable error occurs while building the tree.
     */
    public static DataTypeTree build(OpcUaClient client) throws UaException {
        try {
            return buildAsync(client).get();
        } catch (InterruptedException e) {
            throw new UaException(StatusCodes.Bad_UnexpectedError, e);
        } catch (ExecutionException e) {
            throw UaException.extract(e)
                .orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    /**
     * Build a {@link DataTypeTree} by recursively browsing the DataType hierarchy starting at
     * {@link NodeIds#BaseDataType}.
     *
     * @param client  a connected {@link UaStackClient}.
     * @param session an active {@link OpcUaSession}.
     * @return a {@link DataTypeTree}.
     * @throws UaException if an unrecoverable error occurs while building the tree.
     */
    public static DataTypeTree build(UaStackClient client, OpcUaSession session) throws UaException {
        try {
            return buildAsync(client, session).get();
        } catch (InterruptedException e) {
            throw new UaException(StatusCodes.Bad_UnexpectedError, e);
        } catch (ExecutionException e) {
            throw UaException.extract(e)
                .orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
        }
    }

    /**
     * Build a {@link DataTypeTree} by recursively browsing the DataType hierarchy starting at
     * {@link NodeIds#BaseDataType}.
     *
     * @param client a connected {@link OpcUaClient}.
     * @return a {@link DataTypeTree}.
     */
    public static CompletableFuture<DataTypeTree> buildAsync(OpcUaClient client) {
        return client.getSession().thenCompose(
            session ->
                buildAsync(client.getStackClient(), session)
        );
    }

    /**
     * Build a {@link DataTypeTree} by recursively browsing the DataType hierarchy starting at
     * {@link NodeIds#BaseDataType}.
     *
     * @param client  a connected {@link UaStackClient}.
     * @param session an active {@link OpcUaSession}.
     * @return a {@link DataTypeTree}.
     */
    public static CompletableFuture<DataTypeTree> buildAsync(UaStackClient client, OpcUaSession session) {
        Tree<DataType> root = new Tree<>(
            null,
            new DataType(
                QualifiedName.parse("0:BaseDataType"),
                NodeIds.BaseDataType,
                null,
                null,
                null,
                null
            )
        );

        return readNamespaceTable(client, session)
            .thenCompose(namespaceTable -> addChildren(root, client, session, namespaceTable))
            .thenApply(u -> new DataTypeTree(root));
    }

    private static CompletableFuture<NamespaceTable> readNamespaceTable(UaStackClient client, OpcUaSession session) {
        RequestHeader requestHeader = client.newRequestHeader(
            session.getAuthenticationToken(),
            client.getConfig().getRequestTimeout()
        );

        CompletableFuture<UaResponseMessage> readFuture = client.sendRequest(
            new ReadRequest(
                requestHeader,
                0.0,
                TimestampsToReturn.Neither,
                new ReadValueId[]{
                    new ReadValueId(
                        NodeIds.Server_NamespaceArray,
                        AttributeId.Value.uid(),
                        null,
                        QualifiedName.NULL_VALUE)}
            )
        );

        return readFuture.thenApply(ReadResponse.class::cast).thenApply(response -> {
            DataValue dataValue = response.getResults()[0];
            String[] namespaceUris = (String[]) dataValue.getValue().getValue();
            NamespaceTable namespaceTable = new NamespaceTable();
            for (String namespaceUri : namespaceUris) {
                namespaceTable.add(namespaceUri);
            }
            return namespaceTable;
        });
    }

    private static CompletableFuture<Unit> addChildren(
        Tree<DataType> tree,
        UaStackClient client,
        OpcUaSession session,
        NamespaceTable namespaceTable
    ) {

        CompletableFuture<List<ReferenceDescription>> subtypes = browseSafe(
            client,
            session,
            new BrowseDescription(
                tree.getValue().getNodeId(),
                BrowseDirection.Forward,
                NodeIds.HasSubtype,
                false,
                uint(NodeClass.DataType.getValue()),
                uint(BrowseResultMask.All.getValue())
            )
        );

        CompletableFuture<List<DataType>> dataTypesFuture = subtypes.thenCompose(references -> {
            Stream<CompletableFuture<DataType>> dataTypeFutures =
                references.stream().map(dataTypeReference -> {
                    NodeId dataTypeId = dataTypeReference.getNodeId()
                        .toNodeId(namespaceTable)
                        .orElse(NodeId.NULL_VALUE);

                    CompletableFuture<List<ReferenceDescription>> encodingsFuture = browseSafe(
                        client,
                        session,
                        new BrowseDescription(
                            dataTypeId,
                            BrowseDirection.Forward,
                            NodeIds.HasEncoding,
                            false,
                            uint(NodeClass.Object.getValue()),
                            uint(BrowseResultMask.All.getValue())
                        )
                    );

                    CompletableFuture<DataTypeDefinition> dataTypeDefinitionFuture =
                        readDataTypeDefinition(client, session, dataTypeId);

                    return encodingsFuture.thenCombine(
                        dataTypeDefinitionFuture,
                        (encodingReferences, dataTypeDefinition) -> {
                            NodeId binaryEncodingId = null;
                            NodeId xmlEncodingId = null;
                            NodeId jsonEncodingId = null;

                            for (ReferenceDescription r : encodingReferences) {
                                if (r.getBrowseName().equals(OpcUaDefaultBinaryEncoding.ENCODING_NAME)) {
                                    binaryEncodingId = r.getNodeId().toNodeId(namespaceTable).orElse(null);
                                } else if (r.getBrowseName().equals(OpcUaDefaultXmlEncoding.ENCODING_NAME)) {
                                    xmlEncodingId = r.getNodeId().toNodeId(namespaceTable).orElse(null);
                                } else if (r.getBrowseName().equals(OpcUaDefaultJsonEncoding.ENCODING_NAME)) {
                                    jsonEncodingId = r.getNodeId().toNodeId(namespaceTable).orElse(null);
                                }
                            }

                            return new DataType(
                                dataTypeReference.getBrowseName(),
                                dataTypeId,
                                binaryEncodingId,
                                xmlEncodingId,
                                jsonEncodingId,
                                dataTypeDefinition
                            );
                        }
                    );
                });

            return FutureUtils.sequence(dataTypeFutures);
        });

        return dataTypesFuture
            .thenCompose(dataTypes -> {
                Stream<CompletableFuture<Unit>> futures = dataTypes.stream()
                    .map(tree::addChild)
                    .map(childNode -> addChildren(childNode, client, session, namespaceTable));

                return FutureUtils.sequence(futures);
            })
            .thenApply(v -> Unit.VALUE);
    }

    /**
     * Browse a {@link BrowseDescription} "safely", completing successfully
     * with an empty List if any exceptions occur.
     *
     * @param client            a {@link UaStackClient}.
     * @param session           an {@link OpcUaSession}.
     * @param browseDescription the {@link BrowseDescription}.
     * @return a List of {@link ReferenceDescription}s obtained by browsing {@code browseDescription}.
     */
    private static CompletableFuture<List<ReferenceDescription>> browseSafe(
        UaStackClient client,
        OpcUaSession session,
        BrowseDescription browseDescription
    ) {

        return BrowseHelper.browse(client, session, browseDescription, uint(0))
            .exceptionally(ex -> Collections.emptyList());
    }

    /**
     * Read the DataTypeDefinition attribute for the DataType Node identified by {@code nodeId}.
     *
     * @param client     a {@link UaStackClient}.
     * @param session    an {@link OpcUaSession}.
     * @param dataTypeId the {@link NodeId} of the DataType node.
     * @return the value of the {@link DataTypeDefinition} attribute for the Node identified by
     * {@code dataTypeId}. May be {@code null}.
     */
    private static CompletableFuture<@Nullable DataTypeDefinition> readDataTypeDefinition(
        UaStackClient client,
        OpcUaSession session,
        NodeId dataTypeId
    ) {

        var request = new ReadRequest(
            client.newRequestHeader(
                session.getAuthenticationToken(),
                client.getConfig().getRequestTimeout()
            ),
            0.0,
            TimestampsToReturn.Neither,
            new ReadValueId[]{
                new ReadValueId(
                    dataTypeId,
                    AttributeId.DataTypeDefinition.uid(),
                    null,
                    QualifiedName.NULL_VALUE
                )
            }
        );

        return client.sendRequest(request).thenApply(ReadResponse.class::cast).thenApply(response -> {
            DataValue value = response.getResults()[0];

            if (value.getStatusCode() != null && value.getStatusCode().isGood()) {
                Object o = value.getValue().getValue();
                if (o instanceof ExtensionObject) {
                    Object decoded = ((ExtensionObject) o).decode(
                        client.getStaticSerializationContext()
                    );

                    return (DataTypeDefinition) decoded;
                } else {
                    return null;
                }
            } else {
                // OPC UA 1.03 and prior servers will return Bad_AttributeIdInvalid
                return null;
            }
        });
    }

}
