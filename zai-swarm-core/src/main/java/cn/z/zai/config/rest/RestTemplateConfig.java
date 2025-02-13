package cn.z.zai.config.rest;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Description:**
 */
@Configuration
public class RestTemplateConfig {

    /**
     * RestTemplate
     *
     * @return
     */
    @Bean
    public RestTemplate restTemplate() {
        OkHttpClient httpClient = okHttpClient();
        OkHttp3ClientHttpRequestFactory factory = new OkHttp3ClientHttpRequestFactory(httpClient);
        RestTemplate restTemplate = new RestTemplate(factory);

        List<HttpMessageConverter<?>> converterList = restTemplate.getMessageConverters();
        int converterIndex = -1;
        for (int i = 0; i < converterList.size(); i++) {
            if (converterList.get(i).getClass() == StringHttpMessageConverter.class) {
                converterIndex = i;
                break;
            }
        }
        if (converterIndex != -1) {
            restTemplate.getMessageConverters().set(converterIndex,
                new StringHttpMessageConverter(StandardCharsets.UTF_8));
        }

        return restTemplate;
    }

    @Bean
    public X509TrustManager x509TrustManager() {
        return new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {}

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {}

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
    }

    @Bean
    public SSLSocketFactory sslSocketFactory() {
        try {
            //
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[] {x509TrustManager()}, new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
            // .sslSocketFactory(sslSocketFactory(), x509TrustManager())
            .retryOnConnectionFailure(false).connectionPool(pool()).connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS).build();
    }

    @Bean
    public ConnectionPool pool() {
        return new ConnectionPool(200, 5, TimeUnit.MINUTES);
    }

}
