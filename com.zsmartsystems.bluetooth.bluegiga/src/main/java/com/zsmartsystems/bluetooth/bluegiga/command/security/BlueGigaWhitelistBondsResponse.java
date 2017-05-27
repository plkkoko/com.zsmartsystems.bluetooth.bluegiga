/**
 * Copyright (c) 2014-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.zsmartsystems.bluetooth.bluegiga.command.security;

import com.zsmartsystems.bluetooth.bluegiga.BlueGigaResponse;
import com.zsmartsystems.bluetooth.bluegiga.enumeration.BgApiResponse;

/**
 * Class to implement the BlueGiga command <b>whitelistBonds</b>.
 * <p>
 * This command will add all bonded devices with a known public or static address to the local
 * devices white list. Previous entries in the white list will be first cleared. This command
 * can't be used while advertising, scanning or being connected.
 * <p>
 * This class provides methods for processing BlueGiga API commands.
 * <p>
 * Note that this code is autogenerated. Manual changes may be overwritten.
 *
 * @author Chris Jackson - Initial contribution of Java code generator
 */
public class BlueGigaWhitelistBondsResponse extends BlueGigaResponse {
    public static int COMMAND_CLASS = 0x05;
    public static int COMMAND_METHOD = 0x01;

    /**
     * Command result
     * <p>
     * BlueGiga API type is <i>BgApiResponse</i> - Java type is {@link BgApiResponse}
     */
    private BgApiResponse result;

    /**
     * Number of whitelisted bonds
     * <p>
     * BlueGiga API type is <i>uint8</i> - Java type is {@link int}
     */
    private int count;

    /**
     * Response constructor
     */
    public BlueGigaWhitelistBondsResponse(int[] inputBuffer) {
        // Super creates deserializer and reads header fields
        super(inputBuffer);

        // Deserialize the fields
        result = deserializeBgApiResponse();
        count = deserializeUInt8();
    }

    /**
     * Command result
     * <p>
     * BlueGiga API type is <i>BgApiResponse</i> - Java type is {@link BgApiResponse}
     *
     * @return the current result as {@link BgApiResponse}
     */
    public BgApiResponse getResult() {
        return result;
    }

    /**
     * Number of whitelisted bonds
     * <p>
     * BlueGiga API type is <i>uint8</i> - Java type is {@link int}
     *
     * @return the current count as {@link int}
     */
    public int getCount() {
        return count;
    }


    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("BlueGigaWhitelistBondsResponse [result=");
        builder.append(result);
        builder.append(", count=");
        builder.append(count);
        builder.append("]");
        return builder.toString();
    }
}