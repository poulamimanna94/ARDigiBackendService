package com.siemens.constant;

/**
 * Application constants.
 */
public final class Constants
{
    
    public static final String APPLICATION_CLIENT_APP_NAME = "${application.clientApp.name}";
    public static final String APPLICATION_USER_LIMIT = "${application.user-limit}";
    public static final String DISCOVERY = "discovery";
    public static final String SUCCESS = "SUCCESS";
    public static final String SIGNAL_TAG_CONFIG = "SignalTagConfig";
    public static final String SIGNAL_TAG_CONFIG_SUB_NAME_PRESSURE = "3:Pressure";
    public static final String SIGNAL_TAG_CONFIG_SUB_NAME_SPEED = "3:Speed";
    public static final String PROPERTY_ID = "id";
    public static final String SIEFLUX_LOGGER_CLIENT_PATTERN = "[%-5level] %d{yyyy-MM-dd HH:mm:ss.SSS} [%t] %c{1} - %msg%n";
    public static final String CONFIG_PROPERTY_DEFAULT_LOGGER = "DefaultLogger";
    public static final String PROPERTY_APPENDER_NAME_CONSOLE = "Console";
    public static final String PROPERTY_PLUGIN_NAME_CONSOLE = "CONSOLE";
    public static final String PROPERTY_TARGET = "target";
    public static final String PLUGIN_NAME_PATTERN_LAYOUT = "PatternLayout";
    public static final String PROPERTY_PATTERN = "pattern";
    public static final String PLUGIN_NAME_POLICIES = "Policies";
    public static final String PLUGIN_NAME_SIZED_BASED_TRIGGER_POLICY = "SizeBasedTriggeringPolicy";
    public static final String PROPERTY_FILE_NAME = "fileName";
    public static final String PROPERTY_FILE_PATTERN = "filePattern";
    public static final String PROPERTY_LOG_TO_ROLLING_FILE = "LogToRollingFile";
    public static final String PLUGIN_NAME_ROLLING_FILE = "RollingFile";
    public static final String COMMAND_DATA_OK = "ok";
    public static final String PROPERTY_CONFIG_WITH_SUFFIX_UNDERSCORE = "config_";
    public static final String FILE_EXTENSION_JSON = ".json";
    public static final Object PROPERTY_MY_STRUCTURE_TYPE = "MyStructureType";
    public static final Object PROPERTY_FIELD2_TEST = "Test";
    
