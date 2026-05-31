/*
 * A Gradle plugin for the creation of Minecraft mods and MinecraftForge plugins.
 * Copyright (C) 2013 Minecraft Forge
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 * USA
 */
package net.minecraftforge.gradle.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.SortedMap;

/**
 * Reflection-based wrapper for {@code java.util.jar.Pack200}.
 * <p>
 * Pack200 was removed in JDK 14. This helper allows the plugin to work on
 * both JDK 8 (where Pack200 exists) and JDK 17/21 (where it doesn't).
 * <p>
 * When Pack200 is unavailable, pack/unpack are no-ops (passthrough).
 */
public class Pack200Helper {
    private static final boolean AVAILABLE;
    private static Method newPackerMethod;
    private static Method newUnpackerMethod;

    static {
        boolean found;
        try {
            Class<?> pack200Class = Class.forName("java.util.jar.Pack200");
            newPackerMethod = pack200Class.getMethod("newPacker");
            newUnpackerMethod = pack200Class.getMethod("newUnpacker");
            found = true;
        } catch (Exception e) {
            found = false;
        }
        AVAILABLE = found;
    }

    public static boolean isAvailable() {
        return AVAILABLE;
    }

    /**
     * Packs a JAR stream using Pack200 compression.
     * Falls back to passthrough if Pack200 is unavailable.
     */
    public static void pack(InputStream inJar, OutputStream out) throws Exception {
        if (!AVAILABLE) {
            byte[] buf = new byte[8192];
            int len;
            while ((len = inJar.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            return;
        }

        Object packer = newPackerMethod.invoke(null);
        Method propsMethod = packer.getClass().getMethod("properties");
        Method packMethod = packer.getClass().getMethod("pack", InputStream.class, OutputStream.class);

        @SuppressWarnings("unchecked")
        SortedMap<String, String> props = (SortedMap<String, String>) propsMethod.invoke(packer);
        props.put("effort", "9");
        props.put("keep.file.order", "true");
        props.put("unknown.attribute", "pass");

        packMethod.invoke(packer, inJar, out);
    }

    /**
     * Unpacks a Pack200 stream into a JAR stream.
     * Falls back to passthrough if Pack200 is unavailable.
     */
    public static void unpack(InputStream in, OutputStream outJar) throws Exception {
        if (!AVAILABLE) {
            // Passthrough: copy raw data
            byte[] buf = new byte[8192];
            int len;
            while ((len = in.read(buf)) > 0) {
                outJar.write(buf, 0, len);
            }
            return;
        }

        Object unpacker = newUnpackerMethod.invoke(null);
        Method unpackMethod = unpacker.getClass().getMethod("unpack", InputStream.class, OutputStream.class);
        unpackMethod.invoke(unpacker, in, outJar);
    }
}
