package com.golubets.monitor.environment.web.servlet;

import com.golubets.monitor.environment.dao.ArduinoDao;
import com.golubets.monitor.environment.dao.DataDao;
import com.golubets.monitor.environment.dao.MailSettingsDao;
import com.golubets.monitor.environment.dao.UserDao;
import com.golubets.monitor.environment.mail.EmailSender;
import com.golubets.monitor.environment.model.Arduino;
import com.golubets.monitor.environment.model.DataEntity;
import com.golubets.monitor.environment.model.MailSettings;
import com.golubets.monitor.environment.util.ArduinoIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
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
        modelAndView.setViewName("index");
        return modelAndView;
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
        List<Arduino> list = arduinoDao.getAll();
        for (Arduino arduino : list){
            DataEntity dataEntity = dataDao.getLastRowByArduino(arduino);
            arduino.setTemp(dataEntity.getTemp());
            arduino.setHum(dataEntity.getHum());
        }
            modelAndView.addObject("arduinos", list);
        modelAndView.setViewName("/settings/settingsArduino");
        return modelAndView;
    }

    @RequestMapping(value = "/settings/arduino/new")
    public ModelAndView settingsArduinoNew() {
        ModelAndView modelAndView = new ModelAndView();
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
        modelAndView.setViewName("/settings/editArduino");
        return modelAndView;
    }

    @RequestMapping(value = "/settings/arduino/{id}", method = RequestMethod.POST)
    public String saveArduinoId(@Valid Arduino arduino, BindingResult result, @PathVariable int id) {

        if (result.hasErrors()) {
            return "redirect:/settings/arduino/" + id;
        }

        arduinoDao.persist(arduino);
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
