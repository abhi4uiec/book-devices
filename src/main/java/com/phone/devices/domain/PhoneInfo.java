package com.phone.devices.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhoneInfo {

    private String technology;

    private String _2gBands;

    private String _3gBands;

    private String _4gBands;

}
