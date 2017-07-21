package com.golubets.monitor.environment.util.arduinoutil;

import com.golubets.monitor.environment.model.Arduino;
import com.golubets.monitor.environment.model.ConnectionType;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Created by golubets on 13.02.2017.
 */
public class ArduinoUtil {
    private final Logger log = Logger.getLogger(ArduinoUtil.class);
    private Connector connector;
    private Arduino arduino;

    public ArduinoUtil(Connector connector, Arduino arduino) {
        this.connector = connector;
        this.arduino = arduino;
    }

    private void getFullData () throws NumberFormatException, IOException {
        String[] splitString = connector.getResponse("I").split("\\|");
        for (String s : splitString) {
            String[] row = s.split(":");
            switch (row[0]) {
                case "Mac":
                    arduino.setMac(row[1]);
                    break;
                case "Ip":
                    arduino.setIp(row[1]);
                    break;
                case "Subnet":
                    arduino.setSubnet(row[1]);
                    break;
                case "Gateway":
                    arduino.setGateway(row[1]);
                    break;
                case "DNS":
                    arduino.setDns(row[1]);
                    break;
                case "isEthEnable":
                    if (row[1].equals("1"))
                        arduino.setConnectionType(ConnectionType.ETHERNET);
                    break;
                case "DHTport":
                    arduino.setDhtPort(Integer.parseInt(row[1]));
                    break;
                case "DHTtype":
                    arduino.setDhtType(Integer.parseInt(row[1]));
                    break;
                case "TopT":
                    arduino.setTopT(Integer.parseInt(row[1]));
                    break;
                case "TopH":
                    arduino.setTopH(Integer.parseInt(row[1]));
                    break;
                case "isAlertT":
                    arduino.setAlertT(row[1].equals("1") ? true : false);
                    break;
                case "isAlertH":
                    arduino.setAlertT(row[1].equals("1") ? true : false);
                    break;
                case "AVG10minT":
                    arduino.setTemp(Double.parseDouble(row[1]));
                    break;
                case "AVG10minH":
                    arduino.setHum(Double.parseDouble(row[1]));
                    break;
            }
        }
    }

}
