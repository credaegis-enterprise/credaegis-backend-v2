package com.credaegis.backend.constant;

public class Constants {

    public static final String ROUTEV1 = "/api/v1/organization";
    public static final String ADMIN = "ADMIN";
    public static final String MEMBER = "MEMBER";
    public static final String CLUSTER_ADMIN = "CLUSTER_ADMIN";
    public static final String LOCKED_CLUSTER_ADMIN = "LOCKED_CLUSTER_ADMIN";
    public static final String APP_NAME = "credaegis";
    public static final String ORGANIZATION_ACCOUNT_TYPE = "organization";
    public static final String MEMBER_ACCOUNT_TYPE = "member";
    public static final String DELETED = "DELETED";

    public static final String DIRECT_EXCHANGE = "DIRECT_EXCHANGE";

    public static final String APPROVAL_REQUEST_QUEUE = "APPROVAL_REQUEST_QUEUE";
    public static final String APPROVAL_REQUEST_QUEUE_KEY = "approval_request";

    public static final String NOTIFICATION_QUEUE = "NOTIFICATION_QUEUE";
    public static final String NOTIFICATION_QUEUE_KEY = "notification";

    public static final String APPROVAL_RESPONSE_QUEUE = "APPROVAL_RESPONSE_QUEUE";
    public static final String APPROVAL_RESPONSE_QUEUE_KEY = "approval_response";

    private static final String CERTIFICATE_REVOKE_REQUEST_QUEUE = "REVOKE_REQUEST_QUEUE";
    private static final String CERTIFICATE_REVOKE_QUEUE_KEY = "revoke_request";

    private static final String CERTIFICATE_REVOKE_RESPONSE_QUEUE = "REVOKE_RESPONSE_QUEUE";
    private static final String CERTIFICATE_RESPONSE_QUEUE_KEY = "revoke_response";



}
