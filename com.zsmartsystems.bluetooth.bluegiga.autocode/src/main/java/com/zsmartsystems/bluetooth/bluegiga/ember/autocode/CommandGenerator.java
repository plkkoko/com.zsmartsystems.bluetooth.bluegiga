package com.zsmartsystems.bluetooth.bluegiga.ember.autocode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zsmartsystems.bluetooth.bluegiga.ember.autocode.xml.Command;
import com.zsmartsystems.bluetooth.bluegiga.ember.autocode.xml.Enumeration;
import com.zsmartsystems.bluetooth.bluegiga.ember.autocode.xml.Parameter;
import com.zsmartsystems.bluetooth.bluegiga.ember.autocode.xml.Protocol;
import com.zsmartsystems.bluetooth.bluegiga.ember.autocode.xml.Value;

/**
 *
 * @author Chris Jackson
 *
 */
public class CommandGenerator extends ClassGenerator {
    final String commandPackage = "com.zsmartsystems.bluetooth.bluegiga.command";
    final String enumPackage = "com.zsmartsystems.bluetooth.bluegiga.enumeration";

    public void go(Protocol protocol) throws FileNotFoundException {
        String packageName;
        String className;
        for (Command command : protocol.commands) {
            packageName = commandPackage + "." + getClassName(command.cmdClass);
            if (command.name.endsWith("Event")) {
                className = "BlueGiga" + command.name.substring(0, 1).toUpperCase() + command.name.substring(1);
                createCommandClass(packageName, className, command, command.response_parameters);
            } else {
                className = "BlueGiga" + upperCaseFirstCharacter(command.name) + "Command";
                createCommandClass(packageName, className, command, command.command_parameters);

                className = "BlueGiga" + command.name.substring(0, 1).toUpperCase() + command.name.substring(1)
                        + "Response";
                createCommandClass(packageName, className, command, command.response_parameters);
            }
        }

        for (Enumeration enumeration : protocol.enumerations) {
            createEnumClass(enumeration);
        }
    }

