package com.tworun.openremoteclientservice.constants;

/**
 * Authentication-related constant keys and values for OpenRemote Auth API requests.
 * <p>
 * This class provides constant values for building authentication requests
 * (such as client_id, client_secret, grant_type) using the OAuth2 client credentials flow.
 * </p>
 *
 * <b>Note:</b> This is a utility class and should not be instantiated.
 */
public final class AuthConstants {
    /**
     * Private constructor to prevent instantiation.
     */
    private AuthConstants() {}

    public static final String CLIENT_ID_KEY = "client_id";
    public static final String CLIENT_SECRET_KEY = "client_secret";

    public static final String GRANT_TYPE_CLIENT_CREDENTIALS = "client_credentials";
    public static final String GRANT_TYPE_KEY = "grant_type";
}
