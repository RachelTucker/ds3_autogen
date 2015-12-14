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

package com.spectralogic.ds3autogen.java.converters;

import com.google.common.collect.ImmutableList;
import com.spectralogic.ds3autogen.api.models.Classification;
import com.spectralogic.ds3autogen.api.models.Ds3ApiSpec;
import com.spectralogic.ds3autogen.api.models.Ds3Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.spectralogic.ds3autogen.java.models.Constants.SPECTRA_S3_NAMESPACING;
import static com.spectralogic.ds3autogen.utils.ConverterUtil.isEmpty;

public class NameConverter {

    private static final Logger LOG = LoggerFactory.getLogger(NameConverter.class);

    /**
     * Removes "Handler" from all request names within the spec
     * and namespaces the spectrads3 commands
     */
     public static Ds3ApiSpec renameRequests(final Ds3ApiSpec spec) {
        if (isEmpty(spec.getRequests())) {
            LOG.info("No requests to rename");
            return spec;
        }

        final ImmutableList.Builder<Ds3Request> builder = ImmutableList.builder();
        for (final Ds3Request request : spec.getRequests()) {
            builder.add(updateRequestName(request));
        }

        return new Ds3ApiSpec(builder.build(), spec.getTypes());
    }

    private static Ds3Request updateRequestName(final Ds3Request request) {
        return new Ds3Request(
                processRequestName(request),
                request.getHttpVerb(),
                request.getClassification(),
                request.getBucketRequirement(),
                request.getObjectRequirement(),
                request.getAction(),
                request.getResource(),
                request.getResourceType(),
                request.getOperation(),
                request.getDs3ResponseCodes(),
                request.getOptionalQueryParams(),
                request.getRequiredQueryParams());
    }

    private static String processRequestName(final Ds3Request request) {
        if (request.getClassification() == Classification.spectrads3) {
            final String namespacedName = request.getName().replace("Request", SPECTRA_S3_NAMESPACING + "Request");
            return stripHandlerFromName(namespacedName);
        }
        return stripHandlerFromName(request.getName());
    }

    protected static String stripHandlerFromName(final String requestName) {
        final String nameEnding = "Handler";
        if (isEmpty(requestName)) {
            return null;
        }
        if (requestName.endsWith(nameEnding)) {
            return requestName.substring(0, requestName.length() - nameEnding.length());
        }
        return requestName;
    }
}
