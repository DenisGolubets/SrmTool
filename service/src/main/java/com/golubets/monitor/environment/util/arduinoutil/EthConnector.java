package com.golubets.monitor.environment.util.arduinoutil;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * Created by golubets on 17.07.2016.
 */
public class EthConnector implements Connector, AutoCloseable {
    private static final Logger log = Logger.getLogger(EthConnector.class);
    private int reconnectTime;
    private final String server;
    private final Integer port;
    private Socket socket;
    private BufferedReader in;
    private OutputStreamWriter out;

    private int connectCount = 0;

    public StringBuilder sb;

    public synchronized void close() throws IOException {
        if (socket != null && socket.isConnected()) {
            socket.close();
        }
        in.close();
        out.close();
    }

    @Override
    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }


    public EthConnector(String server, Integer port) throws IOException {
        sb = new StringBuilder();
        this.server = server;
        this.port = port;
        connect(this.server, this.port);
        reconnectTime = 1;
    }

    private void connect(String server, Integer port) throws IOException {

        try {
            socket = new Socket(server, port);

            socket.setKeepAlive(false);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            out = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
        } catch (IOException e) {
            reconnect();
        }
    }

    private void reconnect() throws IOException {

        if (connectCount >= 10) {
            connectCount = 0;
            throw new SocketTimeoutException();
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e1) {
            throw new IOException(e1);
        }
        connect(server, port);
        connectCount++;
    }


    public String getResponse(String request) throws IOException {
        while (true) {
            try {
                if (!(socket.isConnected())) {
                    reconnect();
                }
                sb.setLength(0);
                out.write(request);
                out.flush();

                while (sb.indexOf("end") < 1) {
                    sb.append(in.readLine());
                }
                break;
            } catch (SocketTimeoutException e) {
                reconnect();
            } catch (IOException e) {
                throw new IOException(e);
            }
        }

        //return string without space and last word "end"
        return sb.toString().substring(0, sb.length() - 3).replaceAll("[\\s]", "");
    }

    public void setDate(String request) throws IOException {
        out.write(request);
    }
}
