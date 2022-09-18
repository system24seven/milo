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

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.eclipse.milo.opcua.stack.core.types.builtin.OptionSetUI32;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

/**
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.3">https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.3</a>
 */
@EqualsAndHashCode(
    callSuper = true
)
@ToString
public class UserConfigurationMask extends OptionSetUI32<UserConfigurationMask.Field> {
    public UserConfigurationMask(UInteger value) {
        super(value);
    }

    public boolean getNoDelete() {
        return get(Field.NoDelete);
    }

    public boolean getDisabled() {
        return get(Field.Disabled);
    }

    public boolean getNoChangeByUser() {
        return get(Field.NoChangeByUser);
    }

    public boolean getMustChangePassword() {
        return get(Field.MustChangePassword);
    }

    @Override
    public UInteger getValue() {
        return (UInteger) value;
    }

    @Override
    public Set<Field> toSet() {
        return Arrays.stream(Field.values())
            .filter(this::get)
            .collect(Collectors.toSet());
    }

    public static UserConfigurationMask of(Field... fields) {
        long bits = 0L;

        for (Field f : fields) {
            bits |= (1L << f.bitIndex);
        }

        return new UserConfigurationMask(UInteger.valueOf(bits));
    }

    public enum Field implements BitIndex {
        NoDelete(0),

        Disabled(1),

        NoChangeByUser(2),

        MustChangePassword(3);

        private final int bitIndex;

        Field(int bitIndex) {
            this.bitIndex = bitIndex;
        }

        @Override
        public int getBitIndex() {
            return bitIndex;
        }
    }
}