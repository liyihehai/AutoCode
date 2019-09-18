package com.nnte.ac_backend.controller.autoCode;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "nnte-ac.conf.autocode")
@PropertySource(value = "classpath:nnte-autocode-config.properties")
public class AutoCodeLocalConfig {
    private String debug;
    private String staticRoot;
    private String localHostName;
    private String localHostAbstractName;

    public String getLocalHostName() {
        return localHostName;
    }

    public void setLocalHostName(String localHostName) {
        this.localHostName = localHostName;
    }

    public String getLocalHostAbstractName() {
        return localHostAbstractName;
    }

    public void setLocalHostAbstractName(String localHostAbstractName) {
        this.localHostAbstractName = localHostAbstractName;
    }

    public String getDebug() {
        return debug;
    }

    public void setDebug(String debug) {
        this.debug = debug;
    }

    public String getStaticRoot() {
        return staticRoot;
    }

    public void setStaticRoot(String staticRoot) {
        this.staticRoot = staticRoot;
    }
}
