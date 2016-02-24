/*
 * ******************************************************************************
 *   Copyright 2014-2015 Spectra Logic Corporation. All Rights Reserved.
 *   Licensed under the Apache License, Version 2.0 (the "License"). You may not use
 *   this file except in compliance with the License. A copy of the License is located at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   or in the "license" file accompanying this file.
 *   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 *   CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 * ****************************************************************************
 */

package com.spectralogic.ds3autogen.net;

import com.google.common.base.CaseFormat;
import com.google.common.collect.ImmutableList;
import com.spectralogic.ds3autogen.api.models.Arguments;
import com.spectralogic.ds3autogen.utils.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;

import static com.spectralogic.ds3autogen.utils.ConverterUtil.isEmpty;
import static com.spectralogic.ds3autogen.utils.Helper.capFirst;
import static com.spectralogic.ds3autogen.utils.Helper.sortConstructorArgs;
import static com.spectralogic.ds3autogen.utils.Helper.uncapFirst;

public final class NetHelper {

    private final static Logger LOG = LoggerFactory.getLogger(NetHelper.class);

    private NetHelper() {
        // pass
    }

    /**
     * Creates a comma separated list of constructor arguments
     */
    public static String constructor(final ImmutableList<Arguments> args) {
        if (isEmpty(args)) {
            return "";
        }
        return sortConstructorArgs(args)
                .stream()
                .map(i -> getType(i) + " " + uncapFirst(i.getName()))
                .collect(Collectors.joining(", "));
    }

    /**
     * Converts a camel cased name to a lower hyphenated name
     */
    public static String camelToHyphen(final String str) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_HYPHEN, str);
    }

    /**
     * Gets the type of an argument, converting the type from Contract type to a .net type.
     * @param arg An Argument
     * @return The Java type of the Argument
     */
    public static String getType(final Arguments arg) {
        if (isEmpty(arg.getType())) {
            LOG.error("Argument does not have a type: " + arg.getName());
            return "";
        }

        switch (arg.getType().toLowerCase()) {
            case "void":
            case "boolean":
                return "bool";
            case "integer":
                return "int";
            case "string":
                return "string";
            case "uuid":
                return "Guid";
            case "checksumtype":
                return arg.getType() + ".Type";
            default:
                return arg.getType();
        }
    }

    /**
     * Gets the nullable type of an argument, converting the argument from a Contract
     * type to a nullable .net type.
     */
    public static String getNullableType(final Arguments arg) {
        final String type = getType(arg);
        switch (type) {
            case "":
                return "";
            case "string":
                return type;
            default:
                return type + "?";
        }
    }

    /**
     * Creates the .net code for converting an argument to a String.
     */
    public static String argToString(final Arguments arg) {
        if (isEmpty(arg.getType())) {
            LOG.error("Argument does not have a type: " + arg.getName());
            return "";
        }
        switch (arg.getType().toLowerCase()) {
            case "boolean":
            case "void":
                return "null";
            case "string":
                return capFirst(arg.getName());
            case "double":
            case "integer":
            case "int":
            case "long":
                return  capFirst(arg.getName()) + ".ToString()";
            default:
                return uncapFirst(arg.getName()) + ".ToString()";
        }
    }

    /**
     * Determines if a list of arguments contains the specified argument.
     * This is a wrapper function because Helper is not currently accessible
     * from within the template
     */
    public static boolean containsArgument(final ImmutableList<Arguments> args, final String argName) {
        return Helper.containsArgument(args, argName);
    }

    private final static NetHelper instance = new NetHelper();

    public static NetHelper getInstance() {
        return instance;
    }
}
