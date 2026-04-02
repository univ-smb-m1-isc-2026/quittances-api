package com.nobless.quittances.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "app.security")
public class ApiAccessProperties {

        private List<String> allowedOrigins = new ArrayList<>(List.of(
            "http://localhost:5173",
            "https://localhost:5173"
        ));
    private boolean enforceOriginHeader = false;

    public List<String> getAllowedOrigins() {
        return allowedOrigins;
    }

    public void setAllowedOrigins(List<String> allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }

    public boolean isEnforceOriginHeader() {
        return enforceOriginHeader;
    }

    public void setEnforceOriginHeader(boolean enforceOriginHeader) {
        this.enforceOriginHeader = enforceOriginHeader;
    }
}