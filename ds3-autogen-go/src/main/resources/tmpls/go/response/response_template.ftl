<#include "../common/copyright.ftl" />

package models

import (
    "net/http"
    <#list imports as import>
    "${import}"
    </#list>
)

type ${name} struct {
    ${payloadStruct}
    Headers *http.Header
}

${parseResponseMethod}

func New${name}(webResponse WebResponse) (*${name}, error) {
    expectedStatusCodes := []int { ${expectedCodes} }

    switch code := webResponse.StatusCode(); code {
    <#list responseCodes as code>
    case ${code.code}:
        ${code.parseResponse}
    </#list>
    default:
        return nil, buildBadStatusCodeError(webResponse, expectedStatusCodes)
    }
}
