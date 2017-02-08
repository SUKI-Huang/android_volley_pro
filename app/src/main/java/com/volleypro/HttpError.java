package com.volleypro;

import android.content.Context;

/**
 * Created by tony1 on 1/18/2017.
 */

public class HttpError {

    public static class Code {
        public static final int NETWORK_UNAVAILABLE = 1;
        public static final int UNKNOW_ERROR = 2;
        public static final int ACCEPTED = 202;
        public static final int BAD_GATEWAY = 502;
        public static final int BAD_REQUEST = 400;
        public static final int CONFLICT = 409;
        public static final int CONTINUE = 100;
        public static final int CREATED = 201;
        public static final int EXPECTATION_FAILED = 417;
        public static final int FAILED_DEPENDENCY = 424;
        public static final int FORBIDDEN = 403;
        public static final int GATEWAY_TIMEOUT = 504;
        public static final int GONE = 410;
        public static final int HTTP_VERSION_NOT_SUPPORTED = 505;
        public static final int INSUFFICIENT_SPACE_ON_RESOURCE = 419;
        public static final int INSUFFICIENT_STORAGE = 507;
        public static final int INTERNAL_SERVER_ERROR = 500;
        public static final int LENGTH_REQUIRED = 411;
        public static final int LOCKED = 423;
        public static final int METHOD_FAILURE = 420;
        public static final int METHOD_NOT_ALLOWED = 405;
        public static final int MOVED_PERMANENTLY = 301;
        public static final int MOVED_TEMPORARILY = 302;
        public static final int MULTI_STATUS = 207;
        public static final int MULTIPLE_CHOICES = 300;
        public static final int NETWORK_AUTHENTICATION_REQUIRED = 511;
        public static final int NO_CONTENT = 204;
        public static final int NON_AUTHORITATIVE_INFORMATION = 203;
        public static final int NOT_ACCEPTABLE = 406;
        public static final int NOT_FOUND = 404;
        public static final int NOT_IMPLEMENTED = 501;
        public static final int NOT_MODIFIED = 304;
        public static final int OK = 200;
        public static final int PARTIAL_CONTENT = 206;
        public static final int PAYMENT_REQUIRED = 402;
        public static final int PRECONDITION_FAILED = 412;
        public static final int PRECONDITION_REQUIRED = 428;
        public static final int PROCESSING = 102;
        public static final int PROXY_AUTHENTICATION_REQUIRED = 407;
        public static final int REQUEST_HEADER_FIELDS_TOO_LARGE = 431;
        public static final int REQUEST_TIMEOUT = 408;
        public static final int REQUEST_TOO_LONG = 413;
        public static final int REQUEST_URI_TOO_LONG = 414;
        public static final int REQUESTED_RANGE_NOT_SATISFIABLE = 416;
        public static final int RESET_CONTENT = 205;
        public static final int SEE_OTHER = 303;
        public static final int SERVICE_UNAVAILABLE = 503;
        public static final int SWITCHING_PROTOCOLS = 101;
        public static final int TEMPORARY_REDIRECT = 307;
        public static final int TOO_MANY_REQUESTS = 429;
        public static final int UNAUTHORIZED = 401;
        public static final int UNPROCESSABLE_ENTITY = 422;
        public static final int UNSUPPORTED_MEDIA_TYPE = 415;
        public static final int USE_PROXY = 305;
    }

    public static class Message {
        private static Context context;

        public static void initialize(Context _context) {
            context = _context;
        }

