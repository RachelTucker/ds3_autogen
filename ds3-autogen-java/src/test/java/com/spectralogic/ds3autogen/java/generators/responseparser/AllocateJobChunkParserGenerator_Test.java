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

package com.spectralogic.ds3autogen.java.generators.responseparser;

import com.google.common.collect.ImmutableList;
import com.spectralogic.ds3autogen.api.models.apispec.Ds3ResponseCode;
import com.spectralogic.ds3autogen.api.models.apispec.Ds3ResponseType;
import com.spectralogic.ds3autogen.java.models.ResponseCode;
import org.junit.Test;

import java.util.NoSuchElementException;

import static com.spectralogic.ds3autogen.java.generators.responseparser.AllocateJobChunkParserGenerator.getDs3ResponseCode;
import static com.spectralogic.ds3autogen.java.generators.responseparser.AllocateJobChunkParserGenerator.toParsePayloadCode;
import static com.spectralogic.ds3autogen.java.generators.responseparser.AllocateJobChunkParserGenerator.toRetryLaterCode;
import static com.spectralogic.ds3autogen.java.test.helpers.Ds3ResponseCodeFixtureTestHelper.getTestResponseCodes;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class AllocateJobChunkParserGenerator_Test {

    private final static AllocateJobChunkParserGenerator generator = new AllocateJobChunkParserGenerator();

    @Test (expected = NoSuchElementException.class)
    public void getDs3ResponseCode_NullList_Test() {
        getDs3ResponseCode(null, 200);
    }

    @Test (expected = NoSuchElementException.class)
    public void getDs3ResponseCode_EmptyList_Test() {
        getDs3ResponseCode(ImmutableList.of(), 200);
    }

    @Test (expected = NoSuchElementException.class)
    public void getDs3ResponseCode_NotInList_Test() {
        getDs3ResponseCode(getTestResponseCodes(), 100);
    }

    @Test
    public void getDs3ResponseCode_Test() {
        final Ds3ResponseCode result = getDs3ResponseCode(getTestResponseCodes(), 200);
        assertThat(result.getCode(), is(200));
    }

    @Test
    public void toRetryLaterCode_Test() {
        final String expected = "return new MyResponse(null, parseRetryAfter(response), Status.RETRYLATER);";
        assertThat(toRetryLaterCode("MyResponse"), is(expected));
    }

    @Test
    public void toParsePayloadCode_Test() {
        final String expected = "try (final InputStream inputStream = new ReadableByteChannelInputStream(blockingByteChannel)) {\n" +
                "                    final JobChunkApiBean result = XmlOutput.fromXml(inputStream, JobChunkApiBean.class);\n" +
                "                    return new MyResponse(result, 0, Status.ALLOCATED);\n" +
                "                }\n";

        final Ds3ResponseCode ds3ResponseCode = new Ds3ResponseCode(
                200,
                ImmutableList.of(new Ds3ResponseType("com.spectralogic.s3.server.domain.JobChunkApiBean", null)));

        final String result = toParsePayloadCode(ds3ResponseCode, "MyResponse");
        assertThat(result, is(expected));
    }

    @Test (expected = IllegalArgumentException.class)
    public void toResponseCodeList_Error_Test() {
        generator.toResponseCodeList(ImmutableList.of(), "TestResponse");
    }

    @Test
    public void toResponseCodeList_Test() {
        final ImmutableList responseCodes = ImmutableList.of(
                new Ds3ResponseCode(
                        200,
                        ImmutableList.of(
                                new Ds3ResponseType("com.spectralogic.s3.server.domain.JobChunkApiBean", null))));

        final ImmutableList<ResponseCode> result = generator.toResponseCodeList(responseCodes, "TestResponse");
        assertThat(result.size(), is(3));
        assertThat(result.get(0).getCode(), is(200));
        assertThat(result.get(1).getCode(), is(307));
        assertThat(result.get(2).getCode(), is(503));
    }
}
