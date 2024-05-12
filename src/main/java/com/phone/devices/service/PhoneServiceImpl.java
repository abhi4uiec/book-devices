package com.phone.devices.service;

import com.phone.devices.configuration.PhoneConfiguration;
import com.phone.devices.constants.Constants;
import com.phone.devices.domain.Phone;
import com.phone.devices.exception.PhoneNotAvailableException;
import com.phone.devices.exception.PhoneNotFoundException;
import com.phone.devices.exception.PhoneNotReturnedException;
import com.phone.devices.util.DateTimeUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PhoneServiceImpl implements PhoneService {

    private final PhoneConfiguration phoneConfiguration;

    private final FonoAPIService fonoAPIService;

    private Map<String, Phone> phoneInventory;

    public PhoneServiceImpl(final PhoneConfiguration phoneConfiguration, final FonoAPIService fonoAPIService) {
        this.phoneConfiguration = phoneConfiguration;
        this.fonoAPIService = fonoAPIService;
    }

    // Method to initialize phones in the inventory
    @PostConstruct
    public void initialize() {
        loadPhonesFromConfiguration();
    }

    /**
     * Loads phones from configuration and initializes the phone inventory.
     */
    private void loadPhonesFromConfiguration() {
        phoneInventory = new HashMap<>();
        for (String model : phoneConfiguration.getModels()) {
            phoneInventory.put(model, new Phone(model));
        }
    }

    /**
     * Retrieves phone details by model.
     *
     * @param model The model of the phone to retrieve.
     * @return The phone object with the specified model.
     * @throws PhoneNotFoundException If the phone model is not found in the inventory.
     */
    @Override
    public Phone getPhone(final String model, boolean isFetchAdditionalInfo) {
        Phone phone = phoneInventory.get(model);
        if (phone == null) {
            throw new PhoneNotFoundException("Phone model not found: " + model);
        }
        // this flag is used to avoid calling fonoAPI when there is an exception while booking/returning a phone
        if (isFetchAdditionalInfo) {
            phone.setPhoneInfo(fonoAPIService.getPhoneInfo(model));
        }
        return phone;
    }

    /**
     * Books a phone by model for a specific user.
     *
     * @param model     The model of the phone to book.
     * @param bookedBy  The user who is booking the phone.
     * @return The phone that has been booked.
     * @throws PhoneNotAvailableException If the phone is not available for booking.
     */
    @Override
    public Phone bookPhone(final String model, final String bookedBy) {
        Phone phone = getPhone(model, false);
        if (!phone.isAvailable()) {
            throw new PhoneNotAvailableException("Phone not available for booking: " + model);
        }

        phone.setAvailable(false);
        phone.setBookedDate(DateTimeUtil.currentDate());
        phone.setBookedBy(bookedBy);
        phone.setPhoneInfo(fonoAPIService.getPhoneInfo(model));

        return phone;
    }

    /**
     * Returns a booked phone by model.
     *
     * @param model The model of the phone to return.
     * @return The phone that has been returned.
     * @throws PhoneNotReturnedException If the phone is not returned or does not exist.
     */
    @Override
    public Phone returnPhone(final String model) {
        Phone phone = getPhone(model, false);
        if (phone.isAvailable()) {
            throw new PhoneNotReturnedException("Phone not booked or does not exist: " + model);
        }

        phone.setAvailable(true);
        phone.setBookedDate(Constants.NONE);
        phone.setBookedBy(Constants.NONE);
        phone.setPhoneInfo(fonoAPIService.getPhoneInfo(model));

        return phone;
    }

}
