
ds3_error* ds3_get_object_request(const ds3_client* client, const ds3_request* request, void* user_data, size_t (* callback)(void*, size_t, size_t, void*)) {
    ds3_error* error;

    int num_slashes = num_chars_in_ds3_str(request->path, '/');
    if (num_slashes < 2 || ((num_slashes == 2) && ('/' == request->path->value[request->path->size-1]))) {
        return ds3_create_error(DS3_ERROR_MISSING_ARGS, "The bucket name parameter is required.");
    } else if (g_ascii_strncasecmp(request->path->value, "//", 2) == 0) {
        return ds3_create_error(DS3_ERROR_MISSING_ARGS, "The object name parameter is required.");
    }

    error = _internal_request_dispatcher(client, request, user_data, callback, NULL, NULL, NULL);

    return error;
}
