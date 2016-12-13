package com.golubets.monitor.environment.model;

import java.util.Locale;

/**
 * Created by golubets on 19.07.2016.
 */
public enum ConnectionType {
    SERIAL,
    ETHERNET;

    public String toString() {
        return name().toLowerCase(Locale.US);
    }
}
