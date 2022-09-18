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
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/6.3.1/#6.3.1.3.2">https://reference.opcfoundation.org/v105/Core/docs/Part14/6.3.1/#6.3.1.3.2</a>
 */
@EqualsAndHashCode(
    callSuper = true
)
@ToString
public class UadpDataSetMessageContentMask extends OptionSetUI32<UadpDataSetMessageContentMask.Field> {
    public UadpDataSetMessageContentMask(UInteger value) {
        super(value);
    }

    public boolean getTimestamp() {
        return get(Field.Timestamp);
    }

    public boolean getPicoSeconds() {
        return get(Field.PicoSeconds);
    }

    public boolean getStatus() {
        return get(Field.Status);
    }

    public boolean getMajorVersion() {
        return get(Field.MajorVersion);
    }

    public boolean getMinorVersion() {
        return get(Field.MinorVersion);
    }

    public boolean getSequenceNumber() {
        return get(Field.SequenceNumber);
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

    public static UadpDataSetMessageContentMask of(Field... fields) {
        long bits = 0L;

        for (Field f : fields) {
            bits |= (1L << f.bitIndex);
        }

        return new UadpDataSetMessageContentMask(UInteger.valueOf(bits));
    }

    public enum Field implements BitIndex {
        Timestamp(0),

        PicoSeconds(1),

        Status(2),

        MajorVersion(3),

        MinorVersion(4),

        SequenceNumber(5);

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