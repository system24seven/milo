/*
 * Copyright (c) 2022 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.types.structured;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.GenericDataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;

/**
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part4/6.3.2">https://reference.opcfoundation.org/v105/Core/docs/Part4/6.3.2</a>
 */
@EqualsAndHashCode(
    callSuper = false
)
@SuperBuilder
@ToString
public class SessionlessInvokeResponseType extends Structure implements UaStructuredType {
    public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=20999");

    public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=21001");

    public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=21000");

    public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15092");

    private final String[] namespaceUris;

    private final String[] serverUris;

    private final UInteger serviceId;

    public SessionlessInvokeResponseType(String[] namespaceUris, String[] serverUris,
                                         UInteger serviceId) {
        this.namespaceUris = namespaceUris;
        this.serverUris = serverUris;
        this.serviceId = serviceId;
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

    public String[] getNamespaceUris() {
        return namespaceUris;
    }

    public String[] getServerUris() {
        return serverUris;
    }

    public UInteger getServiceId() {
        return serviceId;
    }

    public static StructureDefinition definition(NamespaceTable namespaceTable) {
        return new StructureDefinition(
            new NodeId(0, 21001),
            new NodeId(0, 22),
            StructureType.Structure,
            new StructureField[]{
                new StructureField("NamespaceUris", LocalizedText.NULL_VALUE, new NodeId(0, 12), 1, null, UInteger.valueOf(0), false),
                new StructureField("ServerUris", LocalizedText.NULL_VALUE, new NodeId(0, 12), 1, null, UInteger.valueOf(0), false),
                new StructureField("ServiceId", LocalizedText.NULL_VALUE, new NodeId(0, 7), -1, null, UInteger.valueOf(0), false)
            }
        );
    }

    public static final class Codec extends GenericDataTypeCodec<SessionlessInvokeResponseType> {
        @Override
        public Class<SessionlessInvokeResponseType> getType() {
            return SessionlessInvokeResponseType.class;
        }

        @Override
        public SessionlessInvokeResponseType decodeType(EncodingContext context, UaDecoder decoder) {
            String[] namespaceUris = decoder.decodeStringArray("NamespaceUris");
            String[] serverUris = decoder.decodeStringArray("ServerUris");
            UInteger serviceId = decoder.decodeUInt32("ServiceId");
            return new SessionlessInvokeResponseType(namespaceUris, serverUris, serviceId);
        }

        @Override
        public void encodeType(EncodingContext context, UaEncoder encoder,
                               SessionlessInvokeResponseType value) {
            encoder.encodeStringArray("NamespaceUris", value.getNamespaceUris());
            encoder.encodeStringArray("ServerUris", value.getServerUris());
            encoder.encodeUInt32("ServiceId", value.getServiceId());
        }
    }
}