    private void createCommandClass(String packageName, String className, Command command, List<Parameter> parameters)
            throws FileNotFoundException {

        System.out.println("Processing command class " + command.name + "  [" + className + "()]");

        StringWriter stringWriter = new StringWriter();
        PrintWriter out = new PrintWriter(stringWriter);

        clearImports();
        // addImport("org.slf4j.Logger");
        // addImport("org.slf4j.LoggerFactory");

        // addImport("java.util.Map");
        // addImport("java.util.HashMap");

        out.println("/**");
        out.println(" * Class to implement the BlueGiga command <b>" + command.name + "</b>.");
        out.println(" * <p>");
        outputWithLinebreak(out, "", command.description);
        out.println(" * <p>");
        out.println(" * This class provides methods for processing BlueGiga API commands.");
        out.println(" * <p>");
        out.println(" * Note that this code is autogenerated. Manual changes may be overwritten.");
        out.println(" *");
        out.println(" * @author Chris Jackson - Initial contribution of Java code generator");
        out.println(" */");

        if (className.endsWith("Event")) {
            addImport("com.zsmartsystems.bluetooth.bluegiga.BlueGigaResponse");
            out.println("public class " + className + " extends BlueGigaResponse {");
        } else if (className.endsWith("Command")) {
            addImport("com.zsmartsystems.bluetooth.bluegiga.BlueGigaCommand");
            out.println("public class " + className + " extends BlueGigaCommand {");
        } else {
            addImport("com.zsmartsystems.bluetooth.bluegiga.BlueGigaResponse");
            out.println("public class " + className + " extends BlueGigaResponse {");
        }

        out.println("    public static int COMMAND_CLASS = " + String.format("0x%02X", command.cmdClass) + ";");
        out.println("    public static int COMMAND_METHOD = " + String.format("0x%02X", command.id) + ";");

        // out.println(" private static final Logger logger =
        // LoggerFactory.getLogger(" + className + ".class);");

        for (Parameter parameter : parameters) {
            if (parameter.auto_size != null) {
                continue;
            }

            out.println();
            out.println("    /**");
            outputWithLinebreak(out, "    ", parameter.description);
            out.println("     * <p>");
            out.println("     * BlueGiga API type is <i>" + parameter.data_type + "</i> - Java type is {@link "
                    + getTypeClass(parameter.data_type) + "}");
            out.println("     */");
            out.println("    private " + getTypeClass(parameter.data_type) + " "
                    + stringToLowerCamelCase(parameter.name) + ";");
        }

        if (className.endsWith("Command")) {
            // addImport("com.zsmartsystems.zigbee.dongle.ember.ezsp.serializer.EzspSerializer");
            // out.println();
            // out.println(" /**");
            // out.println(" * Serialiser used to seialise to binary line
            // data");
            // out.println(" */");
            // out.println(" private EzspSerializer serializer;");
            // out.println();
            // out.println(" /**");
            // out.println(" * Request constructor");
            // out.println(" */");
            // out.println(" public " + className + "() {");
            // out.println(" frameId = FRAME_ID;");
            // out.println(" serializer = new EzspSerializer();");
            // out.println(" }");
        } else {
            // addImport("com.zsmartsystems.zigbee.dongle.ember.ezsp.serializer.EzspDeserializer");
            out.println();
            // out.println(" private EzspDeserializer serializer;");
            // out.println();
            out.println("    /**");
            if (className.endsWith("Event")) {
                out.println("     * Event constructor");
            } else {
                out.println("     * Response constructor");
            }
            out.println("     */");
            out.println("    public " + className + "(int[] inputBuffer) {");
            out.println("        // Super creates deserializer and reads header fields");
            out.println("        super(inputBuffer);");
            out.println();
            out.println("        // Deserialize the fields");
            Map<String, String> autoSizers = new HashMap<String, String>();
            for (Parameter parameter : parameters) {
                if (parameter.auto_size != null) {
                    out.println("        int " + stringToLowerCamelCase(parameter.name) + " = deserialize"
                            + getTypeSerializer(parameter.data_type) + "();");
                    autoSizers.put(parameter.auto_size, parameter.name);
                    continue;
                }
                if (autoSizers.get(parameter.name) != null) {
                    out.println("        " + parameter.name + "= deserialize" + getTypeSerializer(parameter.data_type)
                            + "(" + autoSizers.get(parameter.name) + ");");
                    continue;
                }
                if (parameter.data_type.contains("[") && parameter.data_type.contains("]")
                        && !parameter.data_type.contains("[]")) {
                    int length = Integer.parseInt(parameter.data_type.substring(parameter.data_type.indexOf("[") + 1,
                            parameter.data_type.indexOf("]")));
                    out.println("        " + stringToLowerCamelCase(parameter.name) + " = deserialize"
                            + getTypeSerializer(parameter.data_type) + "(" + length + ");");
                    continue;
                }
                out.println("        " + stringToLowerCamelCase(parameter.name) + " = deserialize"
                        + getTypeSerializer(parameter.data_type) + "();");
            }
            out.println("    }");
            out.println();
        }

        for (Parameter parameter : parameters) {
            if (parameter.auto_size != null) {
                continue;
            }

            if (className.endsWith("Command")) {
                out.println("    /**");
                outputWithLinebreak(out, "    ", parameter.description);
                out.println("     *");
                out.println("     * @param " + stringToLowerCamelCase(parameter.name) + " the "
                        + stringToLowerCamelCase(parameter.name) + " to set as {@link "
                        + getTypeClass(parameter.data_type) + "}");
                out.println("     */");
                out.println("    public void set" + stringToUpperCamelCase(parameter.name) + "("
                        + getTypeClass(parameter.data_type) + " " + stringToLowerCamelCase(parameter.name) + ") {");
                out.println("        this." + stringToLowerCamelCase(parameter.name) + " = "
                        + stringToLowerCamelCase(parameter.name) + ";");
                out.println("    }");
                out.println();
            } else {
                out.println("    /**");
                outputWithLinebreak(out, "    ", parameter.description);
                out.println("     * <p>");
                out.println("     * BlueGiga API type is <i>" + parameter.data_type + "</i> - Java type is {@link "
                        + getTypeClass(parameter.data_type) + "}");
                out.println("     *");
                out.println("     * @return the current " + parameter.name + " as {@link "
                        + getTypeClass(parameter.data_type) + "}");
                out.println("     */");
                out.println("    public " + getTypeClass(parameter.data_type) + " get"
                        + stringToUpperCamelCase(parameter.name) + "() {");
                out.println("        return " + stringToLowerCamelCase(parameter.name) + ";");
                out.println("    }");
                out.println();
            }
        }

        if (className.endsWith("Command")) {
            out.println();
            out.println("    @Override");
            out.println("    public int[] serialize() {");
            out.println("        // Serialize the header");
            out.println("        serializeHeader(COMMAND_CLASS, COMMAND_METHOD);");
            out.println();

            if (parameters != null && parameters.size() != 0) {
                out.println("        // Serialize the fields");
                for (Parameter parameter : parameters) {
                    if (parameter.auto_size != null) {
                        out.println("        serialize" + getTypeSerializer(parameter.data_type) + "("
                                + parameter.auto_size + ".length);");
                        continue;
                    }
                    out.println("        serialize" + getTypeSerializer(parameter.data_type) + "("
                            + stringToLowerCamelCase(parameter.name) + ");");
                }
                out.println();
            }
            out.println("        return getPayload();");
            out.println("    }");
        } else {

        }

        out.println();
        out.println("    @Override");
        out.println("    public String toString() {");

        if (parameters == null || parameters.size() == 0) {
            out.println("        return \"" + className + " []\";");
        } else {
            out.println("        final StringBuilder builder = new StringBuilder();");
            boolean first = true;
            for (Parameter parameter : parameters) {
                if (parameter.auto_size != null) {
                    continue;
                }

                if (first) {
                    out.println("        builder.append(\"" + className + " [" + stringToLowerCamelCase(parameter.name)
                            + "=\");");
                } else {
                    out.println("        builder.append(\", " + stringToLowerCamelCase(parameter.name) + "=\");");
                }
                first = false;
                if (parameter.data_type.equals("uint8array")) {
                    out.println("        for (int c = 0; c < " + stringToLowerCamelCase(parameter.name)
                            + ".length; c++) {");
                    out.println("            if (c > 0) {");
                    out.println("                builder.append(' ');");
                    out.println("            }");
                    out.println("            builder.append(String.format(\"%02X\", " + formatParameterString(parameter)
                            + "[c]));");
                    out.println("        }");
                } else {
                    out.println("        builder.append(" + formatParameterString(parameter) + ");");
                }
            }
            out.println("        builder.append(']');");
            out.println("        return builder.toString();");
        }
        out.println("    }");

        out.println("}");

        out.flush();

        File packageFile = new File(sourceRootPath + packageName.replace(".", "/"));
        PrintWriter outFile = getClassOut(packageFile, className);

        outputCopywrite(outFile);
        outFile.println("package " + packageName + ";");

        outFile.println();

        outputImports(outFile);

        outFile.println();
        outFile.print(stringWriter.toString());

        outFile.flush();
        outFile.close();

        out.close();
    }

