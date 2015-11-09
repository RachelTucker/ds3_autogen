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

package com.spectralogic.ds3autogen.api.models;

import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Ds3TypeMapper {

    private final static Logger LOG = LoggerFactory.getLogger(Ds3TypeMapper.class);
    private final ImmutableMap<String, ImmutableMap<String,String>> typeMapper;

    public Ds3TypeMapper(final ImmutableMap<String, ImmutableMap<String, String>> typeMapper) {
        this.typeMapper = typeMapper;
    }

    public boolean containsArgument(final String requestName, final String argName) {
        return typeMapper.containsKey(requestName) && typeMapper.get(requestName).containsKey(argName);
    }

    public String getMappedType(final String requestName, final String argName) {
        if (containsArgument(requestName, argName)) {
            return typeMapper.get(requestName).get(argName);
        }
        LOG.error("Could not map type for: " + requestName + ", " + argName);
        return null;
    }
}