        private static String NETWORK_UNAVAILABLE = "Network unavailable";
        private static String UNKNOW_ERROR = "Unknow error";
        private static String ACCEPTED = "Accepted";
        private static String BAD_GATEWAY = "Bad Gateway";
        private static String BAD_REQUEST = "Bad Request";
        private static String CONFLICT = "Conflict";
        private static String CONTINUE = "Continue";
        private static String CREATED = "Created";
        private static String EXPECTATION_FAILED = "Expectation Failed";
        private static String FAILED_DEPENDENCY = "Failed Dependency";
        private static String FORBIDDEN = "Forbidden";
        private static String GATEWAY_TIMEOUT = "Gateway Timeout";
        private static String GONE = "Gone";
        private static String HTTP_VERSION_NOT_SUPPORTED = "HTTP Version Not Supported";
        private static String INSUFFICIENT_SPACE_ON_RESOURCE = "Insufficient Space on Resource";
        private static String INSUFFICIENT_STORAGE = "Insufficient Storage";
        private static String INTERNAL_SERVER_ERROR = "Server Error";
        private static String LENGTH_REQUIRED = "Length Required";
        private static String LOCKED = "Locked";
        private static String METHOD_FAILURE = "Method Failure";
        private static String METHOD_NOT_ALLOWED = "Method Not Allowed";
        private static String MOVED_PERMANENTLY = "Moved Permanently";
        private static String MOVED_TEMPORARILY = "Moved Temporarily";
        private static String MULTI_STATUS = "Multi-Status";
        private static String MULTIPLE_CHOICES = "Multiple Choices";
        private static String NETWORK_AUTHENTICATION_REQUIRED = "Network Authentication Required";
        private static String NO_CONTENT = "No Content";
        private static String NON_AUTHORITATIVE_INFORMATION = "Non Authoritative Information";
        private static String NOT_ACCEPTABLE = "Not Acceptable";
        private static String NOT_FOUND = "Not Found";
        private static String NOT_IMPLEMENTED = "Not Implemented";
        private static String NOT_MODIFIED = "Not Modified";
        private static String OK = "OK";
        private static String PARTIAL_CONTENT = "Partial Content";
        private static String PAYMENT_REQUIRED = "Payment Required";
        private static String PRECONDITION_FAILED = "Precondition Failed";
        private static String PRECONDITION_REQUIRED = "Precondition Required";
        private static String PROCESSING = "Processing";
        private static String PROXY_AUTHENTICATION_REQUIRED = "Proxy Authentication Required";
        private static String REQUEST_HEADER_FIELDS_TOO_LARGE = "Request Header Fields Too Large";
        private static String REQUEST_TIMEOUT = "Request Timeout";
        private static String REQUEST_TOO_LONG = "Request Entity Too Large";
        private static String REQUEST_URI_TOO_LONG = "Request-URI Too Long";
        private static String REQUESTED_RANGE_NOT_SATISFIABLE = "Requested Range Not Satisfiable";
        private static String RESET_CONTENT = "Reset Content";
        private static String SEE_OTHER = "See Other";
        private static String SERVICE_UNAVAILABLE = "Service Unavailable";
        private static String SWITCHING_PROTOCOLS = "Switching Protocols";
        private static String TEMPORARY_REDIRECT = "Temporary Redirect";
        private static String TOO_MANY_REQUESTS = "Too Many Requests";
        private static String UNAUTHORIZED = "Unauthorized";
        private static String UNPROCESSABLE_ENTITY = "Unprocessable Entity";
        private static String UNSUPPORTED_MEDIA_TYPE = "Unsupported Media Type";
        private static String USE_PROXY = "Use Proxy";

        public static void setMessage(int errorCode, int resourceId) {
            setMessage(errorCode, context.getString(resourceId));
        }

