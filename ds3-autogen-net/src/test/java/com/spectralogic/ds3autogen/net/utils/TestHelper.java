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

package com.spectralogic.ds3autogen.net.utils;

import com.google.common.collect.ImmutableList;
import com.spectralogic.ds3autogen.api.models.Arguments;
import com.spectralogic.ds3autogen.net.NetHelper;

import java.util.regex.Pattern;

import static com.spectralogic.ds3autogen.utils.Helper.capFirst;
import static com.spectralogic.ds3autogen.utils.Helper.uncapFirst;

public final class TestHelper {

    private TestHelper() {
        // pass
    }

    /**
     * Checks that the response parser has the specified response type and name to marshal
     */
    public static boolean parserHasPayload(
            final String responseType,
            final String nameToMarshal,
            final String parserCode) {
        final Pattern searchString = Pattern.compile(
                "\\s+XmlExtensions" +
                        "\\s+.ReadDocument\\(stream\\)" +
                        "\\s+.ElementOrThrow\\(\"" + nameToMarshal + "\"\\)" +
                        "\\s+.Select\\(" + responseType + "Parser\\)"
                , Pattern.MULTILINE | Pattern.UNIX_LINES);

        return searchString.matcher(parserCode).find();
    }

    /**
     * Checks that the response parser has the specified Http Status Code
     */
    public static boolean parserHasResponseCode(final int responseCode, final String parserCode) {
        final String searchString = "ResponseParseUtilities.HandleStatusCode(response, (HttpStatusCode)"
                + Integer.toString(responseCode) + ");";
        return parserCode.contains(searchString);
    }

    /**
     * Checks if the generated code extends the specified class
     */
    public static boolean extendsClass(final String getObjectRequestHandler, final String abstractRequest, final String generatedCode) {
        final String searchString = "public class " + getObjectRequestHandler + " : " + abstractRequest;
        return generatedCode.contains(searchString);
    }

    /**
     * Checks if the generated code has the specified property
     */
    public static boolean hasProperty(final String propertyName, final String type, final String generatedCode) {
        final Pattern searchString = Pattern.compile("(internal\\s)?(override\\s)?" + type + " " + propertyName, Pattern.MULTILINE | Pattern.UNIX_LINES);
        return searchString.matcher(generatedCode).find();
    }

    /**
     * Checks if the generated code contains the specified optional parameter
     */
    public static boolean hasOptionalParam(
            final String requestName,
            final String paramName,
            final String paramType,
            final String generatedCode) {
        final Pattern searchString = Pattern.compile("private\\s" + paramType + "\\???\\s_" + uncapFirst(paramName) + ";"
                + "\\s+public\\s" + paramType + "\\???\\s" + paramName + "\\s+\\{"
                + "\\s+get\\s\\{\\sreturn\\s_" + uncapFirst(paramName) + ";\\s\\}"
                + "\\s+set\\s\\{\\sWith" + capFirst(paramName) + "\\(value\\);\\s\\}"
                + "\\s+\\}"
                + "\\s+public\\s" + requestName + "\\sWith" + capFirst(paramName) + "\\(" + paramType + "\\???\\s" + uncapFirst(paramName) + "\\)",
                Pattern.MULTILINE | Pattern.UNIX_LINES);
        return searchString.matcher(generatedCode).find();
    }

    /**
     * Checks if the generated code contains the specified required parameter
     */
    public static boolean hasRequiredParam(final String paramName, final String paramType, final String generatedCode) {
        final String searchString = "public " + paramType + " " + paramName + " { get; private set; }";
        return generatedCode.contains(searchString);
    }

    /**
     * Checks if the generated code contains a constructor for the request with the
     * specified constructor parameters
     */
    public static boolean hasConstructor(
            final String requestName,
            final ImmutableList<Arguments> args,
            final String generatedCode) {
        final String searchString = "public " + requestName + "(" + NetHelper.constructor(args) + ")";
        return generatedCode.contains(searchString);
    }

    /**
     * Checks if the client code contains the given command. This assumes that the command has a
     * payload, and that it is not void.
     */
    public static boolean hasPayloadCommand(final String commandName, final String clientCode) {
        final Pattern searchString = Pattern.compile(
                "public " + commandName + "Response " + commandName + "\\(" + commandName + "Request request\\)"
                + "\\s+\\{"
                + "\\s+return new " + commandName + "ResponseParser\\(\\)\\.Parse\\(request, _netLayer\\.Invoke\\(request\\)\\);"
                + "\\s+\\}",
                Pattern.MULTILINE | Pattern.UNIX_LINES);
        return searchString.matcher(clientCode).find();
    }

    /**
     * Checks if the client code contains the given command. This assumes that the command has
     * no payload, i.e. that it returns void
     */
    public static boolean hasVoidCommand(final String commandName, final String clientCode) {
        final Pattern searchString = Pattern.compile("public void " + commandName + "\\(" + commandName + "Request request\\)"
                + "\\s+\\{"
                + "\\s+using \\(var response = _netLayer\\.Invoke\\(request\\)\\)"
                + "\\s+\\{"
                + "\\s+ResponseParseUtilities\\.HandleStatusCode\\(response, HttpStatusCode\\.(OK)?(NoContent)?\\);"
                + "\\s+\\}"
                + "\\s+\\}",
                Pattern.MULTILINE | Pattern.UNIX_LINES);
        return searchString.matcher(clientCode).find();
    }

    public static boolean hasIDsCommand(final String commandName, final String idsClientCode) {
        final Pattern searchString = Pattern.compile(
                "(" + commandName + "Response)?(void)? " + commandName + "\\(" + commandName + "Request request\\);",
                Pattern.MULTILINE | Pattern.UNIX_LINES);
        return searchString.matcher(idsClientCode).find();
    }
}
