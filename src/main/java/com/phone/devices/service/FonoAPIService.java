package com.phone.devices.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phone.devices.config.HttpClientConfig;
import com.phone.devices.domain.PhoneInfo;
import com.phone.devices.exception.FonoAPIException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;

@Service
@Slf4j
public class FonoAPIService {

    @Value("${fonoapi.token}")
    private String apiToken;

    @Value("${fonoapi.url}")
    private String fonoAPIURL;

    private final CloseableHttpClient httpClient;

    private final PhoneServiceDB phoneServiceDB;

    private static final String FONO_SERVICE = "fonoservice";

    public FonoAPIService(final PhoneServiceDB phoneServiceDB, final HttpClientConfig httpClientConfig) {
        this.httpClient = httpClientConfig.httpClient();
        this.phoneServiceDB = phoneServiceDB;
    }

    /**
     * Retrieves phone information using the FonoAPI service.
     * If the call fails, it falls back to retrieving information from the database.
     *
     * @param deviceName The name of the device for which information is requested.
     * @return PhoneInfo containing information about the device.
     * @throws FonoAPIException If an error occurs while calling the FonoAPI service.
     */
    // First retry is executed, and then circuitbreaker
    @CircuitBreaker(name = FONO_SERVICE, fallbackMethod = "fallbackPhoneInfo")
    @Retry(name = "fonoservice")
    public PhoneInfo getPhoneInfo(final String deviceName) {

        try {
            HttpGet httpGet = new HttpGet(buildUri(deviceName));

            try (final CloseableHttpResponse response = httpClient.execute(httpGet)) {

                if (response.getStatusLine().getStatusCode() == 200) {
                    return parseResponse(response.getEntity().getContent(), deviceName);
                } else {
                    log.error("Call to fonoAPI to fetch device information for model {} failed at {}", deviceName, ZonedDateTime.now());
                    throw new FonoAPIException("Error calling fonoAPI to fetch device information for model: " + deviceName);
                }
            }
        } catch (final URISyntaxException | IOException ex) {
            log.error("Check why rest call to Fono API failed");
            throw new RuntimeException("Error processing fonoAPI to fetch device information for model: " + deviceName);
        }
    }

    /**
     * Fallback method to retrieve phone information in case of an exception.
     *
     * @param deviceName The name of the device for which phone information is requested.
     * @param ex         The exception that occurred.
     * @return PhoneInfo containing information about the device.
     */
    public PhoneInfo fallbackPhoneInfo(final String deviceName, final Exception ex) {
        log.error(ex.getMessage());
        return phoneServiceDB.getPhoneInfo(deviceName);
    }

    public URI buildUri(final String deviceName) throws URISyntaxException {
        URIBuilder builder = new URIBuilder(fonoAPIURL);
        builder.setParameter("token", apiToken);
        builder.setParameter("device", deviceName);
        return builder.build();
    }

    private PhoneInfo parseResponse(final InputStream inputStream, final String deviceName) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        PhoneInfo[] phoneInfo = objectMapper.readValue(inputStream, PhoneInfo[].class);
        // Check if phoneInfo array is not null and not empty
        return (phoneInfo != null && phoneInfo.length > 0) ? phoneInfo[0] : phoneServiceDB.getPhoneInfo(deviceName);
    }

}
