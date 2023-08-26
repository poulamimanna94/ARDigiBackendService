package com.siemens.web;

public final class URLConstants
{
    
    public static final String URL_GET_LICENSE_VALIDATION = "/getLicenseValidation";
    public static final String URL_REFRESH_DATA = "/refreshData";
    public static final String URL_API_OPC_ROOT = "/api/opc";
    public static final String API_OPC_CONNECT_BASEURL = "localhost:8890";
    public static final String URL_GET_CONNECTION_COUNT = "/getConnectionCount";
    public static final String URL_LOGIN = "/login";
    public static final String URL_USERS_ROLE_PERMISSIONS = "/users/role-permissions";
    public static final String URL_DEL_MULTIPLE_USERS = "/delMultipleUsers";
    public static final String URL_API_USERS = "/api/users/";
    public static final String URL_USER_RESOURCE_ROOT = "/api/i";
    
    private URLConstants()
    {
        // intentionally created empty constructor to hide the implicit one
    }
    
    public static final String URL_API_ROOT = "/api";
    
    // client configuration resource
    public static final String URL_CREATE_CLIENT_CONFIGURATION = "/create-client-configuration";
    public static final String URL_GET_ALL_CLIENT_CONFIGURATION = "/get-all-client-configuration";
    public static final String URL_UPDATE_CLIENT_CONFIGURATION = "/update-client-configuration";
    public static final String URL_DELETE_CLIENT_CONFIGURATION = "/delete-client-configuration";
    public static final String URL_GET_CLIENT_CONFIGURATION = "/get-client-configuration";
    public static final String URL_GET_CLIENT_CONFIGURATION_LIST = "/get-client-configuration-list";
    public static final String URL_GET_CLIENT_NODE_TREE = "/get-client-node-tree";
    // client node tree resource
    public static final String URL_API_CLIENT_NODE_TREE = "/api/client-node-tree";
    public static final String URL_GET_NODE_TREE = "/get-node-tree";
    public static final String URL_GET_NODES = "/get-nodes";
    // dashboard
    public static final String URL_DASHBOARD = "/dashboard";
    public static final String URL_GET_ALL_MEMORY_DATA = "/getAllMemoryData";
    public static final String URL_GET_NETWORK_SPEED = "/getNetWorkSpeed";
    public static final String URL_GET_NETWORK_VBS_SPEED = "/getNetWorkVBSSpeed";
    // hda import export
    public static final String URL_RUN_IMPORT = "/run_import";
    public static final String URL_BYPASS_HDA_IMPORT = "/bypass/hda_import";
    // log4j
    public static final String URL_API_LOG4J = "/api/log4j";
    public static final String URL_GET_ALL = "/getAll";
    public static final String URL_GET_LIST_LOG_FILES = "/getListLogFile";
    public static final String URL_GET_LOG_DESC_LIMIT = "/getLogDescByLimit";
    public static final String URL_DOWNLOAD_FILE = "/downloadFile/";
    public static final String PATH_PARAM_FILE_NAME = "{fileName}";
    // manager server state resource
    public static final String URL_API_CLIENT_STATE = "/api/client-state";
    public static final String URL_START_CLIENT_SERVER = "/start-client-server";
    public static final String URL_STOP_CLIENT_SERVER = "/stop-client-server";
    public static final String URL_CHECK_CLIENT_SERVER_PORT = "/check-client-server-port";
    
