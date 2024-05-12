package com.phone.devices.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;

import com.phone.devices.constants.Constants;
import com.phone.devices.domain.PhoneInfo;
import com.phone.devices.exception.PhoneNotAvailableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.phone.devices.configuration.PhoneConfiguration;
import com.phone.devices.domain.Phone;
import com.phone.devices.exception.PhoneNotFoundException;
import com.phone.devices.exception.PhoneNotReturnedException;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("it")
class PhoneServiceImplTest {

    private PhoneServiceImpl phoneService;

    @Mock
    private PhoneConfiguration phoneConfiguration;

    @Mock
    private FonoAPIService fonoAPIService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        phoneService = new PhoneServiceImpl(phoneConfiguration, fonoAPIService);
        mockPhoneConfiguration();
        mockFonoAPIService();
        phoneService.initialize();
    }

    private void mockPhoneConfiguration() {
        when(phoneConfiguration.getModels()).thenReturn(Collections.singletonList("TestModel"));
    }

    private void mockFonoAPIService() {
        PhoneInfo phoneInfo = new PhoneInfo(
                "TestTechnology",
                "Test2GBands",
                "Test3GBands",
                "Test4GBands");
        when(fonoAPIService.getPhoneInfo(anyString())).thenReturn(phoneInfo);
    }

    @Test
    void testGetPhone_Success() {
        // Arrange
        String model = "TestModel";

        // Act
        Phone phone = phoneService.getPhone(model, true);

        // Assert
        assertNotNull(phone);
        assertEquals(model, phone.getModel());
    }

    @Test
    void testGetPhone_PhoneNotFoundException() {
        // Arrange
        String model = "NonExistentModel";

        // Act and Assert
        assertThrows(PhoneNotFoundException.class, () -> phoneService.getPhone(model, true));
    }

    @Test
    void testBookPhone_Success() {
        // Arrange
        String model = "TestModel";
        String bookedBy = "TestUser";

        // Act
        Phone phone = phoneService.bookPhone(model, bookedBy);

        // Assert
        assertFalse(phone.isAvailable());
        assertEquals(bookedBy, phone.getBookedBy());
    }

    @Test
    void testBookPhone_PhoneNotAvailableException() {
        // Arrange
        String model = "TestModel";
        String bookedBy = "TestUser";
        phoneService.bookPhone(model, bookedBy);

        // Act and Assert
        assertThrows(PhoneNotAvailableException.class, () -> phoneService.bookPhone(model, bookedBy));
    }

    @Test
    void testReturnPhone_Success() {
        // Arrange
        String model = "TestModel";
        String bookedBy = "TestUser";
        phoneService.bookPhone(model, bookedBy);

        // Act
        Phone returnedPhone = phoneService.returnPhone(model);

        // Assert
        assertTrue(returnedPhone.isAvailable());
        assertEquals(Constants.NONE, returnedPhone.getBookedBy());
    }

    @Test
    void testReturnPhone_PhoneNotReturnedException() {
        // Arrange
        String model = "TestModel";

        // Act and Assert
        assertThrows(PhoneNotReturnedException.class, () -> phoneService.returnPhone(model));
    }
}

