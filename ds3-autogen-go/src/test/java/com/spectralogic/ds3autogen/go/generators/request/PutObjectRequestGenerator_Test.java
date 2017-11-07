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

package com.spectralogic.ds3autogen.go.generators.request;

import com.google.common.collect.ImmutableList;
import com.spectralogic.ds3autogen.api.models.Arguments;
import com.spectralogic.ds3autogen.go.models.request.SimpleVariable;
import com.spectralogic.ds3autogen.go.models.request.Variable;
import com.spectralogic.ds3autogen.go.models.request.VariableInterface;
import org.junit.Test;

import static com.spectralogic.ds3autogen.testutil.Ds3ModelFixtures.getRequestCreateObject;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class PutObjectRequestGenerator_Test {

    private final PutObjectRequestGenerator generator = new PutObjectRequestGenerator();

    @Test
    public void toStructAssignmentParams_Test() {
        final ImmutableList<VariableInterface> expectedVars = ImmutableList.of(
                new SimpleVariable("bucketName"),
                new SimpleVariable("objectName"),
                new SimpleVariable("content"),
                new Variable("Checksum", "networking.NewNoneChecksum()"),
                new Variable("Metadata", "make(map[string]string)")
        );

        final ImmutableList<VariableInterface> result = generator.toStructAssignmentParams(getRequestCreateObject());

        assertThat(result.size(), is(expectedVars.size()));
        expectedVars.forEach(expected -> assertThat(result, hasItem(expected)));
    }

    @Test
    public void toConstructorParamsList_Test() {
        final ImmutableList<Arguments> expectedArgs = ImmutableList.of(
                new Arguments("string", "BucketName"),
                new Arguments("string", "ObjectName"),
                new Arguments("networking.ReaderWithSizeDecorator", "content")
        );

        final ImmutableList<Arguments> result = generator.toConstructorParamsList(getRequestCreateObject());

        assertThat(result.size(), is(expectedArgs.size()));
        for (int i = 0; i < result.size(); i++) {
            assertThat(result.get(i).getName(), is(expectedArgs.get(i).getName()));
            assertThat(result.get(i).getType(), is(expectedArgs.get(i).getType()));
        }
    }

    @Test
    public void toStructParams_Test() {
        final ImmutableList<Arguments> expectedArgs = ImmutableList.of(
                new Arguments("string", "BucketName"),
                new Arguments("string", "ObjectName"),
                new Arguments("networking.Checksum", "Checksum"),
                new Arguments("networking.ReaderWithSizeDecorator", "Content"),
                new Arguments("*string", "Job"),
                new Arguments("map[string]string", "Metadata"),
                new Arguments("*int64", "Offset")

        );

        final ImmutableList<Arguments> result = generator.toStructParams(getRequestCreateObject());

        assertThat(result.size(), is(expectedArgs.size()));
        for (int i = 0; i < result.size(); i++) {
            assertThat(result.get(i).getName(), is(expectedArgs.get(i).getName()));
            assertThat(result.get(i).getType(), is(expectedArgs.get(i).getType()));
        }
    }
}
