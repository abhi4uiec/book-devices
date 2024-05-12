package com.phone.devices.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "phones")
@Getter
@Setter
public class PhoneConfiguration {

    private List<String> models = new ArrayList<>();

}
