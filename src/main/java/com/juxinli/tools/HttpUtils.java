package com.juxinli.tools;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.http.*;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeaderValueParser;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.message.ParserCursor;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * http请求工具类
 *
 * @author L
 */
public class HttpUtils {
    private static final char QP_SEP_A = '&';
    private static final char QP_SEP_S = ';';
    private static final char[] QP_SEPS = new char[]{QP_SEP_A, QP_SEP_S};

    static CookieStore cookieStore = null;

    private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);
    private PoolingHttpClientConnectionManager connMgr;
    private RequestConfig requestConfig;

    private HttpUtils() {
        RegistryBuilder<ConnectionSocketFactory> registryBuilder =
                RegistryBuilder.create();
        registryBuilder.register("http", new PlainConnectionSocketFactory());
        registryBuilder.register("https", createSSLConnSocketFactory());
        connMgr = new PoolingHttpClientConnectionManager(registryBuilder.build());
        connMgr.setMaxTotal(50);
        connMgr.setDefaultMaxPerRoute(connMgr.getMaxTotal());
        RequestConfig.Builder configBuilder = RequestConfig.custom();
        configBuilder.setConnectTimeout(8000);
        configBuilder.setSocketTimeout(8000);
        configBuilder.setConnectionRequestTimeout(8000);
        configBuilder.setStaleConnectionCheckEnabled(true);
        requestConfig = configBuilder.build();
    }

    /**
     * url参数encode
     *
     * @param url 带参数的url地址
     * @return encode后url地址
     */
    public static String URLEncode(String url) {
        if (StringUtils.isBlank(url))
            return null;
        int qy = url.indexOf("?");
        if (qy == -1)
            return url;
        String params = url.substring(qy + 1);
        final BasicHeaderValueParser parser = BasicHeaderValueParser.INSTANCE;
        final CharArrayBuffer buffer = new CharArrayBuffer(params.length());
        buffer.append(params);
        final ParserCursor cursor = new ParserCursor(0, buffer.length());
        final List<NameValuePair> list = new ArrayList<>();
        while (!cursor.atEnd()) {
            final NameValuePair nvp =
                    parser.parseNameValuePair(buffer, cursor, QP_SEPS);
            if (nvp.getName().length() > 0) {
                list.add(new BasicNameValuePair(nvp.getName(), nvp.getValue()));
            }
        }
        params = URLEncodedUtils.format(list, Charset.forName("utf-8"));
        return url.substring(0, qy + 1).concat(params);
    }

    /**
     * 发送http get请求，支持https
     *
     * @param url get url地址
     * @return html页面
     */
    private CloseableHttpResponse get(String url) {
        if (StringUtils.isBlank(url))
            return null;
        url = URLEncode(url);
        CloseableHttpClient httpClient = HttpClients.custom().
                setConnectionManager(connMgr).
                setDefaultRequestConfig(requestConfig).
                build();
        HttpGet httpGet = new HttpGet(url);
        try {
            httpGet.setConfig(requestConfig);
            return httpClient.execute(httpGet);
        } catch (Exception e) {
            logger.warn("get request throw exception, detail: {}",
                    e.toString());
        }
        return null;
    }

    private CloseableHttpResponse header_get(String url) {
        if (StringUtils.isBlank(url))
            return null;
        url = URLEncode(url);
        CloseableHttpClient httpClient = HttpClients.custom().
                setConnectionManager(connMgr).
                setDefaultRequestConfig(requestConfig).
                setDefaultCookieStore(cookieStore).
                build();
        HttpGet httpGet = new HttpGet(url);
        try {
            httpGet.setConfig(requestConfig);
            return httpClient.execute(httpGet);
        } catch (Exception e) {
            logger.warn("get request throw exception, detail: {}",
                    e.toString());
        }
        return null;
    }

    /**
     * cookie请求
     *
     * @param url 链接
     * @return 响应信息
     */
    public static String cookie_Get(String url) {
        CloseableHttpResponse response = httpUtils.header_get(url);
        if (response == null)
            return null;
        setCookieStore(response);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != HttpStatus.SC_OK) {
            logger.warn("response status: " + statusCode);
            return null;
        }
        HttpEntity entity = response.getEntity();
        if (entity == null) {
            logger.warn("response output is null!");
            return null;
        }
        try {
            return EntityUtils.toString(entity, "utf-8");
        } catch (IOException e) {
            logger.warn("get request throw exception, detail: {}",
                    e.toString());
        } finally {
            try {
                EntityUtils.consume(response.getEntity());
            } catch (IOException e) {
                logger.warn("post request throw exception, detail: {}",
                        e.toString());
            }
        }
        return null;
    }

    /**
     * 得到cookie
     *
     * @param httpResponse 请求响应
     */
    public static void setCookieStore(HttpResponse httpResponse) {

        cookieStore = new BasicCookieStore();
        Header firstHeader = httpResponse.getFirstHeader("Set-Cookie");
        if (firstHeader == null) return;
        // JSESSIONID
        String setCookie = firstHeader.getValue();
        String JSESSIONID = setCookie.substring("JSESSIONID=".length(),
                setCookie.indexOf(";"));
        // 新建一个Cookie
        BasicClientCookie cookie = new BasicClientCookie("JSESSIONID",
                JSESSIONID);
        cookie.setVersion(0);
        cookie.setDomain("127.0.0.1");
        cookie.setPath("/CwlProClient");
        cookieStore.addCookie(cookie);
    }

    /**
     * 发送http post请求，支持https
     *
     * @param url    post url地址
     * @param params post请求参数
     * @return html页面
     */
    private CloseableHttpResponse post(String url, HttpEntity params) {
        if (StringUtils.isBlank(url))
            return null;
        CloseableHttpClient httpClient = HttpClients.custom().
                setSSLSocketFactory(createSSLConnSocketFactory()).
                setRedirectStrategy(new DefaultRedirectStrategy() {
                    @Override
                    public boolean isRedirected(final HttpRequest request,
                                                final HttpResponse response,
                                                final HttpContext context)
                            throws ProtocolException {
                        int status = response.getStatusLine().getStatusCode();
                        return status == 301 ||
                                status == 302 ||
                                super.isRedirected(request, response, context);
                    }
                }).
                setConnectionManager(connMgr).
                setDefaultRequestConfig(requestConfig).
                build();
        HttpPost httpPost = new HttpPost(url);
        try {
            httpPost.setConfig(requestConfig);
            httpPost.setEntity(params);
            return httpClient.execute(httpPost);
        } catch (Exception e) {
            logger.warn("post request throw exception, detail: {}",
                    e.toString());
        }
        return null;
    }

    /**
     * SSL连接支持
     *
     * @return SSL对象
     */
    private SSLConnectionSocketFactory createSSLConnSocketFactory() {
        SSLConnectionSocketFactory sslsf = null;
        try {
            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustStrategy() {
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            sslsf = new SSLConnectionSocketFactory(sslContext);
        } catch (GeneralSecurityException e) {
            logger.error(e.getMessage(), e);
        }
        return sslsf;
    }

    private HttpEntity paramsStd(Map<String, Object> params) {
        if (params != null) {
            List<NameValuePair> pairs = new ArrayList<>(params.size());
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                NameValuePair pair =
                        new BasicNameValuePair(entry.getKey(),
                                entry.getValue().toString());
                pairs.add(pair);
            }
            return new UrlEncodedFormEntity(pairs, Charset.forName("utf-8"));
        }
        return null;
    }

    private static HttpUtils httpUtils = new HttpUtils();

    /**
     * get请求
     *
     * @param url url地址
     * @return html页面
     */
    public static String doGet(String url) {
        CloseableHttpResponse response = httpUtils.get(url);
        if (response == null)
            return null;
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != HttpStatus.SC_OK) {
            logger.warn("response status: " + statusCode);
            return null;
        }
        HttpEntity entity = response.getEntity();
        if (entity == null) {
            logger.warn("response output is null!");
            return null;
        }
        try {
            return EntityUtils.toString(entity, "utf-8");
        } catch (IOException e) {
            logger.warn("get request throw exception, detail: {}",
                    e.toString());
        } finally {
            try {
                EntityUtils.consume(response.getEntity());
            } catch (IOException e) {
                logger.warn("post request throw exception, detail: {}",
                        e.toString());
            }
        }
        return null;
    }

    /**
     * get请求，返回HttpResponse
     *
     * @param url url地址
     * @return HttpResponse对象
     */
    public static CloseableHttpResponse doGetResponse(String url) {
        return httpUtils.get(url);
    }

    /**
     * post请求
     *
     * @param url    url地址
     * @param params 参数
     * @return html页面
     */
    public static String doPost(String url, Map<String, Object> params) {
        CloseableHttpResponse response;
        response = httpUtils.post(url, httpUtils.paramsStd(params));
        if (response == null)
            return null;
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != HttpStatus.SC_OK) {
            logger.warn("response status: " + statusCode);
            return null;
        }
        HttpEntity entity;
        entity = response.getEntity();
        if (entity == null) {
            logger.warn("response output is null!");
            return null;
        }
        try {
            return EntityUtils.toString(entity, "utf-8");
        } catch (IOException e) {
            logger.warn("post request throw exception, detail: {}",
                    e.toString());
        } finally {
            try {
                EntityUtils.consume(response.getEntity());
            } catch (IOException e) {
                logger.warn("post request throw exception, detail: {}",
                        e.toString());
            }
        }
        return null;
    }

    /**
     * post请求，返回HttpResponse
     *
     * @param url    url地址
     * @param params Map对象参数
     * @return HttpResponse对象
     */
    public static CloseableHttpResponse doPostResponse(String url, Map<String, Object> params) {
        return httpUtils.post(url, httpUtils.paramsStd(params));
    }

    /**
     * post请求，返回HttpResponse
     *
     * @param url        url地址
     * @param httpEntity 参数
     * @return HttpResponse对象
     */
    public static CloseableHttpResponse doPostResponse(String url, HttpEntity httpEntity) {
        return httpUtils.post(url, httpEntity);
    }

    /**
     * 利用代理访问
     *
     * @param url 请求地址
     * @return 响应信息
     */
    public static String getWithProxy(String url, Map<String, Object> params, String requestType) {

        int index = 0;
        do {
            HttpHost proxy;
            if (requestType.equals("POST"))
                proxy = new HttpHost(proxyHost_POST, NumberUtils.toInt(proxyPort_POST, 80));
            else
                proxy = new HttpHost(proxyHost_GET, NumberUtils.toInt(proxyPort_GET, 80));

            CloseableHttpClient client = HttpClients.custom()
                    .setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.115 Safari/537.36")
                    .setProxy(proxy)
                    .build();

            CloseableHttpResponse httpResponse = null;
            try {
                if (requestType.equals("POST")) {
                    HttpPost httpPost = new HttpPost(url);
                    if (params != null)
                        httpPost.setEntity(httpUtils.paramsStd(params));
                    httpResponse = client.execute(httpPost);
                } else {
                    HttpGet httpGet = new HttpGet(url);
                    httpResponse = client.execute(httpGet);
                }

                if (httpResponse == null)
                    return null;
                HttpEntity entity = httpResponse.getEntity();
                int statusCode = httpResponse.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    return EntityUtils.toString(entity, "utf-8");
                }
            } catch (Exception e) {
                if (requestType.equals("POST"))
                    logger.error("failed to visit site [" + url + "] with proxy " + proxyHost_POST + ":" + proxyPort_POST, e);
                else
                    logger.error("failed to visit site [" + url + "] with proxy " + proxyHost_GET + ":" + proxyPort_GET, e);
            } finally {
                try {
                    if (httpResponse != null) {
                        //ensure the connection is released back to pool
                        EntityUtils.consume(httpResponse.getEntity());
                    }
                } catch (IOException e) {
                    logger.info("close response fail", e);
                }
            }
            //求情失败切换代理
            if (requestType.equals("POST")) {
                proxyHost_POST = StringUtils.substringBefore(proxys[index], ":");
                proxyPort_POST = StringUtils.substringAfter(proxys[index], ":");
            } else {
                proxyHost_GET = StringUtils.substringBefore(proxys[index], ":");
                proxyPort_GET = StringUtils.substringAfter(proxys[index], ":");
            }

        } while (++index < proxys.length);

        return null;
    }

    private static String proxyHost_GET = "218.207.195.206";
    private static String proxyPort_GET = "80";
    private static String proxyHost_POST = "218.207.195.206";
    private static String proxyPort_POST = "80";
    private static String[] proxys = new String[]{
            "218.207.195.206:80",
            "183.136.232.217:3117",
            "183.136.232.218:3118",
            "183.136.232.219:3119",
            "183.136.232.220:3120",
            "183.136.232.221:3121",
            "183.136.232.222:3122",
            "183.136.232.223:3123",
            "183.136.232.224:3124",
            "202.107.247.119:3219",
            "202.107.247.120:3220",
            "202.107.247.121:3221",
            "202.107.247.122:3222",
            "202.107.247.123:3223",
            "202.107.247.124:3224",
            "202.107.247.125:3225",
            "202.107.247.126:3226",
            "220.167.104.186:3186",
            "220.167.104.187:3187",
            "220.167.104.188:3188",
            "220.167.104.189:3189",
            "220.167.104.190:3190",
            "220.167.104.179:3179"};
}