        public static void setMessage(int errorCode, String msg) {
            switch (errorCode) {
                case HttpError.Code.NETWORK_UNAVAILABLE:
                    NETWORK_UNAVAILABLE = msg;
                    break;
                case HttpError.Code.UNKNOW_ERROR:
                    UNKNOW_ERROR = msg;
                    break;
                case HttpError.Code.ACCEPTED:
                    ACCEPTED = msg;
                    break;
                case HttpError.Code.BAD_GATEWAY:
                    BAD_GATEWAY = msg;
                    break;
                case HttpError.Code.BAD_REQUEST:
                    BAD_REQUEST = msg;
                    break;
                case HttpError.Code.CONFLICT:
                    CONFLICT = msg;
                    break;
                case HttpError.Code.CONTINUE:
                    CONTINUE = msg;
                    break;
                case HttpError.Code.CREATED:
                    CREATED = msg;
                    break;
                case HttpError.Code.EXPECTATION_FAILED:
                    EXPECTATION_FAILED = msg;
                    break;
                case HttpError.Code.FAILED_DEPENDENCY:
                    FAILED_DEPENDENCY = msg;
                    break;
                case HttpError.Code.FORBIDDEN:
                    FORBIDDEN = msg;
                    break;
                case HttpError.Code.GATEWAY_TIMEOUT:
                    GATEWAY_TIMEOUT = msg;
                    break;
                case HttpError.Code.GONE:
                    GONE = msg;
                    break;
                case HttpError.Code.HTTP_VERSION_NOT_SUPPORTED:
                    HTTP_VERSION_NOT_SUPPORTED = msg;
                    break;
                case HttpError.Code.INSUFFICIENT_SPACE_ON_RESOURCE:
                    INSUFFICIENT_SPACE_ON_RESOURCE = msg;
                    break;
                case HttpError.Code.INSUFFICIENT_STORAGE:
                    INSUFFICIENT_STORAGE = msg;
                    break;
                case HttpError.Code.INTERNAL_SERVER_ERROR:
                    INTERNAL_SERVER_ERROR = msg;
                    break;
                case HttpError.Code.LENGTH_REQUIRED:
                    LENGTH_REQUIRED = msg;
                    break;
                case HttpError.Code.LOCKED:
                    LOCKED = msg;
                    break;
                case HttpError.Code.METHOD_FAILURE:
                    METHOD_FAILURE = msg;
                    break;
                case HttpError.Code.METHOD_NOT_ALLOWED:
                    METHOD_NOT_ALLOWED = msg;
                    break;
                case HttpError.Code.MOVED_PERMANENTLY:
                    MOVED_PERMANENTLY = msg;
                    break;
                case HttpError.Code.MOVED_TEMPORARILY:
                    MOVED_TEMPORARILY = msg;
                    break;
                case HttpError.Code.MULTI_STATUS:
                    MULTI_STATUS = msg;
                    break;
                case HttpError.Code.MULTIPLE_CHOICES:
                    MULTIPLE_CHOICES = msg;
                    break;
                case HttpError.Code.NETWORK_AUTHENTICATION_REQUIRED:
                    NETWORK_AUTHENTICATION_REQUIRED = msg;
                    break;
                case HttpError.Code.NO_CONTENT:
                    NO_CONTENT = msg;
                    break;
                case HttpError.Code.NON_AUTHORITATIVE_INFORMATION:
                    NON_AUTHORITATIVE_INFORMATION = msg;
                    break;
                case HttpError.Code.NOT_ACCEPTABLE:
                    NOT_ACCEPTABLE = msg;
                    break;
                case HttpError.Code.NOT_FOUND:
                    NOT_FOUND = msg;
                    break;
                case HttpError.Code.NOT_IMPLEMENTED:
                    NOT_IMPLEMENTED = msg;
                    break;
                case HttpError.Code.NOT_MODIFIED:
                    NOT_MODIFIED = msg;
                    break;
                case HttpError.Code.OK:
                    OK = msg;
                    break;
                case HttpError.Code.PARTIAL_CONTENT:
                    PARTIAL_CONTENT = msg;
                    break;
                case HttpError.Code.PAYMENT_REQUIRED:
                    PAYMENT_REQUIRED = msg;
                    break;
                case HttpError.Code.PRECONDITION_FAILED:
                    PRECONDITION_FAILED = msg;
                    break;
                case HttpError.Code.PRECONDITION_REQUIRED:
                    PRECONDITION_REQUIRED = msg;
                    break;
                case HttpError.Code.PROCESSING:
                    PROCESSING = msg;
                    break;
                case HttpError.Code.PROXY_AUTHENTICATION_REQUIRED:
                    PROXY_AUTHENTICATION_REQUIRED = msg;
                    break;
                case HttpError.Code.REQUEST_HEADER_FIELDS_TOO_LARGE:
                    REQUEST_HEADER_FIELDS_TOO_LARGE = msg;
                    break;
                case HttpError.Code.REQUEST_TIMEOUT:
                    REQUEST_TIMEOUT = msg;
                    break;
                case HttpError.Code.REQUEST_TOO_LONG:
                    REQUEST_TOO_LONG = msg;
                    break;
                case HttpError.Code.REQUEST_URI_TOO_LONG:
                    REQUEST_URI_TOO_LONG = msg;
                    break;
                case HttpError.Code.REQUESTED_RANGE_NOT_SATISFIABLE:
                    REQUESTED_RANGE_NOT_SATISFIABLE = msg;
                    break;
                case HttpError.Code.RESET_CONTENT:
                    RESET_CONTENT = msg;
                    break;
                case HttpError.Code.SEE_OTHER:
                    SEE_OTHER = msg;
                    break;
                case HttpError.Code.SERVICE_UNAVAILABLE:
                    SERVICE_UNAVAILABLE = msg;
                    break;
                case HttpError.Code.SWITCHING_PROTOCOLS:
                    SWITCHING_PROTOCOLS = msg;
                    break;
                case HttpError.Code.TEMPORARY_REDIRECT:
                    TEMPORARY_REDIRECT = msg;
                    break;
                case HttpError.Code.TOO_MANY_REQUESTS:
                    TOO_MANY_REQUESTS = msg;
                    break;
                case HttpError.Code.UNAUTHORIZED:
                    UNAUTHORIZED = msg;
                    break;
                case HttpError.Code.UNPROCESSABLE_ENTITY:
                    UNPROCESSABLE_ENTITY = msg;
                    break;
                case HttpError.Code.UNSUPPORTED_MEDIA_TYPE:
                    UNSUPPORTED_MEDIA_TYPE = msg;
                    break;
                case HttpError.Code.USE_PROXY:
                    USE_PROXY = msg;
                    break;

            }
        }


