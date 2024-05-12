package com.phone.devices.service;

import com.phone.devices.config.HttpClientConfig;
import com.phone.devices.domain.PhoneInfo;
import com.phone.devices.exception.FonoAPIException;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.context.ActiveProfiles;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

@ActiveProfiles("it")
class FonoAPIServiceTest {

    private FonoAPIService fonoAPIService;

    private PhoneServiceDB phoneServiceDB;

    private HttpClientConfig httpClientConfig;

    private CloseableHttpClient httpClientMock;

    private final static String FONO_API_BASE_URL = "https://fonoapi.freshpixl.com/v1/getdevice";

    private final static String deviceName = "2x Samsung Galaxy S8";

    @BeforeEach
    public void setup() throws URISyntaxException {
        phoneServiceDB = Mockito.mock(PhoneServiceDB.class);
        httpClientConfig = Mockito.mock(HttpClientConfig.class);

        // Mock httpClientConfig.httpClient() to return a mock CloseableHttpClient
        httpClientMock = Mockito.mock(CloseableHttpClient.class);
        Mockito.when(httpClientConfig.httpClient()).thenReturn(httpClientMock);

        // Mock the PhoneServiceDB
        fonoAPIService = spy(new FonoAPIService(phoneServiceDB, httpClientConfig));

        // Mock the uri
        URI uri = new URI(FONO_API_BASE_URL + "?token=" + URLEncoder.encode("dev") + "&device=" + URLEncoder.encode(deviceName));
        doReturn(uri).when(fonoAPIService).buildUri(anyString());
    }

    @Test
    public void testGetPhoneInfo_SuccessfulResponse() throws IOException, URISyntaxException {

        PhoneInfo phoneInfo = new PhoneInfo(
                "TestTechnology",
                "Test2GBands",
                "Test3GBands",
                "Test4GBands");

        // Mock response entity and status line
        String mockResponseData = """
                [
                  {
                    "technology": "TestTechnology",
                    "_2gBands": "Test2GBands",
                    "_3gBands": "Test3GBands",
                    "_4gBands": "Test4GBands"
                  }
                ]
                """;

        InputStream inputStream = new ByteArrayInputStream(mockResponseData.getBytes(StandardCharsets.UTF_8));
        HttpEntity httpEntityMock = Mockito.mock(HttpEntity.class);
        Mockito.when(httpEntityMock.getContent()).thenReturn(inputStream);

        // Mock response status line
        StatusLine statusLineMock = Mockito.mock(StatusLine.class);
        Mockito.when(statusLineMock.getStatusCode()).thenReturn(200);

        // Mock CloseableHttpResponse
        CloseableHttpResponse httpResponseMock = Mockito.mock(CloseableHttpResponse.class);
        Mockito.when(httpResponseMock.getEntity()).thenReturn(httpEntityMock);
        Mockito.when(httpResponseMock.getStatusLine()).thenReturn(statusLineMock);

        // Mock httpClient.execute() to return the mock response
        Mockito.when(httpClientMock.execute(any())).thenReturn(httpResponseMock);

        // Test the method
        PhoneInfo result = fonoAPIService.getPhoneInfo(deviceName);

        // Verify the result
        assertEquals(phoneInfo, result);
    }

    @Test
    public void testGetPhoneInfo_NonSuccessfulResponse() throws IOException, URISyntaxException {

        // Mock response status line
        StatusLine statusLineMock = Mockito.mock(StatusLine.class);
        Mockito.when(statusLineMock.getStatusCode()).thenReturn(500);

        // Mock CloseableHttpResponse
        CloseableHttpResponse httpResponseMock = Mockito.mock(CloseableHttpResponse.class);
        Mockito.when(httpResponseMock.getStatusLine()).thenReturn(statusLineMock);

        // Mock httpClient.execute() to return the mock response
        Mockito.when(httpClientMock.execute(any())).thenReturn(httpResponseMock);

        // Test the method
        assertThrows(FonoAPIException.class, () -> fonoAPIService.getPhoneInfo("testDevice"));
    }
}
