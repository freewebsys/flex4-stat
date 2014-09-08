package com.freewebsys.action;

import com.freewebsys.service.StatBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * User: freewebsys.com
 */
@Controller
@RequestMapping(value = "/stat")
public class StatBaseController {

    @Autowired
    private StatBaseService statBaseService;

    @RequestMapping(value = "/menu", method = {RequestMethod.GET})
    @ResponseBody
    public String menu(HttpServletRequest request, Model view) {
        return statBaseService.readConfFile("menu.xml");
    }

    @RequestMapping(value = "/module/data", method = {RequestMethod.GET})
    @ResponseBody
    public String moduleData(HttpServletRequest request, @RequestParam(value = "moduleId", defaultValue = "") String moduleId, Model view) {
        String confData =  statBaseService.readConfFile("/module-conf/" + moduleId + ".xml");

        return statBaseService.findStat(confData);
    }

    @RequestMapping(value = "/find", method = {RequestMethod.GET})
    @ResponseBody
    public String findStat() {

        statBaseService.findStat(null);
        return "";
    }
}
