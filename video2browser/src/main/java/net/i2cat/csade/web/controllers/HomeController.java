package net.i2cat.csade.web.controllers;

import java.util.Locale;

import net.i2cat.csade.services.PerUserPresenceServiceImpl;
import net.i2cat.csade.services.PresenceService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController extends AbstractExceptionController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	@Autowired private PresenceService presenceService;
	

	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String Home(Locale locale, Model model) {
		
		return "redirect:/app/index.html";
	}

	
}