        public static String getMessage(int errorCode) {
            switch (errorCode) {
                case HttpError.Code.NETWORK_UNAVAILABLE:
                    return NETWORK_UNAVAILABLE;
                case HttpError.Code.UNKNOW_ERROR:
                    return UNKNOW_ERROR;
                case HttpError.Code.ACCEPTED:
                    return ACCEPTED;
                case HttpError.Code.BAD_GATEWAY:
                    return BAD_GATEWAY;
                case HttpError.Code.BAD_REQUEST:
                    return BAD_REQUEST;
                case HttpError.Code.CONFLICT:
                    return CONFLICT;
                case HttpError.Code.CONTINUE:
                    return CONTINUE;
                case HttpError.Code.CREATED:
                    return CREATED;
                case HttpError.Code.EXPECTATION_FAILED:
                    return EXPECTATION_FAILED;
                case HttpError.Code.FAILED_DEPENDENCY:
                    return FAILED_DEPENDENCY;
                case HttpError.Code.FORBIDDEN:
                    return FORBIDDEN;
                case HttpError.Code.GATEWAY_TIMEOUT:
                    return GATEWAY_TIMEOUT;
                case HttpError.Code.GONE:
                    return GONE;
                case HttpError.Code.HTTP_VERSION_NOT_SUPPORTED:
                    return HTTP_VERSION_NOT_SUPPORTED;
                case HttpError.Code.INSUFFICIENT_SPACE_ON_RESOURCE:
                    return INSUFFICIENT_SPACE_ON_RESOURCE;
                case HttpError.Code.INSUFFICIENT_STORAGE:
                    return INSUFFICIENT_STORAGE;
                case HttpError.Code.INTERNAL_SERVER_ERROR:
                    return INTERNAL_SERVER_ERROR;
                case HttpError.Code.LENGTH_REQUIRED:
                    return LENGTH_REQUIRED;
                case HttpError.Code.LOCKED:
                    return LOCKED;
                case HttpError.Code.METHOD_FAILURE:
                    return METHOD_FAILURE;
                case HttpError.Code.METHOD_NOT_ALLOWED:
                    return METHOD_NOT_ALLOWED;
                case HttpError.Code.MOVED_PERMANENTLY:
                    return MOVED_PERMANENTLY;
                case HttpError.Code.MOVED_TEMPORARILY:
                    return MOVED_TEMPORARILY;
                case HttpError.Code.MULTI_STATUS:
                    return MULTI_STATUS;
                case HttpError.Code.MULTIPLE_CHOICES:
                    return MULTIPLE_CHOICES;
                case HttpError.Code.NETWORK_AUTHENTICATION_REQUIRED:
                    return NETWORK_AUTHENTICATION_REQUIRED;
                case HttpError.Code.NO_CONTENT:
                    return NO_CONTENT;
                case HttpError.Code.NON_AUTHORITATIVE_INFORMATION:
                    return NON_AUTHORITATIVE_INFORMATION;
                case HttpError.Code.NOT_ACCEPTABLE:
                    return NOT_ACCEPTABLE;
                case HttpError.Code.NOT_FOUND:
                    return NOT_FOUND;
                case HttpError.Code.NOT_IMPLEMENTED:
                    return NOT_IMPLEMENTED;
                case HttpError.Code.NOT_MODIFIED:
                    return NOT_MODIFIED;
                case HttpError.Code.OK:
                    return OK;
                case HttpError.Code.PARTIAL_CONTENT:
                    return PARTIAL_CONTENT;
                case HttpError.Code.PAYMENT_REQUIRED:
                    return PAYMENT_REQUIRED;
                case HttpError.Code.PRECONDITION_FAILED:
                    return PRECONDITION_FAILED;
                case HttpError.Code.PRECONDITION_REQUIRED:
                    return PRECONDITION_REQUIRED;
                case HttpError.Code.PROCESSING:
                    return PROCESSING;
                case HttpError.Code.PROXY_AUTHENTICATION_REQUIRED:
                    return PROXY_AUTHENTICATION_REQUIRED;
                case HttpError.Code.REQUEST_HEADER_FIELDS_TOO_LARGE:
                    return REQUEST_HEADER_FIELDS_TOO_LARGE;
                case HttpError.Code.REQUEST_TIMEOUT:
                    return REQUEST_TIMEOUT;
                case HttpError.Code.REQUEST_TOO_LONG:
                    return REQUEST_TOO_LONG;
                case HttpError.Code.REQUEST_URI_TOO_LONG:
                    return REQUEST_URI_TOO_LONG;
                case HttpError.Code.REQUESTED_RANGE_NOT_SATISFIABLE:
                    return REQUESTED_RANGE_NOT_SATISFIABLE;
                case HttpError.Code.RESET_CONTENT:
                    return RESET_CONTENT;
                case HttpError.Code.SEE_OTHER:
                    return SEE_OTHER;
                case HttpError.Code.SERVICE_UNAVAILABLE:
                    return SERVICE_UNAVAILABLE;
                case HttpError.Code.SWITCHING_PROTOCOLS:
                    return SWITCHING_PROTOCOLS;
                case HttpError.Code.TEMPORARY_REDIRECT:
                    return TEMPORARY_REDIRECT;
                case HttpError.Code.TOO_MANY_REQUESTS:
                    return TOO_MANY_REQUESTS;
                case HttpError.Code.UNAUTHORIZED:
                    return UNAUTHORIZED;
                case HttpError.Code.UNPROCESSABLE_ENTITY:
                    return UNPROCESSABLE_ENTITY;
                case HttpError.Code.UNSUPPORTED_MEDIA_TYPE:
                    return UNSUPPORTED_MEDIA_TYPE;
                case HttpError.Code.USE_PROXY:
                    return USE_PROXY;
                default:
                    return null;
            }
        }
    }

}
