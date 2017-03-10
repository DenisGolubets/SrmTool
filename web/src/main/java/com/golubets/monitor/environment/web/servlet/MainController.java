package com.golubets.monitor.environment.web.servlet;

import com.golubets.monitor.environment.dao.ArduinoDao;
import com.golubets.monitor.environment.dao.DataDao;
import com.golubets.monitor.environment.dao.MailSettingsDao;
import com.golubets.monitor.environment.dao.UserDao;
import com.golubets.monitor.environment.mail.EmailSender;
import com.golubets.monitor.environment.model.Arduino;
import com.golubets.monitor.environment.model.AvgDataEntity;
import com.golubets.monitor.environment.model.ConnectionType;
import com.golubets.monitor.environment.model.MailSettings;
import com.golubets.monitor.environment.util.arduinoutil.ArduinoIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by golubets on 20.12.2016.
 */
@Controller
public class MainController {
    @Autowired
    private ArduinoDao arduinoDao;
    @Autowired
    MailSettingsDao mailSettingsDao;
    @Autowired
    UserDao userDao;
    @Autowired
    DataDao dataDao;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("arduinos", arduinoDao.getAllWithLastData());
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Arduino> index(@PathVariable int id) {
        if (id == -1) {
            return arduinoDao.getAllWithLastData();
        } else {
            List<Arduino> list = new ArrayList<>();
            list.add(arduinoDao.getByIDWithLastData(id));
            return list;
        }
    }

    @RequestMapping(value = "/d{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    List<AvgDataEntity> indexDayAvgData(@PathVariable int id) {
        List<AvgDataEntity> list = new ArrayList<>();
        list = dataDao.getAvgLastLimitRecords(arduinoDao.getByID(id), 24);
        return list;
    }

    @RequestMapping(value = "/m{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    List<AvgDataEntity> indexMonthAvgData(@PathVariable int id) {
        List<AvgDataEntity> list = new ArrayList<>();
        list = dataDao.getAvgLastLimitRecords(arduinoDao.getByID(id),720);
        return list;
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
        List<Arduino> list = arduinoDao.getAllWithLastData();
        modelAndView.addObject("arduinos", list);
        modelAndView.setViewName("/settings/settingsArduino");
        return modelAndView;
    }

    @RequestMapping(value = "/settings/arduino/new")
    public ModelAndView settingsArduinoNew() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("arduino", new Arduino());
        modelAndView.addObject("connectionType", Arrays.asList(ConnectionType.values()));
        modelAndView.addObject("dhtType", Arrays.asList(11,22,23));
        modelAndView.setViewName("/settings/addArduino");
        return modelAndView;
    }

    @RequestMapping(value = "/settings/arduino/new", method = RequestMethod.POST)
    public String saveNewArduino(@Valid Arduino arduino, BindingResult result) {

        if (result.hasErrors()) {
            return "redirect:/settings/arduino/new";
        }
        if (arduino.getId() == 0) {
            arduino.setId(new ArduinoIDGenerator().genereteId());
        }
        arduinoDao.persist(arduino);
        return "redirect:/settings/arduino";

    }

    @RequestMapping(value = "/settings/arduino/{id}")
    public ModelAndView settingsArduinoId(@PathVariable int id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("arduino", arduinoDao.getByID(id));
        modelAndView.addObject("connectionType", Arrays.asList(ConnectionType.values()));
        modelAndView.addObject("dhtType", Arrays.asList(11,22,23));
        modelAndView.setViewName("/settings/editArduino");
        return modelAndView;
    }

    @RequestMapping(value = "/settings/arduino/{id}", method = RequestMethod.POST)
    public String saveArduinoId(@Valid Arduino arduino, BindingResult result, @PathVariable int id) {

        if (result.hasErrors()) {
            return "redirect:/settings/arduino/" + id;
        }

        arduinoDao.update(arduino);
        return "redirect:/settings/arduino";

    }

    @RequestMapping(value = "/settings/email")
    public ModelAndView settingsEmail() {
        ModelAndView modelAndView = new ModelAndView();
        List<MailSettings> list = mailSettingsDao.getAll();
        modelAndView.setViewName("/settings/settingsEmail");
        MailSettings mailSettings = (list.size() == 1) ? list.get(0) : new MailSettings();
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
        mailSettingsDao.persist(mailSettings);
        return "redirect:/settings/";

    }
}
