package com.golubets.monitor.environment.connection;

import java.io.IOException;

/**
 * Created by golubets on 17.07.2016.
 */
public interface Connector {

    String getResponse(String request) throws IOException;

    void setDate(String request) throws IOException;

    void close() throws IOException;


}
