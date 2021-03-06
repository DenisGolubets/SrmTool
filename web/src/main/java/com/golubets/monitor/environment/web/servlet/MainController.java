package com.golubets.monitor.environment.web.servlet;

import com.golubets.monitor.environment.dao.ArduinoDao;
import com.golubets.monitor.environment.dao.DataDao;
import com.golubets.monitor.environment.dao.MailSettingsDao;
import com.golubets.monitor.environment.dao.UserDao;
import com.golubets.monitor.environment.exception.PersistException;
import com.golubets.monitor.environment.mail.EmailSender;
import com.golubets.monitor.environment.model.Arduino;
import com.golubets.monitor.environment.model.AvgDataEntity;
import com.golubets.monitor.environment.model.ConnectionType;
import com.golubets.monitor.environment.model.MailSettings;
import com.golubets.monitor.environment.util.DateUtil;
import com.golubets.monitor.environment.util.arduinoutil.ArduinoIDGenerator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.*;

/**
 * Created by golubets on 20.12.2016.
 */
@Controller
public class MainController {
    private static final Logger LOGGER = Logger.getLogger(MainController.class);

    @Autowired
    private ArduinoDao arduinoDao;
    @Autowired
    MailSettingsDao mailSettingsDao;
    @Autowired
    UserDao userDao;
    @Autowired
    DataDao dataDao;
    @Autowired
    DateUtil dateUtil;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();
        try {
            modelAndView.addObject("arduinos", arduinoDao.getAllWithLastData());
            modelAndView.setViewName("index");
        } catch (PersistException e) {
            LOGGER.error("", e);
        }
        return modelAndView;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Arduino> index(@PathVariable int id) {
        if (id == -1) {
            List<Arduino> list = new ArrayList<>();
            try {
                list = arduinoDao.getAllWithLastData();
            } catch (PersistException e) {
                LOGGER.error("", e);
            }
            return list;
        } else {
            List<Arduino> list = new ArrayList<>();
            try {
                list.add(arduinoDao.getByIDWithLastData(id));
            } catch (PersistException e) {
                LOGGER.error("", e);
            }
            return list;
        }
    }

    @RequestMapping(value = "/d{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    List<AvgDataEntity> indexDayAvgData(@PathVariable int id) {
        List<AvgDataEntity> list = new ArrayList<>();
        try {
            list = dataDao.getAvgLastLimitRecords(arduinoDao.getByID(id), 24);
        } catch (PersistException e) {
            LOGGER.error("", e);
        }
        return list;
    }

    @RequestMapping(value = "/m{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    List<AvgDataEntity> indexMonthAvgData(@PathVariable int id) {
        List<AvgDataEntity> prepareList = new ArrayList<>();
        try {
            String previousHour = dateUtil.getPreviousHour(new Date());
            //get last 720 Hours
            int records = 720;
            prepareList = new ArrayList<>(records);
            for (int i = 0; i < records; i++) {
                if (prepareList.size() == 0) {
                    AvgDataEntity avgDataEntity = new AvgDataEntity();
                    avgDataEntity.setDateTime(previousHour);
                    prepareList.add(avgDataEntity);
                } else {
                    AvgDataEntity avgDataEntity = new AvgDataEntity();
                    String prevHour = dateUtil.getPreviousHour(prepareList.get(prepareList.size() - 1).getDateTime());
                    avgDataEntity.setDateTime(prevHour);
                    prepareList.add(avgDataEntity);
                }
            }
            Collections.sort(prepareList, (o1, o2) -> o1.getDateTime().compareTo(o2.getDateTime()));

            List<AvgDataEntity> list;
            list = dataDao.getAvgLastLimitRecords(arduinoDao.getByID(id), records);

            int listRecords = records - 1;
            for (int i = records - 1; i > 0; i--) {
                if (prepareList.get(i).getDateTime().equals(list.get(listRecords).getDateTime())) {
                    prepareList.set(i, list.get(listRecords));
                    listRecords--;
                }
            }
        } catch (PersistException e) {
            LOGGER.error("", e);
        }
        return prepareList;
    }

