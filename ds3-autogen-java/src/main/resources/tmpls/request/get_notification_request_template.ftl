<#include "../copyright.ftl"/>

package ${packageName};

<#include "../imports.ftl"/>

public class ${name} extends ${parentClass} {

    // Variables
    <#include "common/variables.ftl"/>

    // Constructor
<#include "common/notification_constructor.ftl"/>

    <#include "common/with_constructors.ftl"/>

    @Override
    public String getPath() {
        return ${path};
    }

    <#include "common/getters.ftl"/>
}