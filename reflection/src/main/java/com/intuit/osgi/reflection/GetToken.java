package com.intuit.osgi.reflection;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpField;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.util.HttpCookieStore;
import org.eclipse.jetty.util.SocketAddressResolver;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.intuit.osgi.reflection.Constants.SERVER_PORT;
import static com.intuit.osgi.reflection.Constants.SYSTEM_PROPERTY_VALIDATION_ENDPOINT_KEY;

/**
 * Created by aattuluri on 7/23/17.
 *
 *
 * Makes http requests to /validate on localhost:SERVER_PORT to get a randomly generated token
 *
 */
@Component
public class GetToken {

    static String URL = System.getProperty(SYSTEM_PROPERTY_VALIDATION_ENDPOINT_KEY, "http://localhost:"+SERVER_PORT+"/validate");

    HttpClient httpClient = null;

    public GetToken() {

    }

    @PostConstruct
    public void init() throws Exception {

        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(100, // core thread pool size
                200, // maximum thread pool size
                30, // time to wait before resizing pool
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(200 * 2, true),
                new ThreadPoolExecutor.CallerRunsPolicy());


        httpClient = new HttpClient(new SslContextFactory());

        httpClient.setMaxRequestsQueuedPerDestination(200);
        httpClient.setMaxConnectionsPerDestination(200);
        httpClient.setConnectTimeout(30000);
        httpClient.setFollowRedirects(false);
        httpClient.setCookieStore(new HttpCookieStore.Empty());
        httpClient.setExecutor(threadPool);
        httpClient.setIdleTimeout(60000);
        httpClient.setRequestBufferSize(32000);


        httpClient.setUserAgentField(new HttpField(HttpHeader.USER_AGENT, HttpHeader.USER_AGENT.asString(), null));
        httpClient.setStrictEventOrdering(false);
        httpClient.setSocketAddressResolver(new SocketAddressResolver.Sync());
        httpClient.setConnectBlocking(true);

        httpClient.getContentDecoderFactories().clear();

        httpClient.start();
        httpClient.getProtocolHandlers().remove("www-authenticate");
    }

    public String getToken(String payload) throws Exception {

        ContentResponse contentResponse = httpClient.newRequest(URL).
                method(HttpMethod.GET).
                send();
        if(contentResponse.getStatus() == 200) {
            return new String(contentResponse.getContent());
        } else {
            return payload;
        }

    }

    @PreDestroy
    public void destroy() throws Exception {
        httpClient.stop();
    }
}
