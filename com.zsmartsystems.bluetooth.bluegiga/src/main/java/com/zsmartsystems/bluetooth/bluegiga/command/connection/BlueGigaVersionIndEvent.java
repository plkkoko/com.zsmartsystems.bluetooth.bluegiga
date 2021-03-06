/**
 * Copyright (c) 2014-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.zsmartsystems.bluetooth.bluegiga.command.connection;

import com.zsmartsystems.bluetooth.bluegiga.BlueGigaResponse;

/**
 * Class to implement the BlueGiga command <b>versionIndEvent</b>.
 * <p>
 * This event indicates the remote devices version.
 * <p>
 * This class provides methods for processing BlueGiga API commands.
 * <p>
 * Note that this code is autogenerated. Manual changes may be overwritten.
 *
 * @author Chris Jackson - Initial contribution of Java code generator
 */
public class BlueGigaVersionIndEvent extends BlueGigaResponse {
    public static int COMMAND_CLASS = 0x03;
    public static int COMMAND_METHOD = 0x01;

    /**
     * Connection handle
     * <p>
     * BlueGiga API type is <i>uint8</i> - Java type is {@link int}
     */
    private int connection;

    /**
     * Bluetooth controller specification version
     * <p>
     * BlueGiga API type is <i>uint8</i> - Java type is {@link int}
     */
    private int versNr;

    /**
     * Manufacturer of the controller
     * <p>
     * BlueGiga API type is <i>uint16</i> - Java type is {@link int}
     */
    private int compId;

    /**
     * Bluetooth controller version
     * <p>
     * BlueGiga API type is <i>uint16</i> - Java type is {@link int}
     */
    private int subVersNr;

    /**
     * Event constructor
     */
    public BlueGigaVersionIndEvent(int[] inputBuffer) {
        // Super creates deserializer and reads header fields
        super(inputBuffer);

        event = (inputBuffer[0] & 0x80) != 0;

        // Deserialize the fields
        connection = deserializeUInt8();
        versNr = deserializeUInt8();
        compId = deserializeUInt16();
        subVersNr = deserializeUInt16();
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
     * Bluetooth controller specification version
     * <p>
     * BlueGiga API type is <i>uint8</i> - Java type is {@link int}
     *
     * @return the current vers_nr as {@link int}
     */
    public int getVersNr() {
        return versNr;
    }
    /**
     * Manufacturer of the controller
     * <p>
     * BlueGiga API type is <i>uint16</i> - Java type is {@link int}
     *
     * @return the current comp_id as {@link int}
     */
    public int getCompId() {
        return compId;
    }
    /**
     * Bluetooth controller version
     * <p>
     * BlueGiga API type is <i>uint16</i> - Java type is {@link int}
     *
     * @return the current sub_vers_nr as {@link int}
     */
    public int getSubVersNr() {
        return subVersNr;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("BlueGigaVersionIndEvent [connection=");
        builder.append(connection);
        builder.append(", versNr=");
        builder.append(versNr);
        builder.append(", compId=");
        builder.append(compId);
        builder.append(", subVersNr=");
        builder.append(subVersNr);
        builder.append(']');
        return builder.toString();
    }
}
