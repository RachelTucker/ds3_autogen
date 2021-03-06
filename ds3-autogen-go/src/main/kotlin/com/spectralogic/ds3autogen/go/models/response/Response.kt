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

package com.spectralogic.ds3autogen.go.models.response

import com.google.common.collect.ImmutableList
import com.google.common.collect.ImmutableSet

data class Response(
        val name: String,
        val payloadStruct: String, //The struct definition for the response payload
        val expectedCodes: String,
        val parseResponseMethod: String, //Contains the Go code for the parse method if one is needed, else empty
        val responseCodes: ImmutableList<ResponseCode>)
