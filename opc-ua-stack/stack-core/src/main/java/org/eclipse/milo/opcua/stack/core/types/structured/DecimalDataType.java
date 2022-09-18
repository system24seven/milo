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
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;

@EqualsAndHashCode(
    callSuper = false
)
@SuperBuilder
@ToString
public class DecimalDataType extends Structure implements UaStructuredType {
    public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=17861");

    public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=17863");

    public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=17862");

    public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15045");

    private final Short scale;

    private final ByteString value;

    public DecimalDataType(Short scale, ByteString value) {
        this.scale = scale;
        this.value = value;
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

    public Short getScale() {
        return scale;
    }

    public ByteString getValue() {
        return value;
    }

    public static StructureDefinition definition(NamespaceTable namespaceTable) {
        return new StructureDefinition(
            new NodeId(0, 17863),
            new NodeId(0, 22),
            StructureType.Structure,
            new StructureField[]{
                new StructureField("Scale", LocalizedText.NULL_VALUE, new NodeId(0, 4), -1, null, UInteger.valueOf(0), false),
                new StructureField("Value", LocalizedText.NULL_VALUE, new NodeId(0, 15), -1, null, UInteger.valueOf(0), false)
            }
        );
    }

    public static final class Codec extends GenericDataTypeCodec<DecimalDataType> {
        @Override
        public Class<DecimalDataType> getType() {
            return DecimalDataType.class;
        }

        @Override
        public DecimalDataType decodeType(EncodingContext context, UaDecoder decoder) {
            Short scale = decoder.decodeInt16("Scale");
            ByteString value = decoder.decodeByteString("Value");
            return new DecimalDataType(scale, value);
        }

        @Override
        public void encodeType(EncodingContext context, UaEncoder encoder, DecimalDataType value) {
            encoder.encodeInt16("Scale", value.getScale());
            encoder.encodeByteString("Value", value.getValue());
        }
    }
}