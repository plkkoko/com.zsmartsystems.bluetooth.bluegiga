/**
 * Copyright (c) 2014-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.zsmartsystems.bluetooth.bluegiga.command.attributeclient;

import com.zsmartsystems.bluetooth.bluegiga.BlueGigaResponse;
import java.util.UUID;

/**
 * Class to implement the BlueGiga command <b>findInformationFoundEvent</b>.
 * <p>
 * This event is generated when characteristics type mappings are found. This happens
 * typically after Find Information command has been issued to discover all attributes of a
 * service.
 * <p>
 * This class provides methods for processing BlueGiga API commands.
 * <p>
 * Note that this code is autogenerated. Manual changes may be overwritten.
 *
 * @author Chris Jackson - Initial contribution of Java code generator
 */
public class BlueGigaFindInformationFoundEvent extends BlueGigaResponse {
    public static int COMMAND_CLASS = 0x04;
    public static int COMMAND_METHOD = 0x04;

    /**
     * Connection handle
     * <p>
     * BlueGiga API type is <i>uint8</i> - Java type is {@link int}
     */
    private int connection;

    /**
     * Characteristics handle
     * <p>
     * BlueGiga API type is <i>uint16</i> - Java type is {@link int}
     */
    private int chrHandle;

    /**
     * Characteristics type (UUID)
     * <p>
     * BlueGiga API type is <i>uuid</i> - Java type is {@link UUID}
     */
    private UUID uuid;

    /**
     * Event constructor
     */
    public BlueGigaFindInformationFoundEvent(int[] inputBuffer) {
        // Super creates deserializer and reads header fields
        super(inputBuffer);

        event = (inputBuffer[0] & 0x80) != 0;

        // Deserialize the fields
        connection = deserializeUInt8();
        chrHandle = deserializeUInt16();
        uuid = deserializeUuid();
    }

    /**
     * Connection handle
     * <p>
     * BlueGiga API type is <i>uint8</i> - Java type is {@link int}
     *
     * @return the current connection as {@link int}
     */
    public int getConnection() {
        return connection;
    }
    /**
     * Characteristics handle
     * <p>
     * BlueGiga API type is <i>uint16</i> - Java type is {@link int}
     *
     * @return the current chr_handle as {@link int}
     */
    public int getChrHandle() {
        return chrHandle;
    }
    /**
     * Characteristics type (UUID)
     * <p>
     * BlueGiga API type is <i>uuid</i> - Java type is {@link UUID}
     *
     * @return the current uuid as {@link UUID}
     */
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("BlueGigaFindInformationFoundEvent [connection=");
        builder.append(connection);
        builder.append(", chrHandle=");
        builder.append(chrHandle);
        builder.append(", uuid=");
        builder.append(uuid);
        builder.append(']');
        return builder.toString();
    }
}
