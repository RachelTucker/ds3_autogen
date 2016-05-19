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

package com.spectralogic.ds3autogen.python;

import com.spectralogic.ds3autogen.api.FileUtils;
import com.spectralogic.ds3autogen.api.ParserException;
import com.spectralogic.ds3autogen.api.ResponseTypeNotFoundException;
import com.spectralogic.ds3autogen.api.TypeRenamingConflictException;
import com.spectralogic.ds3autogen.python.utils.TestPythonGeneratedCode;
import com.spectralogic.ds3autogen.testutil.logging.FileTypeToLog;
import com.spectralogic.ds3autogen.testutil.logging.GeneratedCodeLogger;
import freemarker.template.TemplateModelException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class PythonFunctionalTests {

    private final static Logger LOG = LoggerFactory.getLogger(PythonFunctionalTests.class);
    private final static GeneratedCodeLogger CODE_LOGGER = new GeneratedCodeLogger(FileTypeToLog.REQUEST, LOG);

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
    public void simpleRequest() throws IOException, ResponseTypeNotFoundException, ParserException, TypeRenamingConflictException, TemplateModelException {
        final String requestName = "SimpleTestRequest";
        final FileUtils fileUtils = mock(FileUtils.class);
        final TestPythonGeneratedCode codeGenerator = new TestPythonGeneratedCode(fileUtils);

        codeGenerator.generateCode(fileUtils, "/input/requests/simpleRequest.xml");
        final String ds3Code = codeGenerator.getDs3Code();

        CODE_LOGGER.logFile(ds3Code, FileTypeToLog.REQUEST);

        //TODO add asserts
    }

    @Test
    public void deleteJobCreatedNotification() throws IOException, ResponseTypeNotFoundException, ParserException, TypeRenamingConflictException, TemplateModelException {
        final String requestName = "DeleteJobCreatedNotificationRegistrationSpectraS3Request";
        final FileUtils fileUtils = mock(FileUtils.class);
        final TestPythonGeneratedCode codeGenerator = new TestPythonGeneratedCode(fileUtils);

        codeGenerator.generateCode(fileUtils, "/input/requests/deleteJobCreatedNotificationRegistrationRequest.xml");
        final String ds3Code = codeGenerator.getDs3Code();

        CODE_LOGGER.logFile(ds3Code, FileTypeToLog.REQUEST);

        //TODO add asserts
    }
}