    public static final String URL_GET_LICENSE = "/getLicense";
    public static final String URL_CHECK_EXPIRED_LICENSE_WARNING_SCHEDULER = "/checkExpiredLicenseWarningSchedular";
    public static final String URL_API_SIGNAL_FORM = "/api/signal-alarm";
    public static final String URL_GET_ALL_CACHE = "/getAllCache";
    public static final String URL_CREATE_CACHE = "/create-cache";
    public static final String URL_CREATE_CACHE_SIGNAL = "/create-cache-signal";
    public static final String URL_CREATE_CACHE_ALARM = "/create-cache-alarm";
    public static final String URL_GET_ALL_SIGNAL = "/getAllSiganl";
    public static final String URL_GET_PAGINATION_SIGNAL = "/getPaginatedSignal/{pageNo}/{pageSize}";
    public static final String URL_GET_ALL_ALARMS = "/getAllAlarm";
    public static final String URL_GET_PAGINATION_ALARM = "/getPaginatedAlarm/{pageNo}/{pageSize}";
    public static final String URL_GET_SIGNAL_NODE_ALL_CHILED = "/getSignalNodeAllChiled/{level}/{name}";
    public static final String URL_GET_ALARM_NODE_ALL_CHILED = "/getAlarmNodeAllChiled/{level}/{name}";
    public static final String URL_API_SIGNAL_TAG = "/api/signal-tag";
    public static final String URL_SAVE = "/save";
    public static final String URL_UPDATE = "/update";
    public static final String URL_DELETE_BY_ENTITY = "/deleteByEntity";
    public static final String URL_GET_SUBSCRIPTION_BY_NAME = "/getBySubscriptionName/{subscriptionName}";
    public static final String URL_GET_SIGNAL_DETAILS = "/getSignalDetails/{signalID}";
    public static final String URL_GET_SIGNAL_VALUE = "/getSignalValue/{toTime}/{fromTime}";
    public static final String URL_GET_ALARM_ACTIVE = "/getAlarmActive/{signalID}";
    public static final String URL_GET_ALL_ALARM_DETAILS = "/getAllAlarmDetails";
    public static final String URL_GET_ALL_ARCHIVE_SIGNAL = "/getByAllArchiveSignal/{archive}}";
    public static final String URL_GET_ALL_BY_CLIENT_ID = "/getByClientId/{clientId}";
    public static final String URL_GET_DB_TREE_NODE = "/get-db-tree-node";
    public static final String URL_GET_CLIENT_ARCHIVE_TAGS = "/get-client-archive-tags";
    public static final String URL_GET_CLIENT_SIGNAL_ARCHIVE_DATA = "/get-client-signal-archived-data";
    public static final String URL_CONFIG_USER_LIMIT = "/config-userlimit";
    public static final String URL_UPLOAD = "/upload";
    public static final String URL_LOGO_DOWNLOAD = "/logo/download";
    public static final String URL_API_SYSTEM_CONFIG = "/api/i/system-config";
    public static final String URL_API_USER_AUTH = "/api/user-auth";
    public static final String URL_API_DASHBOARD = "/api/dashboard";
    public static final String URL_API_EHS = "/api/ehs";
    public static final String URL_AUTHORIZE = "/authorize";
    public static final String URL_SYNC = "/i/sync";
    public static final String URL_USERS_AUTH_PROVIDERS = "/users/auth-provider";
    public static final String URL_USERS_AUTH_PROVIDERS_ROLES = "/users/auth-provider-roles";
    public static final String URL_API_CLIENT_CONFIGURATION = "/api/client-configuration";
    public static final String URL_GET_CLIENT_SIGNAL_ARCHIVE_INTERVAL_DATA = "/get-client-signal-archived-interval-data";
    public static final String URL_ASSET_TEMPLATE_DOWNLOAD = "/asset_template/download";
    
    // user resource rest web
    public static final String URL_USERS_ROOT = "/users";
    public static final String URL_USERS = "/users";
    public static final String URL_UPDATE_USER_ROLE_PERMISSION = "/update-user-role-permission";
    public static final String URL_GET_USER_ROLE_PERMISSION = "/update-user-role-permission";
    public static final String URL_USERS_AUTHORITIES = "/users/authorities";
    public static final String LOGIN_REGEX = "^(?>[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*)|(?>[_.@A-Za-z0-9-]+)$";
    public static final String URL_DELETE_USER = "/users/{login:" + LOGIN_REGEX + "}";
    public static final String URL_GET_USER = "/users/get-user";
    public static final String URL_GET_ALL_USER = "/users/get-all-user";
    public static final String URL_LOGOUT_USER = "/users/logout";
    
    // dashboard graph controller
    public static final String URL_DASHBOARD_GRAPH = "/dashboard-graphs";
    public static final String URL_DASHBOARD_GRAPH_SAVE = "/save";
    public static final String URL_DASHBOARD_GRAPH_GET_GRAPH_CONFIG = "/get-graph-config";
    
    // Asset API
    public static final String ASSET_API_ROOT = "/asset";
    
    public static final String SUCCESSFULLY_MODIFIED = "Successfully modified record.";
    public static final String RECORD_FOUND = "Found following records.";
    public static final String NO_RECORD_FOUND = "No record found.";
}
