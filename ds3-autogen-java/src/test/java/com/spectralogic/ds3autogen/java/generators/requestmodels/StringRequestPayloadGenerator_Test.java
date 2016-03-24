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

package com.spectralogic.ds3autogen.java.generators.requestmodels;

import com.google.common.collect.ImmutableList;
import com.spectralogic.ds3autogen.api.models.Arguments;
import org.junit.Test;

import static com.spectralogic.ds3autogen.testutil.Ds3ModelFixtures.getGetBlobPersistence;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class StringRequestPayloadGenerator_Test {

    private final static StringRequestPayloadGenerator generator = new StringRequestPayloadGenerator();

    @Test
    public void toConstructorArgumentsList_Test() {
        final ImmutableList<Arguments> result = generator.toConstructorArgumentsList(getGetBlobPersistence());
        assertThat(result.size(), is(1));
        assertThat(result.get(0).getName(), is("RequestPayload"));
        assertThat(result.get(0).getType(), is("String"));
    }
}
