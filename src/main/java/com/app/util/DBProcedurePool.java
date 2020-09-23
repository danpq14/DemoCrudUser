package com.app.util;


public class DBProcedurePool {

    public static final String CLIENT_GET = "{call PKG_CLIENT.client_get(?,?,?,?)}";

    public static final String USER_INSERT = "{call PKG_USER.user_insert(?,?,?,?,?,?)}";
    public static final String USER_DELETE = "{call PKG_USER.user_delete(?,?,?)}";
    public static final String USER_SEARCH = "{call PKG_USER.user_search(?,?,?,?,?,?)}";
    public static final String USER_UPDATE_STATE = "{call PKG_USER.user_update_state(?,?,?,?,?)}";
    public static final String USER_GET = "{call PKG_USER.user_get(?,?,?,?)}";
    public static final String USER_SEARCH_EMAIL = "{call PKG_USER.user_search_email(?,?,?,?)}";

    public static final String AUTHORIZATION_INSERT = "{call PKG_AUTHORIZATION.authorization_insert(?,?,?,?,?,?,?,?)}";
    public static final String AUTHORIZATION_SEARCH = "{call PKG_AUTHORIZATION.authorization_search(?,?,?,?,?,?)}";
    public static final String AUTHORIZATION_GET = "{call PKG_AUTHORIZATION.authorization_get(?,?,?,?)}";
    public static final String AUTHORIZATION_UPDATE_STATE = "{call PKG_AUTHORIZATION.authorization_update_state(?,?,?,?,?)}";

    public static final String TOKEN_GET = "{call PKG_TOKEN.token_get(?,?,?,?)}";
    public static final String TOKEN_INSERT = "{call PKG_TOKEN.token_insert(?,?,?,?,?,?,?)}";
    public static final String TOKEN_UPDATE_STATE = "{call PKG_TOKEN.token_update_state(?,?,?,?,?)}";
    public static final String TOKEN_DELETE = "{call PKG_TOKEN.token_delete(?,?,?)}";
    public static final String TOKEN_SEARCH = "{call PKG_TOKEN.token_search(?,?,?,?)}";
    
    public static final String PAYMENT_SEARCH = "{call PKG_PAYMENT.payment_search(?,?,?,?)}";
    
    public static final String COUNTRY_GET = "{call PGK_COUNTRY.country_get(?,?,?,?)}";
    
    
    //Test demo database
    public static final String GET_USER_BY_ID = "{call PKG_D_USER.user_get(?,?,?,?)}";
    public static final String GET_USER_BY_PHONE = "{call PKG_D_USER.user_get_by_phone(?,?,?,?)}";
    public static final String INSERT_USER = "{call PKG_D_USER.insert_user(?,?,?,?,?,?,?,?)}";
    public static final String UPDATE_USER = "{call PKG_D_USER.update_user(?,?,?,?,?,?,?,?,?)}";
    public static final String DELETE_USER = "{call PKG_D_USER.delete_user(?,?,?)}";

}
