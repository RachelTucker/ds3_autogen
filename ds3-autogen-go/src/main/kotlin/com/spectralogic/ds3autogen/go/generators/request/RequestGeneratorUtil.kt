/*
 * ******************************************************************************
 *   Copyright 2014-2017 Spectra Logic Corporation. All Rights Reserved.
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

package com.spectralogic.ds3autogen.go.generators.request

import com.google.common.collect.ImmutableList
import com.spectralogic.ds3autogen.api.models.Arguments
import com.spectralogic.ds3autogen.api.models.apispec.Ds3Param
import com.spectralogic.ds3autogen.go.models.request.SimpleVariable
import com.spectralogic.ds3autogen.go.models.request.Variable
import com.spectralogic.ds3autogen.go.models.request.WithConstructor
import com.spectralogic.ds3autogen.go.utils.toGoParamName
import com.spectralogic.ds3autogen.go.utils.toGoType
import com.spectralogic.ds3autogen.utils.ConverterUtil
import com.spectralogic.ds3autogen.utils.ConverterUtil.isEmpty
import com.spectralogic.ds3autogen.utils.Helper
import com.spectralogic.ds3autogen.utils.Helper.camelToUnderscore
import com.spectralogic.ds3autogen.utils.Helper.uncapFirst
import com.spectralogic.ds3autogen.utils.NormalizingContractNamesUtil
import com.spectralogic.ds3autogen.utils.collections.GuavaCollectors
import com.spectralogic.ds3autogen.utils.comparators.CustomArgumentComparator
import java.util.stream.Collectors

/**
 * Contains utilities used in by Go request generators
 */


/**
 * Removes parameters with type void
 */
fun removeVoidDs3Params(ds3Params: ImmutableList<Ds3Param>?): ImmutableList<Ds3Param> {
    if (ConverterUtil.isEmpty(ds3Params)) {
        return ImmutableList.of()
    }
    return ds3Params!!.stream()
            .filter{ param -> !param.type.equals("void", ignoreCase = true) }
            .collect(GuavaCollectors.immutableList<Ds3Param>())
}

/**
 * Removes parameters with either type void or name operation
 */
fun removeVoidAndOperationDs3Params(ds3Params: ImmutableList<Ds3Param>?): ImmutableList<Ds3Param> {
    if (ConverterUtil.isEmpty(ds3Params)) {
        return ImmutableList.of()
    }
    return ds3Params!!.stream()
            .filter{ param -> !param.type.equals("void", ignoreCase = true)
                           && !param.name.equals("Operation", ignoreCase = true) }
            .collect(GuavaCollectors.immutableList<Ds3Param>())
}

/**
 * Converts a list of required Ds3Params into a list of Arguments. Returns an empty list
 * if requiredParams is null or empty.
 */
fun toGoArgumentsList(requiredParams: ImmutableList<Ds3Param>?): ImmutableList<Arguments> {
    if (ConverterUtil.isEmpty(requiredParams)) {
        return ImmutableList.of()
    }
    return requiredParams!!.stream()
            .map(::toGoArgument)
            .collect(GuavaCollectors.immutableList())
}

/**
 * Converts a Ds3Param into an Argument containing the corresponding Go type and parameter name
 */
fun toGoArgument(ds3Param: Ds3Param): Arguments {
    val goType = toGoType(ds3Param.type, ds3Param.nullable)
    val goName = toGoParamName(ds3Param.name, ds3Param.type)
    return Arguments(goType, Helper.uncapFirst(goName))
}

/**
 * Transforms a list of Arguments into a comma-separated list of function input parameters.
 * The parameters are sorted according to Argument name: BucketName, ObjectName, alphabetical.
 */
fun toFunctionInput(arguments: ImmutableList<Arguments>): String {
    return arguments.stream()
            .sorted(CustomArgumentComparator())
            .map { arg -> uncapFirst(arg.name) + " " + arg.type }
            .collect(Collectors.joining(", "))
}

