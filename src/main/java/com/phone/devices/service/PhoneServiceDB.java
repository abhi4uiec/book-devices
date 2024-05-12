package com.phone.devices.service;

import com.phone.devices.constants.Constants;
import com.phone.devices.domain.PhoneInfo;
import com.phone.devices.repo.PhoneModelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PhoneServiceDB {

    private final PhoneModelRepository phoneModelRepository;

    public PhoneServiceDB(final PhoneModelRepository phoneModelRepository) {
        this.phoneModelRepository = phoneModelRepository;
    }

    /**
     * Retrieves phone information from the database for the specified device name.
     * If the information is not found in the database, returns default information.
     *
     * @param deviceName The name of the device for which information is requested.
     * @return PhoneInfo containing the network technology and bands information for the device.
     */
    public PhoneInfo getPhoneInfo(final String deviceName) {
        log.info("Fetching data from DB for device {}", deviceName);

        return phoneModelRepository.findById(deviceName)
                .map(model ->
                        // Maps PhoneModel entity to PhoneInfo DTO
                        new PhoneInfo(
                                model.getNetworkTechnology(),
                                model.getNetwork2GBands(),
                                model.getNetwork3GBands(),
                                model.getNetwork4GBands()
                        ))
                .orElseGet( () -> {
                    // Logs an error if no data is found for the device in the database
                    log.error("No data found in DB for device {}", deviceName);
                    // Returns default PhoneInfo if no data is found in the database
                    return new PhoneInfo(
                            Constants.NOT_AVAILABLE,
                            Constants.NOT_AVAILABLE,
                            Constants.NOT_AVAILABLE,
                            Constants.NOT_AVAILABLE
                    );
                });
    }
}
