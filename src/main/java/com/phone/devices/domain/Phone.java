package com.phone.devices.domain;

import com.phone.devices.constants.Constants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Phone {

    private String model;
    private boolean isAvailable;
    private String bookedDate;
    private String bookedBy;

    // Additional information retrieved from FonoAPI
    private PhoneInfo phoneInfo;

    public Phone(final String model) {
        this.model = model;
        this.isAvailable = true;
        this.bookedBy = Constants.NONE;
        this.bookedDate = Constants.NONE;
    }
}