/**
 * Converts a list of Ds3Param into a list of Variables which represent query
 * parameter assignments.
 */
fun toQueryParamVarList(ds3Params: ImmutableList<Ds3Param>?): ImmutableList<Variable> {
    if (isEmpty(ds3Params)) {
        return ImmutableList.of()
    }
    return ds3Params!!.stream()
            .filter { param -> !param.name.equals("Operation", ignoreCase = true) }
            .map(::toQueryParamVar)
            .collect(GuavaCollectors.immutableList())
}

/**
 * Creates the variable that represents the key-value pair that will be added to the
 * query parameters in the request handler. The parameter value must be converted to
 * a string.
 */
fun toQueryParamVar(ds3Param: Ds3Param): Variable {
    val goType = toGoType(ds3Param.type, ds3Param.nullable)
    val goName = toGoParamName(ds3Param.name, ds3Param.type)
    val key = camelToUnderscore(ds3Param.name)
    return Variable(key, goVarToString(uncapFirst(goName), goType))
}

/**
 * Creates the Go code for transforming a variable to string based on its Go type
 */
fun goVarToString(name: String, goType: String): String {
    when (goType) {
        "" -> return "\"\"" // Denotes void parameter in contract
        "int" -> return "strconv.Itoa($name)"
        "bool" -> return "strconv.FormatBool($name)"
        "int64" -> return "strconv.FormatInt($name, 10)"
        "float64" -> return "strconv.FormatFloat($name, 'f', -1, 64)"
        "string" -> return name
        "*int" -> return "strconv.Itoa(*$name)"
        "*bool" -> return "strconv.FormatBool(*$name)"
        "*int64" -> return "strconv.FormatInt(*$name, 10)"
        "*float64" -> return "strconv.FormatFloat(*$name, 'f', -1, 64)"
        "*string" -> return "*$name"
        else -> return name + ".String()"
    }
}

/**
 * Determines if the Go conversion of the specified contract type into a string
 * requires the import of strconv
 */
fun usesStrconv(contractType: String): Boolean {
    val type = NormalizingContractNamesUtil.removePath(contractType)
    when (type.toLowerCase()) {
        "boolean", "integer", "int", "double", "long" -> return true
        else -> return false
    }
}

/**
 * Converts a list of Ds3Params into a list of SimpleVariables. This is used to create
 * assignments within the request constructor to the request struct.
 */
fun toSimpleVariables(ds3Params: ImmutableList<Ds3Param>?): ImmutableList<SimpleVariable> {
    if (isEmpty(ds3Params)) {
        return ImmutableList.of()
    }
    return ds3Params!!.stream()
            .map{ param -> SimpleVariable(uncapFirst(toGoParamName(param.name, param.type))) }
            .collect(GuavaCollectors.immutableList())
}

/**
 * Converts a list of Ds3Params into a list of with-constructors, and filters for parameters
 * whose nullability matches nullableParams
 */
fun toWithConstructors(optionalParams: ImmutableList<Ds3Param>?, nullableParams: Boolean): ImmutableList<WithConstructor> {
    if (isEmpty(optionalParams)) {
        return ImmutableList.of()
    }
    return optionalParams!!.stream()
            .filter { param -> param.nullable == nullableParams }
            .filter { param -> !param.type.equals("void", ignoreCase = true) }
            .map(::toWithConstructor)
            .collect(GuavaCollectors.immutableList())
}

/**
 * Converts a Ds3Param into a with-constructor with Go parameter naming schemes
 */
fun toWithConstructor(ds3Param: Ds3Param): WithConstructor {
    val goName = toGoParamName(ds3Param.name, ds3Param.type)
    val goType = toGoType(ds3Param.type, ds3Param.nullable)
    return WithConstructor(
            goName,
            goType,
            camelToUnderscore(ds3Param.name),
            goVarToString(uncapFirst(goName), goType))
}
