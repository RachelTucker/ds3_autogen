/*
 * ******************************************************************************
 *   Copyright 2014-2016 Spectra Logic Corporation. All Rights Reserved.
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

package com.spectralogic.ds3autogen.python.helpers;

import com.google.common.collect.ImmutableList;
import com.spectralogic.ds3autogen.api.models.Arguments;

import java.util.stream.Collectors;

import static com.spectralogic.ds3autogen.utils.ConverterUtil.isEmpty;
import static com.spectralogic.ds3autogen.utils.Helper.camelToUnderscore;

/**
 * Series of static functions that are used within the Python module template files
 * to help generate the Python SDK code.
 */
public final class PythonHelper {

    private final static PythonHelper pythonHelper = new PythonHelper();

    private PythonHelper() {
        //pass
    }

    public static PythonHelper getInstance() {
        return pythonHelper;
    }

    /**
     * Creates a comma separated list of all required and optional arguments
     * for use in the request handler init function.
     */
    public static String toRequestInitList(
            final ImmutableList<Arguments> requiredArgs,
            final ImmutableList<Arguments> optionalArgs) {
        final String optionalInits = toOptionalArgInitList(optionalArgs);
        if (isEmpty(optionalInits)) {
            return toRequiredArgInitList(requiredArgs);
        }
        return toRequiredArgInitList(requiredArgs) + ", " + optionalInits;
    }

    /**
     * Creates a comma separated list of all optional arguments for use in the
     * request handler init function. Optional arguments are set to None.
     */
    protected static String toOptionalArgInitList(final ImmutableList<Arguments> optionalArgs) {
        if (isEmpty(optionalArgs)) {
            return "";
        }
        return optionalArgs.stream()
                .map(i -> camelToUnderscore(i.getName()) + "=None")
                .collect(Collectors.joining(", "));
    }

    /**
     * Creates a comma separated list of all required arguments for use in the
     * request handler init function. This list will always be started with 'self'
     */
    protected static String toRequiredArgInitList(final ImmutableList<Arguments> requiredArgs) {
        if (isEmpty(requiredArgs)) {
            return "self";
        }
        return "self, " + requiredArgs.stream()
                .map(i -> camelToUnderscore(i.getName()))
                .collect(Collectors.joining(", "));
    }
}
