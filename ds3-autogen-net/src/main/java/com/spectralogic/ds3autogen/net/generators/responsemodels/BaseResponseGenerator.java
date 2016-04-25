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

package com.spectralogic.ds3autogen.net.generators.responsemodels;

import com.spectralogic.ds3autogen.api.models.Ds3Request;
import com.spectralogic.ds3autogen.net.NetHelper;
import com.spectralogic.ds3autogen.net.model.response.BaseResponse;
import com.spectralogic.ds3autogen.utils.NormalizingContractNamesUtil;

import static com.spectralogic.ds3autogen.utils.NormalizingContractNamesUtil.removePath;

public class BaseResponseGenerator implements ResponseModelGenerator<BaseResponse> {

    @Override
    public BaseResponse generate(final Ds3Request ds3Request, final String responseType) {
        final String name = NormalizingContractNamesUtil.toResponseName(ds3Request.getName());
        final String netResponseType = toNetResponseType(responseType);

        return new BaseResponse(
                name,
                netResponseType);
    }

    /**
     * Converts a contract response type into the .net response type
     */
    public static String toNetResponseType(final String responseType) {
        return NetHelper.toNetType(removePath(responseType));
    }
}
