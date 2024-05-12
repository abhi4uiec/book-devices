package com.phone.devices.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "phone_model_info")
public class PhoneModel {

    @Id
    @Column(name = "device_name", nullable = false)
    private String deviceName;

    @Column(name = "technology", nullable = false)
    private String networkTechnology;

    @Column(name = "2g_bands", nullable = false)
    private String network2GBands;

    @Column(name = "3g_bands", nullable = false)
    private String network3GBands;

    @Column(name = "4g_bands", nullable = false)
    private String network4GBands;
}

