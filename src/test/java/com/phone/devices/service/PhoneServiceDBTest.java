package com.phone.devices.service;

import com.phone.devices.constants.Constants;
import com.phone.devices.domain.PhoneInfo;
import com.phone.devices.entity.PhoneModel;
import com.phone.devices.repo.PhoneModelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ActiveProfiles("it")
class PhoneServiceDBTest {

    private PhoneServiceDB phoneServiceDB;

    @Mock
    private PhoneModelRepository phoneModelRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        phoneServiceDB = new PhoneServiceDB(phoneModelRepository);
    }

    @Test
    void testGetPhoneInfo_DataFound() {
        // Arrange
        String deviceName = "TestDevice";
        String networkTechnology = "TestTechnology";
        String network2GBands = "Test2GBands";
        String network3GBands = "Test3GBands";
        String network4GBands = "Test4GBands";

        PhoneInfo expectedPhoneInfo = new PhoneInfo(networkTechnology, network2GBands, network3GBands, network4GBands);

        when(phoneModelRepository.findById(deviceName))
                .thenReturn(
                        Optional.of(new PhoneModel(deviceName, networkTechnology, network2GBands, network3GBands, network4GBands)
        ));

        // Act
        PhoneInfo actualPhoneInfo = phoneServiceDB.getPhoneInfo(deviceName);

        // Assert
        assertEquals(expectedPhoneInfo, actualPhoneInfo);
        verify(phoneModelRepository, times(1)).findById(deviceName);
    }

    @Test
    void testGetPhoneInfo_DataNotFound() {
        // Arrange
        String deviceName = "NonExistentDevice";
        PhoneInfo expectedPhoneInfo = new PhoneInfo(
                Constants.NOT_AVAILABLE,
                Constants.NOT_AVAILABLE,
                Constants.NOT_AVAILABLE,
                Constants.NOT_AVAILABLE);

        when(phoneModelRepository.findById(deviceName)).thenReturn(Optional.empty());

        // Act
        PhoneInfo actualPhoneInfo = phoneServiceDB.getPhoneInfo(deviceName);

        // Assert
        assertEquals(expectedPhoneInfo, actualPhoneInfo);
        verify(phoneModelRepository, times(1)).findById(deviceName);
    }

}
