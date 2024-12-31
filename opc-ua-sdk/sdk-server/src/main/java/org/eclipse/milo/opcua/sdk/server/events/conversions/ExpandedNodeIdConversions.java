/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.events.conversions;

import org.eclipse.milo.opcua.stack.core.BuiltinDataType;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class ExpandedNodeIdConversions {

  private ExpandedNodeIdConversions() {}

  @Nullable
  static NodeId expandedNodeIdToNodeId(@NotNull ExpandedNodeId e) {
    // TODO need a real NamespaceTable here
    return e.toNodeId(new NamespaceTable()).orElse(null);
  }

  @NotNull
  static String expandedNodeIdToString(@NotNull ExpandedNodeId e) {
    return e.toParseableString();
  }

  @Nullable
  static Object convert(@NotNull Object o, BuiltinDataType targetType, boolean implicit) {
    if (o instanceof ExpandedNodeId) {
      ExpandedNodeId eni = (ExpandedNodeId) o;

      return implicit ? implicitConversion(eni, targetType) : explicitConversion(eni, targetType);
    } else {
      return null;
    }
  }

  @Nullable
  static Object explicitConversion(@NotNull ExpandedNodeId eni, BuiltinDataType targetType) {
    // @formatter:off
    switch (targetType) {
      case NodeId:
        return expandedNodeIdToNodeId(eni);
      default:
        return implicitConversion(eni, targetType);
    }
    // @formatter:on
  }

  @Nullable
  static Object implicitConversion(@NotNull ExpandedNodeId eni, BuiltinDataType targetType) {
    // @formatter:off
    switch (targetType) {
      case String:
        return expandedNodeIdToString(eni);
      default:
        return null;
    }
    // @formatter:on
  }
}