    private Constants()
    {
        // hide the implicit one
    }
    
    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^(?>[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*)|(?>[_.@A-Za-z0-9-]+)$";
    
    public static final String XML_FILE_EXT = ".xml";
    
    public static final String SYSTEM_ACCOUNT = "system";
    public static final String DEFAULT_LANGUAGE = "en";
    public static final String ANONYMOUS_USER = "anonymoususer";
    
    public static final String T3000_CONNECTOR_TYPE = "T3000";
    public static final String OPCUA_CONNECTOR_TYPE = "OPCUA";
    
    public static final int T3000_CLIENT_CONFIG_ID_MIN = 10000;
    public static final int T3000_CLIENT_CONFIG_ID_MAX = 50000;
    
    public static final int OPCUA_CLIENT_CONFIG_ID_MIN = 55000;
    public static final int OPCUA_CLIENT_CONFIG_ID_MAX = 99999;
    
    public static final String T3000_CLIENT_CONFIG_FILE_NAME = "T3000ClientConfig.xml";
    public static final String OPCUA_CLIENT_CONFIG_FILE_NAME = "OpcUaClientConfig.xml";
    public static final String CONTENT_TYPE_APP_OCTET_STREAM = "application/octet-stream";
    
    public static final String OPCUA_STANDALONE_CLIENT_CONFIG_FILE_NAME = "clientConfigModel_";
    
    public static final String AUTHORIZATION_HEADER = "Authorization";
    
    public static final String CACHE_SIGNAL_ALARM_FILE_NAME = "C:\\Users\\shail\\Desktop\\cachefile (2).xml";
    
    // log
    public static final String DIRECTORY_SERVER_LOG = "logs/";
    public static final String DIRECTORY_CLIENT_LOG = "logs/clients/";
    public static final String FILE_PATH_LOG = "logs/server_sieflux.log";
    
    // dasboard
    public static final long MEGABYTE = 1024L * 1024L;
    public static final long GEGABYTE = 1024L * 1024L * 1024L;
    public static final long TERAABYTE = 1024L * 1024L * 1024L * 1024L;
    
    public static final String RECORD_FETCHED_SUCCESSFULLY = " Record fetched successfully.";
    public static final String RECORD_DELETED_SUCCESSFULLY = " Record deleted successfully.";
    public static final String RECORD_SAVED_SUCCESSFULLY = " Record saved successfully.";
    public static final String RECORD_LIST_SUCCESSFULLY = " List fetched successfully.";
    public static final String RECORD_UPDATE_SUCCESSFULLY = " Record updated successfully.";
    public static final String CLIENT_ALIVE_STATUS = " Current Client alive status.";
    
    public static final String ERROR = "Error: ";
    
    // User Service
    public static final String USER = "admin";
    public static final String ROLE = "ROLE_";
    
    public static final String URL_LICENSE = "http://localhost:9989/getLicenceValidator";
    
    public static final String LICENSE_STATUS = "License ok";
    public static final String DATE_FORMATE = "yyyy-MM-dd";
    public static final String FORWARDSLASH = "/";
    public static final String DASH_SYMBOL = "_";
    public static final String TRUE_VALUE = "true";
    public static final String SEARCH = "search";
    public static final String FOLDER_TYPE = "FolderType";
    public static final String BASE_DATA_VARIABLE_TYPE = "BaseDataVariableType";
    public static final String STATUS_UNDEFINED = "undefined";
    public static final String STATUS_OFF = "off";
    public static final String STATUS_ON = "on";
    public static final String PICTURE = "picture";
    public static final CharSequence UNDERSCORE = "_";
    public static final CharSequence DASH = "-";
    public static final int UNDERSCORE_CHAR = '_';
    public static final int DASH_CHAR = '-';
    public static final String JAVA_VERSION = "java.version";
    public static final String JAVA_VERSION_1_6 = "1.6";
    public static final String HOSTNAME_APP_PROPERTY = "@hostname";
    public static final String SERVER_NAME_OPCUA = "OPCUA/";
    public static final String PRIVATE_CHILD_FILE_PROPERTY = "private";
    public static final String SAMPLE_ORG = "Sample Organisation";
    public static final String PATH_FROM_SOURCE_ROOT_SAMPLE_CONSOLE_SERVER = "/com/prosysopc/ua/samples/server/SampleConsoleServer.class";
    public static final String DEV_SOFTWARE_VERSION = "dev";
    public static final String CLOSED_BY_USER = "Closed by user";
    public static final String DISCOVERY_SERVER_URL = "opc.tcp://localhost:4840";
    public static final String APP_NAME_SAMPLE_CONSOLE_SERVER = "SiefluxConsoleServer";
    public static final String INTEGER_FILED = "IntegerField";
    public static final String STRUCTURE_FIELD = "StructureField";
    public static final String MY_NESTED_STRUCTURE = "MyNestedStructure";
    public static final String FIELD_2_VALUE = "Field2Value";
    public static final String MY_METHOD = "MyMethod";
    public static final String OPERATION = "Operation";
    public static final String PARAMETER = "Parameter";
    public static final String OPERATION_TO_PERFORM_PARAMETER = "The operation to perform on parameter: valid functions are sin, cos, tan, pow";
    public static final String PARAMETER_OPERATION = "The parameter for operation";
    public static final String RESULT_OPERATION = "The result of 'operation(parameter)'";
    public static final String ENUM_STRINGS = "EnumStrings";
    public static final String MY_ENUMTYPE_ENUM_STRINGS = "MyEnumType_EnumStrings";
    public static final String MY_ENUM_OBJECT = "MyEnumObject";
    public static final String ZERO = "Zero";
    public static final String ONE = "One";
    public static final String TWO = "Two";
    public static final String THREE = "Three";
    public static final String MY_EVENT_TYPE = "MyEventType";
    public static final String MY_SWITCH = "MySwitch";
    public static final String USER_IDENTITY_OPCUA = "opcua";
    public static final String USER_IDENTITY_OPCUA2 = "opcua2";
    public static final String USER_IDENTITY_OPCUA3 = "opcua3";
    public static final String URL_END_WITH_CSS = ".css";
    public static final String URL_END_WITH_HTML = ".html";
    public static final String ACCESS_DENIED_RESPONSE_MESSAGE = "Access Denied";
    public static final String APPLICATION_JSON_CONTENT_TYPE = "application/json;charset=UTF-8";
    public static final String UNAUTHORISED = "Unauthorised";
    public static final String INVALID_TOKEN_ERROR_CODE = "invalid_token";
    public static final int DASHBOARD_MAX_SIGNAL = 10;
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    
    public static final String API_OPC_CONNECT_BASEURL = "${api.opc.baseUrl}";
    public static final String API_OPC_ENDPOINT_URI_PROTOCOL = "opc.tcp://";
    
    public static final String API_OPC_CONNECT_URL = "connection/connect";
    public static final String API_OPC_GET_TREE = "tree/get-tree";
    public static final String API_OPC_CREATE_TREE = "tree/create-tree";
    public static final String API_OPC_ADD_REMOVE_TAGS = "tags";
    public static final String API_OPC_GET_TAG_LIVEDATA = "data/get-formatted-data";
    
    public static final String NODE_SERVER_ERROR = "Node Server Error";
    public static final int HTTP_SUCCESS_CODE = 200;
    public static final int HTTP_SERVER_ERROR_CODE = 500;
    
    public static final String ALARM_DANGER = "DANGER";
    public static final String ALARM_WARNING = "WARNING";
    public static final String ALARM_NORMAL = "NORMAL";
    
    public static final String USERS_BY_LOGIN_CACHE = "usersByLogin";
    public static final String USERS_BY_EMAIL_CACHE = "usersByEmail";
    
    public static final String LOGIN_ACTIVE = "ACTIVE";
    public static final String LOGIN_INACTIVE = "INACTIVE";
    public static final String LOGIN_INACTIVE_BY_SYS = "INACTIVE_BY_SYS";
    
    public static final String EVAC_RUNNING = "RUNNING";
    public static final String EVAC_COMPLETED = "COMPLETED";
    
    public static final String XLXS_FILE_LOCATION = "uploads/Asset_List_10Nos.xlsx";
    
}
