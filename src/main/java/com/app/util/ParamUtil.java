package com.app.util;

import io.vertx.core.json.JsonObject;

import java.text.DateFormat;
import java.util.*;


public class ParamUtil {

    public static boolean getBoolean(Map paramMap, String paramName) {
        return getBoolean(paramMap, paramName, false);
    }

    public static boolean getBoolean(Map paramMap, String paramName, boolean defaultValue) {
        return paramMap.get(paramName) != null ? GetterUtil.getBoolean(paramMap.get(paramName).toString(), defaultValue) : defaultValue;
    }

    public static int getInt(Map paramMap, String paramName) {
        return getInt(paramMap, paramName, 0);
    }

    public static int getInt(Map paramMap, String paramName, int defaultValue) {
        return paramMap.get(paramName) != null ? GetterUtil.getInteger(paramMap.get(paramName), defaultValue) : defaultValue;
    }

    public static long getLong(Map paramMap, String paramName) {
        return getLong(paramMap, paramName, 0L);
    }

    public static long getLong(Map paramMap, String paramName, long defaultValue) {
        return paramMap.get(paramName) != null ? GetterUtil.getLong(paramMap.get(paramName).toString(), defaultValue) : defaultValue;
    }

    public static String getString(Map paramMap, String paramName) {
        return getString(paramMap, paramName, StringPool.BLANK);
    }

    public static String getString(Map paramMap, String paramName, String defaultValue) {

        if (paramMap == null || paramMap.isEmpty() || paramName.isEmpty()) {
            return defaultValue;
        }

        if (paramMap.get(paramName) == null) {
            return defaultValue;
        }

        if (paramMap.get(paramName) instanceof List) {
            return ((List) paramMap.get(paramName)).get(0).toString().trim();
        }

        if (paramMap.get(paramName) instanceof java.util.Date) {

            DateFormat dateFormat = AppConstants.DEFAULT_DATE_TIME_FORMAT;

            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+7"));

            return dateFormat.format(paramMap.get(paramName));
        }
        
        if (paramMap.get(paramName) instanceof java.sql.Timestamp) {

            DateFormat dateFormat = AppConstants.DEFAULT_DATE_TIME_FORMAT;

            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+7"));

            return dateFormat.format(paramMap.get(paramName));
        }

        return paramMap.get(paramName).toString().trim();
    }

    public static Map getMapData(Map paramMap, String paramName) {
        return getMapData(paramMap, paramName, new HashMap());
    }

    public static Map getMapData(Map paramMap, String paramName, Map defaultValue) {

        Object paramValue = paramMap.get(paramName);

        if (paramValue != null) {

            if (paramValue instanceof String) {

                JsonObject jsonObject = new JsonObject(paramValue.toString());

                return jsonObject.getMap();

            } else if (paramValue instanceof Map) {

                return (Map) paramValue;
            } else {
                return defaultValue;
            }

        } else {
            return defaultValue;
        }
    }


    public static List getListData(Map paramMap, String paramName) {
        return getListData(paramMap, paramName, new ArrayList<>());
    }

    public static List getListData(Map paramMap, String paramName, List defaultValue) {

        Object paramValue = paramMap.get(paramName);

        if (paramValue != null) {

            if (paramValue instanceof List) {

                return (List) paramValue;

            } else {
                return defaultValue;
            }

        } else {
            return defaultValue;
        }
    }
}
