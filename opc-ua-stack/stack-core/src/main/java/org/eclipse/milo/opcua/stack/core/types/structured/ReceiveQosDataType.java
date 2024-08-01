/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.types.structured;

import java.util.StringJoiner;

import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;

/**
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/6.4.1/#6.4.1.1.5">https://reference.opcfoundation.org/v105/Core/docs/Part14/6.4.1/#6.4.1.1.5</a>
 */
public abstract class ReceiveQosDataType extends QosDataType implements UaStructuredType {
    public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=23608");

    public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=23860");

    public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=23928");

    public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=23996");

    public ReceiveQosDataType() {
    }

    @Override
    public ExpandedNodeId getTypeId() {
        return TYPE_ID;
    }

    @Override
    public ExpandedNodeId getBinaryEncodingId() {
        return BINARY_ENCODING_ID;
    }

    @Override
    public ExpandedNodeId getXmlEncodingId() {
        return XML_ENCODING_ID;
    }

    @Override
    public ExpandedNodeId getJsonEncodingId() {
        return JSON_ENCODING_ID;
    }

    @Override
    public String toString() {
        var joiner = new StringJoiner(", ", ReceiveQosDataType.class.getSimpleName() + "[", "]");
        return joiner.toString();
    }

    public static StructureDefinition definition(NamespaceTable namespaceTable) {
        return new StructureDefinition(
            new NodeId(0, 23860),
            new NodeId(0, 23603),
            StructureType.Structure,
            new StructureField[]{

            }
        );
    }
}