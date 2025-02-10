package com.assessment.dropbox.constants;

public final class ApiPaths {
    private ApiPaths() {
    }

    public static final String API_BASE_PATH = "/api";
    public static final String FILES_BASE_PATH = API_BASE_PATH + "/files";
    public static final String USERS_BASE_PATH = API_BASE_PATH + "/users";
    public static final String UPLOAD_URL_PATH = "/generate-upload-url";
    public static final String DOWNLOAD_URL_PATH = "/download-url";
    public static final String USER_FILES_PATH = "/user/{userId}";
    public static final String FILE_METADATA_PATH = "/metadata";
}