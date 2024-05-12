package com.phone.devices.service;

import com.phone.devices.domain.Phone;

public interface PhoneService {

    /**
     *
     * @param model
     * @return
     */
    Phone getPhone(final String model, boolean isFetchAdditionalInfo);

    /**
     *
     * @param model
     * @param bookedBy
     * @return
     */
    Phone bookPhone(final String model, final String bookedBy);

    /**
     *
     * @param model
     * @return
     */
    Phone returnPhone(final String model);

}
