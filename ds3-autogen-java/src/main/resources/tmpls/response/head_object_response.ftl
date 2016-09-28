<#include "../copyright.ftl"/>

package ${packageName};

<#include "../imports.ftl"/>

public class ${name} extends ${parentClass} {

    public enum Status { EXISTS, DOESNTEXIST, UNKNOWN }

    <#list params as param>
    private final ${param.type} ${param.name?uncap_first};
    </#list>

    public ${name}(${constructorParams}) {
        <#list params as param>
        this.${param.name?uncap_first} = this.${param.name?uncap_first};
        </#list>
    }

    <#list params as param>
    public ${param.type} get${param.name?cap_first}() {
        return this.${param.name?uncap_first};
    }
    </#list>
}