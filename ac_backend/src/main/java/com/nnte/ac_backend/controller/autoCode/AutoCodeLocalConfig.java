package com.nnte.ac_backend.controller.autoCode;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "nnte-ac.conf.autocode")
@PropertySource(value = "classpath:nnte-autocode-config.properties")
@Data
public class AutoCodeLocalConfig {
    private String debug;
    private String staticRoot;
    private String localHostName;
    private String localHostAbstractName;
    private String frontHosts;
}
