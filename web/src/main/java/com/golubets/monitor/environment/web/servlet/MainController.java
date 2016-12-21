package com.golubets.monitor.environment.web.servlet;

import com.golubets.monitor.environment.dao.ArduinoDao;
import com.golubets.monitor.environment.dao.MailSettingsDao;
import com.golubets.monitor.environment.dao.UserDao;
import com.golubets.monitor.environment.model.MailSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
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
    @RequestMapping(value = "/settings/arduino")
    public ModelAndView settingsArduino() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("arduinos",arduinoDao.getAll());
        modelAndView.setViewName("/settings/settingsArduino");
        return modelAndView;
    }
    @RequestMapping(value = "/settings/arduino/new")
    public ModelAndView settingsArduinoNew() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/settings/addArduino");
        return modelAndView;
    }

    @RequestMapping(value = "/settings/arduino/{id}")
    public ModelAndView settingsArduinoId(@PathVariable int id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("arduino",arduinoDao.getByID(id));
        modelAndView.setViewName("/settings/editArduino");
        return modelAndView;
    }

    @RequestMapping(value = "/settings/email")
    public ModelAndView settingsEmail() {
        ModelAndView modelAndView = new ModelAndView();
        List<MailSettings> list = mailSettingsDao.getAll();
        modelAndView.setViewName("/settings/settingsEmail");
        MailSettings mailSettings = (list.size()==1)?list.get(0):new MailSettings();
        modelAndView.addObject("mailSettings",mailSettings);
        return modelAndView;
    }
}
