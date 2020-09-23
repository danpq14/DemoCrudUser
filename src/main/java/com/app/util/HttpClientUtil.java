package com.app.util;

import com.app.asp.server.vertical.ASPVertical;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpMethod;
import io.vertx.rxjava.core.http.HttpClient;
import io.vertx.rxjava.core.http.HttpClientRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Level;


public class HttpClientUtil implements LoggerInterface {

    public static HttpClientRequest createHttpRequest(HttpServiceConfig httpServiceConfig, String requestURI, HttpMethod requestMethod, Map queryParametersMap, String requestBody) {
        Date requestDate = new Date();
        DateFormat yyyyMMddTHHmmssZ = AppConstants.DEFAULT_DATE_TIME_FORMAT;
        yyyyMMddTHHmmssZ.setTimeZone(TimeZone.getTimeZone("UTC"));
        HttpClient httpClient = ASPVertical.httpClient;
        try {
            URI uri = new URI(httpServiceConfig.getServiceURL());
            if ("https".equals(uri.getScheme())) {
                httpClient = ASPVertical.httpsClient;
            }
        } catch (URISyntaxException e) {
            logger.log(Level.SEVERE, "", e);
        }
        String requestURL = httpServiceConfig.getServiceURL() + requestURI;
        long requestTimeout = httpServiceConfig.getServiceTimeOut() > 0 ? httpServiceConfig.getServiceTimeOut() : 60000;
        HttpClientRequest httpClientRequest = httpClient.requestAbs(requestMethod, requestURL);
        httpClientRequest.setTimeout(requestTimeout);
        String signatureRequestURI = httpClientRequest.uri();
        if (httpClientRequest.uri().contains(StringPool.QUESTION)) {
            int queryIndex = httpClientRequest.uri().indexOf(StringPool.QUESTION);
            signatureRequestURI = httpClientRequest.uri().substring(0, queryIndex);
        }
        Map<String, String> signedHeaderMap = new LinkedHashMap<>();
        signedHeaderMap.put(AppParams.X_DATE, yyyyMMddTHHmmssZ.format(requestDate));
        signedHeaderMap.put(AppParams.X_EXPIRES, String.valueOf(requestTimeout));
//        Authorization serviceAuthorization = new Authorization(httpServiceConfig.getServiceAuthAlgorithm(), httpServiceConfig.getServiceAuthId(), httpServiceConfig.getServiceAuthKey(),
//                httpServiceConfig.getServiceRegion(), httpServiceConfig.getServiceName(), httpServiceConfig.getServiceAuthType(),
//                httpClientRequest.method().name(), signatureRequestURI, queryParametersMap, signedHeaderMap,
//                requestBody.getBytes(), requestDate, httpServiceConfig.getServiceTimeOut());
//        logger.log(Level.INFO, "DEBUG INFO : " + serviceAuthorization.getDebugInfo().toString());
        httpClientRequest.putHeader(HttpHeaders.ACCEPT.toString(), AppConstants.CONTENT_TYPE_APPLICATION_JSON);
        httpClientRequest.putHeader(HttpHeaders.CONTENT_TYPE.toString(), AppConstants.CONTENT_TYPE_APPLICATION_JSON);
        httpClientRequest.putHeader(HttpHeaders.CONTENT_LENGTH.toString(), AppUtil.getContentLength(requestBody));
        httpClientRequest.putHeader(AppParams.X_DATE, yyyyMMddTHHmmssZ.format(requestDate));
        httpClientRequest.putHeader(AppParams.X_EXPIRES, String.valueOf(requestTimeout));
//        httpClientRequest.putHeader(AppParams.X_AUTHORIZATION, serviceAuthorization.toString());
        String serviceName = "[" + httpServiceConfig.getServiceName().toUpperCase() + " REQUEST] ";
        logger.log(Level.INFO, "{0}{1}" + StringPool.DOUBLE_SPACE + "{2}", new Object[]{serviceName, httpClientRequest.method().name(), httpClientRequest.uri()});
        if (!requestBody.isEmpty() && !requestBody.contains(AppParams.DATA)) {
            logger.log(Level.INFO, "{0}{1}", new Object[]{serviceName, requestBody});
        }
        return httpClientRequest;
    }
}
