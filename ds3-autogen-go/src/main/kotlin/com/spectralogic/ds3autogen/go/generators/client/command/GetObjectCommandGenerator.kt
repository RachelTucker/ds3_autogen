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

package com.spectralogic.ds3autogen.go.generators.client.command

import com.spectralogic.ds3autogen.go.models.client.CustomBuildLine
import com.spectralogic.ds3autogen.go.models.client.RequestBuildLine
import java.util.*

/**
 * Generates the Go client command for Amazon GetObject and CompleteBlob.
 * Adds checksum and metadata to the request construction.
 */
class GetObjectCommandGenerator : BaseCommandGenerator() {

    /**
     * Retrieves the request builder line that adds the optional checksum.
     */
    override fun toChecksumBuildLine(): Optional<RequestBuildLine> {
        return Optional.of(CustomBuildLine.CHECKSUM_BUILD_LINE)
    }

    /**
     * Retrieves the request builder line that adds metadata which may
     * include the range header.
     */
    override fun toHeadersBuildLine(): Optional<RequestBuildLine> {
        return Optional.of(CustomBuildLine.METADATA_BUILD_LINE)
    }
}