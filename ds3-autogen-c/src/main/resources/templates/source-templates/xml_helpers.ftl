static bool attribute_equal(const struct _xmlAttr* attribute, const char* attribute_name) {
    return xmlStrcmp(attribute->name, (const xmlChar*) attribute_name) == 0;
}

static bool element_equal(const xmlNodePtr xml_node, const char* element_name) {
    return xmlStrcmp(xml_node->name, (const xmlChar*) element_name) == 0;
}

static uint16_t xml_get_uint16(xmlDocPtr doc, xmlNodePtr child_node) {
    xmlChar* text;
    uint16_t size;
    text = xmlNodeListGetString(doc, child_node->xmlChildrenNode, 1);
    if (text == NULL) {
        return 0;
    }
    size = atoi((char*)text);
    xmlFree(text);
    return size;
}

static uint64_t xml_get_uint16_from_attribute(xmlDocPtr doc, struct _xmlAttr* attribute) {
    return xml_get_uint16(doc, (xmlNodePtr) attribute);
}

static uint64_t xml_get_uint64(xmlDocPtr doc, xmlNodePtr child_node) {
    xmlChar* text;
    uint64_t size;
    text = xmlNodeListGetString(doc, child_node->xmlChildrenNode, 1);
    if (text == NULL) {
        return 0;
    }
    size = g_ascii_strtoull((const char*)text, NULL, 10);
    xmlFree(text);
    return size;
}

static uint64_t xml_get_uint64_from_attribute(xmlDocPtr doc, struct _xmlAttr* attribute) {
    return xml_get_uint64(doc, (xmlNodePtr) attribute);
}

static ds3_str* xml_get_string(xmlDocPtr doc, xmlNodePtr child_node) {
    xmlChar* text;
    ds3_str* result;
    text = xmlNodeListGetString(doc, child_node->xmlChildrenNode, 1);
    if (text == NULL) {
        // Element is found, but is empty: <name />
        return NULL;
    }
    result = ds3_str_init((const char*) text);
    xmlFree(text);
    return result;
}

static ds3_str* xml_get_string_from_attribute(xmlDocPtr doc, struct _xmlAttr* attribute) {
    return xml_get_string(doc, (xmlNodePtr) attribute);
}

static ds3_bool xml_get_bool(const ds3_log* log, xmlDocPtr doc, const xmlNodePtr xml_node) {
    xmlChar* text;
    ds3_bool result;
    text = xmlNodeListGetString(doc, xml_node->xmlChildrenNode, 1);
    if (xmlStrcmp(text, (xmlChar*)"true") == 0) {
        result = True;
    } else if (xmlStrcmp(text, (xmlChar*)"false") == 0) {
        result = False;
    } else {
        ds3_log_message(log, DS3_ERROR, "Unknown boolean value");
        result = False;
    }
    xmlFree(text);
    return result;
}

static ds3_bool xml_get_bool_from_attribute(const ds3_log* log, xmlDocPtr doc, struct _xmlAttr* attribute) {
    return xml_get_bool(log, doc, (xmlNodePtr) attribute);
}

static size_t _ds3_send_xml_buff(void* buffer, size_t size, size_t nmemb, void* user_data) {
    size_t to_read;
    size_t remaining;
    ds3_xml_send_buff* xml_buff;

    xml_buff = (ds3_xml_send_buff*) user_data;
    to_read = size * nmemb;
    remaining = xml_buff->size - xml_buff->total_read;

    if (remaining < to_read) {
        to_read = remaining;
    }

    strncpy((char*)buffer, xml_buff->buff + xml_buff->total_read, to_read);
    xml_buff->total_read += to_read;
    return to_read;
}

