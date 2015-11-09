package com.spectralogic.ds3autogen.java.helpers;

import com.google.common.collect.ImmutableList;
import com.spectralogic.ds3autogen.api.models.Arguments;
import com.spectralogic.ds3autogen.api.models.Operation;
import com.spectralogic.ds3autogen.utils.Helper;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class JavaHelper {
    private final static JavaHelper javaHelper = new JavaHelper();
    private final static List<String> bulkBaseClassArgs = Arrays.asList("Priority", "WriteOptimization");
    private final static String indent = "    ";

    private JavaHelper() {}

    public static JavaHelper getInstance() {
        return javaHelper;
    }

    public static boolean isBulkRequestArg(final String name) {
        return bulkBaseClassArgs.contains(name);
    }

    public static String createWithConstructor(final Arguments arg, final String requestName) {
        final StringBuilder stringBuilder = new StringBuilder();
        if (arg.getType().equals("void")) {
            stringBuilder.append(voidWithConstructor(arg, requestName));
        } else {
            stringBuilder.append(withConstructor(arg, requestName));
        }

        stringBuilder.append(indent(2)).append("return this;\n").append(indent(1)).append("}\n");
        return stringBuilder.toString();
    }

    public static String createGetter(final String argName, final String argType) {
        return "public " + argType + " get" + capFirst(argName) + "() {\n"
                + indent(2) + "return this." + uncapFirst(argName) + ";\n"
                + indent(1) + "}\n";
    }

    public static String createWithConstructorBulk(final Arguments arg, final String requestName) {
        final StringBuilder stringBuilder = new StringBuilder();
        if (bulkBaseClassArgs.contains(arg.getName())) {
            stringBuilder.append(indent(1)).append("@Override\n").append(withConstructorFirstLine(arg, requestName)).append(indent(2)).append("super.with").append(capFirst(arg.getName())).append("(").append(uncapFirst(arg.getName())).append(");\n");
        } else if (arg.getName().equals("MaxUploadSize")) {
            stringBuilder.append(maxUploadSizeWithConstructor(arg, requestName));
        } else if (arg.getType().equals("void")) {
            stringBuilder.append(voidWithConstructor(arg, requestName));
        } else {
            stringBuilder.append(withConstructor(arg, requestName));
        }

        stringBuilder.append(indent(2)).append("return this;\n").append(indent(1)).append("}\n");
        return stringBuilder.toString();
    }

    private static String withConstructor(final Arguments arg, final String requestName) {
        return withConstructorFirstLine(arg, requestName)
                + indent(2) + argAssignmentLine(arg.getName())
                + indent(2) + updateQueryParamLine(arg.getName(), argToString(arg));
    }

    private static String maxUploadSizeWithConstructor(final Arguments arg, final String requestName) {
        return withConstructorFirstLine(arg, requestName)
                + indent(2) + "if (" + uncapFirst(arg.getName()) + " > MIN_UPLOAD_SIZE_IN_BYTES) {\n"
                + indent(3) + putQueryParamLine(arg.getName(), argToString(arg)) + "\n"
                + indent(2) + "} else {\n"
                + indent(3) + putQueryParamLine(arg.getName(), "MAX_UPLOAD_SIZE_IN_BYTES") + "\n"
                + indent(2) + "}\n";
    }

    private static String voidWithConstructor(final Arguments arg, final String requestName) {
        return withConstructorFirstLine(arg, requestName)
                + indent(2) + "this." + uncapFirst(arg.getName()) + " = " + uncapFirst(arg.getName()) + ";\n"
                + indent(2) + "if (this." + uncapFirst(arg.getName()) + ") {\n"
                + indent(3) + putQueryParamLine(arg.getName(), "null") + "\n"
                + indent(2) + "} else {\n"
                + indent(3) + removeQueryParamLine(arg.getName())
                + indent(2) + "}\n";
    }

    private static String withConstructorFirstLine(final Arguments arg, final String requestName) {
        return indent(1) + "public " + requestName + " with" + capFirst(arg.getName()) + "(final " + getType(arg) + " " + uncapFirst(arg.getName()) + ") {\n";
    }

    private static String argAssignmentLine(final String name) {
        return "this." + uncapFirst(name) + " = " + uncapFirst(name) + ";\n";
    }

    private static String removeQueryParamLine(final String name) {
        return "this.getQueryParams().remove(\"" + Helper.camelToUnderscore(name) + "\");\n";
    }

    public static String putQueryParamLine(final Arguments arg) {
        return putQueryParamLine(arg.getName(), argToString(arg));
    }

    private static String putQueryParamLine(final String name, final String type) {
        return "this.getQueryParams().put(\"" + Helper.camelToUnderscore(name) + "\", " + type + ");";
    }

    private static String updateQueryParamLine(final String name, final String type) {
        return "this.updateQueryParam(\"" + Helper.camelToUnderscore(name) + "\", " + type + ");\n";
    }

    public static String toXmlLine(
            final String outputStringName,
            final String objectListName,
            final Operation operation) {
        final StringBuilder builder = new StringBuilder();
        builder.append("final String " + outputStringName + " = XmlOutput.toXml(" + objectListName + ", ");
        if (operation == Operation.START_BULK_GET) {
            return builder.append("true);").toString();
        }
        return  builder.append("false);").toString();
    }

    private static String indent(final int depth) {
        StringBuilder stringBuilder = new StringBuilder();
        if (depth <= 0) {
            return null;
        }
        for (int i = 0; i < depth; i++) {
            stringBuilder.append(indent);
        }
        return stringBuilder.toString();
    }

    public static String argToString(final Arguments arg) {
        switch (arg.getType()) {
            case "void":
                return "null";
            case "String":
                return uncapFirst(arg.getName());
            case "Integer":
            case "long":
                return capFirst(arg.getType()) + ".toString(" + uncapFirst(arg.getName()) + ")";
            default:
                return uncapFirst(arg.getName()) + ".toString()";
        }
    }

    public static ImmutableList<Arguments> removeArgument(final List<Arguments> arguments, final String name) {
        final ImmutableList.Builder<Arguments> builder = ImmutableList.builder();
        for (final Arguments arg : arguments) {
            if (!arg.getName().equals(name)) {
                builder.add(arg);
            }
        }
        return builder.build();
    }

    public static ImmutableList<Arguments> addArgument(
            final ImmutableList<Arguments> arguments,
            final ImmutableList<Arguments> additionalArguments) {
        final ImmutableList.Builder<Arguments> builder = ImmutableList.builder();
        builder.addAll(arguments);
        builder.addAll(additionalArguments);
        return builder.build();
    }

    public static ImmutableList<Arguments> addArgument(
            final ImmutableList<Arguments> arguments,
            final String argName,
            final String argType) {
        final ImmutableList.Builder<Arguments> builder = ImmutableList.builder();
        builder.addAll(arguments);
        builder.add(new Arguments(argType, argName));
        return builder.build();
    }

    public static ImmutableList<Arguments> sortConstructorArgs(final ImmutableList<Arguments> arguments) {
        final List<Arguments> sortable = new ArrayList<>();
        sortable.addAll(arguments);
        Collections.sort(sortable, new CustomComparator());

        final ImmutableList.Builder<Arguments> builder = ImmutableList.builder();
        builder.addAll(sortable);
        return builder.build();
    }

    public static String argTypeList(final ImmutableList<Arguments> arguments) {
        return sortConstructorArgs(arguments)
                .stream()
                .map(i -> i.getType()).collect(Collectors.joining(", "));
    }

    public static String argsToList(final List<Arguments> arguments) {
        return arguments.stream().map(i -> uncapFirst(i.getName())).collect(Collectors.joining(", "));
    }

    public static String capFirst(final String str) {
        return StringUtils.capitalize(str);
    }

    public static String uncapFirst(final String str) {
        return StringUtils.uncapitalize(str);
    }

    public static String getType(final Arguments arg) {
        if (arg.getType() == null) {
            return "";
        }

        switch (arg.getType()) {
            case "void":
                return "boolean";
            case "Integer":
                return "int";
            default:
                return arg.getType();
        }
    }

    public static String constructorArgs(final ImmutableList<Arguments> requiredArguments) {
        return sortConstructorArgs(requiredArguments)
                .stream()
                .map(i -> "final " + getType(i) + " " + uncapFirst(i.getName()))
                .collect(Collectors.joining(", "));
    }

    /*
     * Creates a comma separated list of argument names, while changing one argument name to a specified value
     */
    public static String modifiedArgNameList(
            final ImmutableList<Arguments> arguments,
            final String modifyArgName, final String toArgName) {
        final ImmutableList.Builder<String> builder = ImmutableList.builder();
        for (final Arguments arg : arguments) {
            if (arg.getName().equals(modifyArgName)) {
                builder.add(toArgName);
            } else {
                builder.add(uncapFirst(arg.getName()));
            }
        }
        return builder.build()
                .stream()
                .map(i -> i)
                .collect(Collectors.joining(", "));
    }
}