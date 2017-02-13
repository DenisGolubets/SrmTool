package com.golubets.monitor.environment.util.serializer;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.golubets.monitor.environment.model.Arduino;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by golubets on 24.08.2016.
 */
public class ArduinoSerializer {
    private static final Logger log = Logger.getLogger(ArduinoSerializer.class);

    private static final long serialVersionUID = 0L;

   // private final static String ARDUINO_SETTINGS_FOLDER = "data/";
    private final static String ARDUINO_SETTINGS_FOLDER = "D:/data/";
    private final static String MASK = ".ar";


    public void saveArduinoToJsonFile(List<Arduino> arduinos) {
        for (Arduino a : arduinos) {
            File file = new File(ARDUINO_SETTINGS_FOLDER + a.getId() + MASK);
            ObjectMapper mapper = new ObjectMapper();
            try (FileOutputStream out = new FileOutputStream(file)) {
                mapper.writeValue(file, a);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Arduino> loadArduinoFromJsonFile() {

        List<Arduino> list = new ArrayList<>();
        try {
            File folder = new File(ARDUINO_SETTINGS_FOLDER);
            String[] files = folder.list((folder1, name) -> name.endsWith(MASK));

            if (files.length > 0) {
                for (String s : files) {
                    File file = new File(ARDUINO_SETTINGS_FOLDER + s);
                    if (file.length() > 0) {
                        ObjectMapper mapper = new ObjectMapper();
                        Arduino arduino = mapper.readValue(file, Arduino.class);
                        list.add(arduino);
                    }
                }
            }
        } catch (NullPointerException e) {
            log.error(e);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
