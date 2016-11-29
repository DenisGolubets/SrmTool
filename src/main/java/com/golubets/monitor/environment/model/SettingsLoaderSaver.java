package com.golubets.monitor.environment.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.golubets.monitor.environment.model.baseobject.BaseObject;
import com.golubets.monitor.environment.model.mail.MailSettings;
import org.apache.log4j.Logger;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by golubets on 24.08.2016.
 */
public class SettingsLoaderSaver {
    private static final Logger log = Logger.getLogger(SettingsLoaderSaver.class);

    private static final byte[] k = "2w32eEt9!".getBytes(Charset.forName("UTF-8"));
    private static final SecretKey key64 = new SecretKeySpec(k, "Blowfish");

    private final static String SETTING_FILE = "setting.properties";
    private final static String ENCRYPT_SETTING_FILE = "setting.dat";

    @JsonDeserialize(as=HashMap.class)
    private Map<String, BaseObject> settingsMap;
    private static File settingFile = new File(SETTING_FILE);
    private static File cryptSettingFile = new File(ENCRYPT_SETTING_FILE);

    private static Cipher cipher = null;


    public SettingsLoaderSaver() {
        settingsMap = new HashMap<>();
    }


    public static void saveSettingsToJsonFile(Map<String, BaseObject> saveMap) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(settingFile,saveMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveEncryptedSettingsToJsonFile(Map<String, BaseObject> saveMap) {
        initCipher();
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key64);
        } catch (InvalidKeyException e) {
            log.error(e);
        }
        try (OutputStream out = new CipherOutputStream(new BufferedOutputStream(new FileOutputStream(cryptSettingFile)), cipher)) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(out, saveMap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public Map<String, BaseObject> loadSettingsFromJsonFile() {
        HashMap<String,BaseObject> map = null;
        if (settingFile.exists() && !settingFile.isDirectory()) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                TypeFactory typeFactory = mapper.getTypeFactory();
                MapType mapType = typeFactory.constructMapType(HashMap.class, String.class, MailSettings.class);
                map = mapper.readValue(settingFile, mapType);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Map map = mapper.readValues(settingFile, HashMap.class);

        }
        return map;
    }

    public Map<String, BaseObject> loadEncryptelSettingsFromJsonFile() {
        HashMap<String,BaseObject> map = null;
        initCipher();
        try {
            cipher.init(Cipher.DECRYPT_MODE, key64);
        } catch (InvalidKeyException e) {
            log.error(e);
        }
        if (cryptSettingFile.exists() && !cryptSettingFile.isDirectory()) {
            try (InputStream in = new CipherInputStream(new BufferedInputStream(new FileInputStream(cryptSettingFile)), cipher)) {
                ObjectMapper mapper = new ObjectMapper();
                TypeFactory typeFactory = mapper.getTypeFactory();
                MapType mapType = typeFactory.constructMapType(HashMap.class, String.class, MailSettings.class);
                map = mapper.readValue(in, mapType);
            } catch (FileNotFoundException e) {
                log.error(SETTING_FILE + " not found", e);
            } catch (IOException e) {
                log.error(e);
            }
        }

//        if (settingFile.exists() && !settingFile.isDirectory()) {
//            try {
//                ObjectMapper mapper = new ObjectMapper();
//                TypeFactory typeFactory = mapper.getTypeFactory();
//                MapType mapType = typeFactory.constructMapType(HashMap.class, String.class, MailSettings.class);
//                map = mapper.readValue(settingFile, mapType);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            //Map map = mapper.readValues(settingFile, HashMap.class);
//
//        }


        return map;
    }


    private static void initCipher() {
        if (cipher == null) {
            try {
                cipher = Cipher.getInstance("Blowfish");
            } catch (NoSuchAlgorithmException e) {
                log.error(e);
            } catch (NoSuchPaddingException e) {
                log.error(e);
            }
        }
    }

    public static void saveCryptSettingsToFile(Map<String, BaseObject> saveMap) {
        initCipher();
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key64);
        } catch (InvalidKeyException e) {
            log.error(e);
        }

        try (CipherOutputStream cipherOutputStream = new CipherOutputStream(new BufferedOutputStream(new FileOutputStream(cryptSettingFile)), cipher);
             ObjectOutputStream out = new ObjectOutputStream(cipherOutputStream)) {
            out.writeObject(saveMap);
        } catch (FileNotFoundException e) {
            log.error(SETTING_FILE + " not found", e);
        } catch (IOException e) {
            log.error(e);
        }
    }

    public Map<String, BaseObject> loadCryptSettingsFromFile() {
        initCipher();
        try {
            cipher.init(Cipher.DECRYPT_MODE, key64);
        } catch (InvalidKeyException e) {
            log.error(e);
        }
        if (cryptSettingFile.exists() && !cryptSettingFile.isDirectory()) {
            try (CipherInputStream cipherInputStream = new CipherInputStream(new BufferedInputStream(new FileInputStream(cryptSettingFile)), cipher);
                 ObjectInputStream in = new ObjectInputStream(cipherInputStream)) {
                settingsMap = (Map<String, BaseObject>) in.readObject();
            } catch (FileNotFoundException e) {
                log.error(SETTING_FILE + " not found", e);
            } catch (IOException e) {
                log.error(e);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return settingsMap;
    }

    public static void saveSettingsToFile(Map<String, BaseObject> saveMap) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(settingFile))) {
            out.writeObject(saveMap);
        } catch (FileNotFoundException e) {
            log.error(SETTING_FILE + " not found", e);
        } catch (IOException e) {
            log.error(e);
        }
    }

    public Map<String, BaseObject> loadSettingsFromFile() {
        if (settingFile.exists() && !settingFile.isDirectory()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(settingFile))) {
                settingsMap = (Map<String, BaseObject>) in.readObject();
            } catch (FileNotFoundException e) {
                log.error(SETTING_FILE + " not found", e);
            } catch (IOException e) {
                log.error(e);
            } catch (ClassNotFoundException e) {
                log.error(e);
            }
        }
        return settingsMap;
    }
}
