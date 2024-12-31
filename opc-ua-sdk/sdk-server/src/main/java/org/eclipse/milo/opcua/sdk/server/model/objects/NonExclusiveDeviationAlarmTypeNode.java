/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.objects;

import java.util.Optional;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class NonExclusiveDeviationAlarmTypeNode extends NonExclusiveLimitAlarmTypeNode
    implements NonExclusiveDeviationAlarmType {
  public NonExclusiveDeviationAlarmTypeNode(
      UaNodeContext context,
      NodeId nodeId,
      QualifiedName browseName,
      LocalizedText displayName,
      LocalizedText description,
      UInteger writeMask,
      UInteger userWriteMask,
      RolePermissionType[] rolePermissions,
      RolePermissionType[] userRolePermissions,
      AccessRestrictionType accessRestrictions,
      UByte eventNotifier) {
    super(
        context,
        nodeId,
        browseName,
        displayName,
        description,
        writeMask,
        userWriteMask,
        rolePermissions,
        userRolePermissions,
        accessRestrictions,
        eventNotifier);
  }

  public NonExclusiveDeviationAlarmTypeNode(
      UaNodeContext context,
      NodeId nodeId,
      QualifiedName browseName,
      LocalizedText displayName,
      LocalizedText description,
      UInteger writeMask,
      UInteger userWriteMask,
      RolePermissionType[] rolePermissions,
      RolePermissionType[] userRolePermissions,
      AccessRestrictionType accessRestrictions) {
    super(
        context,
        nodeId,
        browseName,
        displayName,
        description,
        writeMask,
        userWriteMask,
        rolePermissions,
        userRolePermissions,
        accessRestrictions);
  }

  @Override
  public PropertyTypeNode getSetpointNodeNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(NonExclusiveDeviationAlarmType.SETPOINT_NODE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public NodeId getSetpointNode() {
    return getProperty(NonExclusiveDeviationAlarmType.SETPOINT_NODE).orElse(null);
  }

  @Override
  public void setSetpointNode(NodeId value) {
    setProperty(NonExclusiveDeviationAlarmType.SETPOINT_NODE, value);
  }

  @Override
  public PropertyTypeNode getBaseSetpointNodeNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(NonExclusiveDeviationAlarmType.BASE_SETPOINT_NODE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public NodeId getBaseSetpointNode() {
    return getProperty(NonExclusiveDeviationAlarmType.BASE_SETPOINT_NODE).orElse(null);
  }

  @Override
  public void setBaseSetpointNode(NodeId value) {
    setProperty(NonExclusiveDeviationAlarmType.BASE_SETPOINT_NODE, value);
  }
}
