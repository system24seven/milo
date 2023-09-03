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
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/12.2.12/#12.2.12.9">https://reference.opcfoundation.org/v105/Core/docs/Part5/12.2.12/#12.2.12.9</a>
 */
@EqualsAndHashCode(
    callSuper = false
)
@SuperBuilder
@ToString
public class RolePermissionType extends Structure implements UaStructuredType {
    public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=96");

    public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=128");

    public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=16126");

    public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15062");

    private final NodeId roleId;

    private final PermissionType permissions;

    public RolePermissionType(NodeId roleId, PermissionType permissions) {
        this.roleId = roleId;
        this.permissions = permissions;
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

    public NodeId getRoleId() {
        return roleId;
    }

    public PermissionType getPermissions() {
        return permissions;
    }

    public static StructureDefinition definition(NamespaceTable namespaceTable) {
        return new StructureDefinition(
            new NodeId(0, 128),
            new NodeId(0, 22),
            StructureType.Structure,
            new StructureField[]{
                new StructureField("RoleId", LocalizedText.NULL_VALUE, new NodeId(0, 17), -1, null, UInteger.valueOf(0), false),
                new StructureField("Permissions", LocalizedText.NULL_VALUE, new NodeId(0, 94), -1, null, UInteger.valueOf(0), false)
            }
        );
    }

    public static final class Codec extends GenericDataTypeCodec<RolePermissionType> {
        @Override
        public Class<RolePermissionType> getType() {
            return RolePermissionType.class;
        }

        @Override
        public RolePermissionType decodeType(EncodingContext context, UaDecoder decoder) {
            NodeId roleId = decoder.decodeNodeId("RoleId");
            PermissionType permissions = new PermissionType(decoder.decodeUInt32("Permissions"));
            return new RolePermissionType(roleId, permissions);
        }

        @Override
        public void encodeType(EncodingContext context, UaEncoder encoder, RolePermissionType value) {
            encoder.encodeNodeId("RoleId", value.getRoleId());
            encoder.encodeUInt32("Permissions", value.getPermissions().getValue());
        }
    }
}