    @RequestMapping(value = "settings")
    public ModelAndView settings() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("settings");
        return modelAndView;
    }

    @RequestMapping(value = "login")
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @RequestMapping(value = "/settings/arduino")
    public ModelAndView settingsArduino() {
        ModelAndView modelAndView = new ModelAndView();
        try {
            List<Arduino> list = arduinoDao.getAllWithLastData();
            modelAndView.addObject("arduinos", list);
            modelAndView.setViewName("/settings/settingsArduino");
        } catch (PersistException e) {
            LOGGER.error("", e);
        }
        return modelAndView;
    }

    @RequestMapping(value = "/settings/arduino/new")
    public ModelAndView settingsArduinoNew() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("arduino", new Arduino());
        modelAndView.addObject("connectionType", Arrays.asList(ConnectionType.values()));
        modelAndView.addObject("dhtType", Arrays.asList(11, 22, 23));
        modelAndView.setViewName("/settings/addArduino");
        return modelAndView;
    }

    @RequestMapping(value = "/settings/arduino/new", method = RequestMethod.POST)
    public String saveNewArduino(@Valid Arduino arduino, BindingResult result) {

        try {
            if (result.hasErrors()) {
                return "redirect:/settings/arduino/new";
            }
            if (arduino.getId() == 0) {
                arduino.setId(new ArduinoIDGenerator().genereteId());
            }
            arduinoDao.persist(arduino);
        } catch (PersistException e) {
            LOGGER.error("", e);
        }
        return "redirect:/settings/arduino";

    }

    @RequestMapping(value = "/settings/arduino/{id}")
    public ModelAndView settingsArduinoId(@PathVariable int id) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            modelAndView.addObject("arduino", arduinoDao.getByID(id));
            modelAndView.addObject("connectionType", Arrays.asList(ConnectionType.values()));
            modelAndView.addObject("dhtType", Arrays.asList(11, 22, 23));
            modelAndView.setViewName("/settings/editArduino");
        } catch (PersistException e) {
            LOGGER.error("", e);
        }
        return modelAndView;
    }

    @RequestMapping(value = "/settings/arduino/{id}", method = RequestMethod.POST)
    public String saveArduinoId(@Valid Arduino arduino, BindingResult result, @PathVariable int id) {

        if (result.hasErrors()) {
            return "redirect:/settings/arduino/" + id;
        }
        try {
            arduinoDao.update(arduino);
        } catch (PersistException e) {
            LOGGER.error("", e);
        }
        return "redirect:/settings/arduino";

    }

    @RequestMapping(value = "/settings/email")
    public ModelAndView settingsEmail() {
        ModelAndView modelAndView = new ModelAndView();
        MailSettings mailSettings = null;
        try {
            List<MailSettings> list = mailSettingsDao.getAll();
            modelAndView.setViewName("/settings/settingsEmail");
            mailSettings = (list.size() == 1) ? list.get(0) : new MailSettings();
        } catch (PersistException e) {
            LOGGER.error("", e);
        }
        modelAndView.addObject("mailSettings", mailSettings);
        return modelAndView;
    }

    @RequestMapping(value = "/settings/email", method = RequestMethod.POST)
    public String saveSettingsEmail(@Valid MailSettings mailSettings, BindingResult result) {

        if (result.hasErrors()) {
            return "redirect:/settings/email";
        }
        try {
            EmailSender emailSender = new EmailSender(mailSettings);
            emailSender.sendMail("Test", "This email is tested from STMTool");
        } catch (Exception e) {
            return "redirect:/settings/email";
        }
        try {
            mailSettingsDao.persist(mailSettings);
        } catch (PersistException e) {
            LOGGER.error("", e);
        }
        return "redirect:/settings/";

    }
}
