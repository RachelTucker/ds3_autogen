<#include "../copyright.ftl"/>

package ${packageName};

<#include "common/import_abstract_request.ftl"/>
import com.spectralogic.ds3client.HttpVerb;
import java.io.InputStream;
import java.nio.channels.SeekableByteChannel;
<#include "../imports.ftl"/>
<#include "common/checksum_import.ftl"/>

public class ${name} extends AbstractRequest {

    // Variables
    public final static String AMZ_META_HEADER = "x-amz-meta-";

    private final InputStream stream;
    private final long size;
    <#include "common/variables.ftl"/>
    private SeekableByteChannel channel;
<#include "common/checksum_variables.ftl"/>

    // Constructor

    /**
     * @deprecated use {@link #${name}(${javaHelper.argTypeList(
                                         helper.addArgument(
                                         helper.addArgument(
                                         helper.addArgument(requiredArguments, optionalArguments), "Size", "long"), "Channel", "SeekableByteChannel"))}) instead
     */
    @Deprecated
    public ${name}(${javaHelper.constructorArgs(
                     helper.addArgument(
                     helper.addArgument(requiredArguments, "Size", "long"), "Channel", "SeekableByteChannel"))}) {
        <#list requiredArguments as arg>
        this.${arg.getName()?uncap_first} = ${arg.getName()?uncap_first};
        </#list>
        this.size = size;
        this.channel = channel;
        this.stream = new SeekableByteChannelInputStream(channel);
    }

    public ${name}(${javaHelper.constructorArgs(
                     helper.addArgument(
                     helper.addArgument(
                     helper.addArgument(requiredArguments, optionalArguments), "Size", "long"), "Channel", "SeekableByteChannel"))}) {
        this(${javaHelper.modifiedArgNameList(
               helper.addArgument(
               helper.addArgument(
               helper.addArgument(
                   requiredArguments, optionalArguments), "Size", "long"), "Stream", "InputStream"), "Stream", "new SeekableByteChannelInputStream(channel)")});

        this.channel = channel;
    }

    public ${name}(${javaHelper.constructorArgs(
                     helper.addArgument(
                     helper.addArgument(
                     helper.addArgument(
                         requiredArguments, optionalArguments), "Size", "long"), "Stream", "InputStream"))}) {
            <#list requiredArguments as arg>
            this.${arg.getName()?uncap_first} = ${arg.getName()?uncap_first};
            </#list>
            <#list optionalArguments as arg>
            this.${arg.getName()?uncap_first} = ${arg.getName()?uncap_first};
            </#list>
            this.size = size;
            this.stream = stream;

            <#list optionalArguments as arg>
            ${javaHelper.putQueryParamLine(arg)}
            </#list>
        }

    <#include "common/with_constructors.ftl"/>

<#include "common/checksum_constructor_getter.ftl"/>

	public ${name} withMetaData(final String key, final String value) {
		final String modifiedKey;
		if (!key.toLowerCase().startsWith(AMZ_META_HEADER)){
			modifiedKey = AMZ_META_HEADER + key;
		} else {
			modifiedKey = key;
		}
		this.getHeaders().put(modifiedKey, value);
		return this;
	}

    <#include "common/getters_verb_path.ftl"/>

    @Override
    ${javaHelper.createGetter("Size", "long")}

    @Override
    ${javaHelper.createGetter("Stream", "InputStream")}

    <#include "common/getters.ftl"/>

    ${javaHelper.createGetter("Channel", "SeekableByteChannel")}
}