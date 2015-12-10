package com.spectralogic.ds3autogen.c.utils;

import com.spectralogic.ds3autogen.api.FileUtils;
import org.junit.rules.TemporaryFolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;

public class TestFileUtilImpl implements FileUtils {

    private final static Logger LOG = LoggerFactory.getLogger(TestFileUtilImpl.class);

    private final TemporaryFolder tempFolder;

    private OutputStream outputStream;

    public TestFileUtilImpl() {
        this.tempFolder = null;
        try {
            this.outputStream = getOutputFile(null);
        } catch (final IOException e) {
            LOG.error("unable to create ByteArrayOutputStream", e);
            e.printStackTrace();
        }
    }

    @Override
    public OutputStream getOutputFile(final Path path) throws IOException {
        if (path == null || this.outputStream instanceof ByteArrayOutputStream) {
            return getOutputStream();
        } else {
            final Path parentPath = path.getParent();

            final String[] pathParts = parentPath.toString().split("/|\\\\");
            try {
                tempFolder.newFolder(pathParts);
            } catch (final RuntimeException | IOException e) {
                LOG.error("Attempted to create folder that already exists -- ignoring", e);
            }

            this.outputStream = new FileOutputStream(tempFolder.newFile(path.toString()));
        }
        return getOutputStream();
    }

    public OutputStream getOutputStream() {
        if (this.outputStream == null) {
            this.outputStream = new ByteArrayOutputStream(1024 * 8);
        }
        return this.outputStream;
    }
}