    private void createEnumClass(Enumeration enumeration) throws FileNotFoundException {
        String className = upperCaseFirstCharacter(enumeration.name);
        System.out.println("Processing enum class " + enumeration.name + "  [" + className + "()]");

        StringWriter stringWriter = new StringWriter();
        PrintWriter out = new PrintWriter(stringWriter);

        clearImports();

        addImport("java.util.Map");
        addImport("java.util.HashMap");

        out.println("/**");
        out.println(" * Class to implement the BlueGiga Enumeration <b>" + enumeration.name + "</b>.");
        if (enumeration.description != null && enumeration.description.trim().length() > 0) {
            out.println(" * <p>");
            outputWithLinebreak(out, "", enumeration.description);
        }
        out.println(" * <p>");
        out.println(" * Note that this code is autogenerated. Manual changes may be overwritten.");
        out.println(" *");
        out.println(" * @author Chris Jackson - Initial contribution of Java code generator");
        out.println(" */");

        out.println("public enum " + className + " {");

        out.println("    /**");
        out.println("     * Default unknown value");
        out.println("     */");
        out.println("    UNKNOWN(-1),");

        boolean first = true;
        for (Value value : enumeration.values) {
            if (!first) {
                out.println(",");
            }
            first = false;
            out.println();
            out.println("    /**");
            outputWithLinebreak(out, "    ", value.description);
            out.println("     */");
            out.print("    " + value.name.toUpperCase() + "(0x" + String.format("%04X", value.enum_value) + ")");
        }

        out.println(";");

        out.println();
        out.println("    /**");
        out.println("     * A mapping between the integer code and its corresponding type to");
        out.println("     * facilitate lookup by code.");
        out.println("     */");
        out.println("    private static Map<Integer, " + className + "> codeMapping;");
        out.println();

        out.println("    private int key;");
        out.println();

        out.println("    private " + className + "(int key) {");
        out.println("        this.key = key;");
        out.println("    }");
        out.println();

        out.println("    private static void initMapping() {");
        out.println("        codeMapping = new HashMap<Integer, " + className + ">();");
        out.println("        for (" + className + " s : values()) {");
        out.println("            codeMapping.put(s.key, s);");
        out.println("        }");
        out.println("    }");
        out.println();

        out.println("    /**");
        out.println("     * Lookup function based on the type code. Returns null if the code does not exist.");
        out.println("     *");
        out.println("     * @param i");
        out.println("     *            the code to lookup");
        out.println("     * @return enumeration value.");
        out.println("     */");
        out.println("    public static " + className + " get" + className + "(int i) {");
        out.println("        if (codeMapping == null) {");
        out.println("            initMapping();");
        out.println("        }");
        out.println();

        out.println("        if (codeMapping.get(i) == null) {");
        out.println("            return UNKNOWN;");
        out.println("        }");
        out.println();

        out.println("        return codeMapping.get(i);");
        out.println("    }");
        out.println();
        out.println("    /**");
        out.println("     * Returns the BlueGiga protocol defined value for this enum");
        out.println("     *");
        out.println("     * @return the BGAPI enumeration key");
        out.println("     */");
        out.println("    public int getKey() {");
        out.println("        return key;");
        out.println("    }");

        out.println("}");

        out.flush();

        File packageFile = new File(sourceRootPath + enumPackage.replace(".", "/"));
        PrintWriter outFile = getClassOut(packageFile, className);

        outputCopywrite(outFile);
        outFile.println("package " + enumPackage + ";");

        outFile.println();

        outputImports(outFile);

        outFile.println();
        outFile.print(stringWriter.toString());

        outFile.flush();
        outFile.close();

        out.close();
    }

    protected String getTypeClass(String dataType) {
        String dataTypeLocal = new String(dataType);

        switch (dataTypeLocal) {
            case "int8":
                return "int";
            case "uint8":
                return "int";
            case "uint16":
                return "int";
            case "uint32":
                return "long";
            case "uint8array":
                return "int[]";
            case "bd_addr":
                return "String";
            default:
                addImport(enumPackage + "." + dataTypeLocal);
                return dataTypeLocal;
        }
    }

    protected String getTypeSerializer(String dataType) {
        String dataTypeLocal = new String(dataType);

        switch (dataTypeLocal) {
            case "int8":
                return "Int8";
            case "uint8":
                return "UInt8";
            case "uint16":
                return "UInt16";
            case "uint32":
                return "UInt32";
            case "uint8array":
                return "UInt8Array";
            case "bd_addr":
                return "Address";
            default:
                return dataTypeLocal;
        }
    }
}
