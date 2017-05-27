/**
 * Copyright (c) 2014-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.zsmartsystems.bluetooth.bluegiga.command.attributedb;

import com.zsmartsystems.bluetooth.bluegiga.BlueGigaCommand;

/**
 * Class to implement the BlueGiga command <b>write</b>.
 * <p>
 * This command writes an attribute's value to the local database.
 * <p>
 * This class provides methods for processing BlueGiga API commands.
 * <p>
 * Note that this code is autogenerated. Manual changes may be overwritten.
 *
 * @author Chris Jackson - Initial contribution of Java code generator
 */
public class BlueGigaWriteCommand extends BlueGigaCommand {
    public static int COMMAND_CLASS = 0x02;
    public static int COMMAND_METHOD = 0x00;

    /**
     * Handle of the attribute to write.
     * <p>
     * BlueGiga API type is <i>uint16</i> - Java type is {@link int}
     */
    private int handle;

    /**
     * Attribute offset to write data
     * <p>
     * BlueGiga API type is <i>uint8</i> - Java type is {@link int}
     */
    private int offset;

    /**
     * Value of the attribute to write
     * <p>
     * BlueGiga API type is <i>uint8array</i> - Java type is {@link int[]}
     */
    private int[] value;
    /**
     * Handle of the attribute to write.
     *
     * @param handle the handle to set as {@link int}
     */
    public void setHandle(int handle) {
        this.handle = handle;
    }

    /**
     * Attribute offset to write data
     *
     * @param offset the offset to set as {@link int}
     */
    public void setOffset(int offset) {
        this.offset = offset;
    }

    /**
     * Value of the attribute to write
     *
     * @param value the value to set as {@link int[]}
     */
    public void setValue(int[] value) {
        this.value = value;
    }


    @Override
    public int[] serialize() {
        // Serialize the header
        serializeHeader(COMMAND_CLASS, COMMAND_METHOD);

        // Serialize the fields
        serializeUInt16(handle);
        serializeUInt8(offset);
        serializeUInt8Array(value);

        return getPayload();
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("BlueGigaWriteCommand [handle=");
        builder.append(handle);
        builder.append(", offset=");
        builder.append(offset);
        builder.append(", value=");
        builder.append(value);
        builder.append("]");
        return builder.toString();
    }
}