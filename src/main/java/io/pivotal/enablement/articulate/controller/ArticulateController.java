package io.pivotal.enablement.articulate.controller;

import io.pivotal.enablement.articulate.model.Attendee;
import io.pivotal.enablement.articulate.service.AttendeeService;
import io.pivotal.enablement.articulate.service.EnvironmentHelper;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 
 * @author mborges
 *
 */
@Controller
public class ArticulateController {

	private Log log = LogFactory.getLog(ArticulateController.class);

	@Autowired
	private AttendeeService attendeeService;

	@Autowired
	private EnvironmentHelper environmentHelper;

	@RequestMapping("/")
	public String index(Model model) throws Exception {
		addAppEnv(model);
		return "index";
	}

	@RequestMapping(value = "/basics", method = RequestMethod.GET)
	public String kill(@RequestParam(value = "doit", required = false) boolean doit, Model model) throws Exception {

		addAppEnv(model);

		if (doit) {
			model.addAttribute("killed", true);
			log.warn("*** The system is shutting down. ***");
			Runnable killTask = () -> {
				try {
					String name = Thread.currentThread().getName();
					log.warn("killing shortly " + name);
					TimeUnit.SECONDS.sleep(5);
					log.warn("killed " + name);
					System.exit(0);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			};
			new Thread(killTask).start();
		}

		return "basics";

	}

	@RequestMapping(value = "/services", method = RequestMethod.GET)
	public String attendees(Model model) throws Exception {

		model.addAttribute("attendees", attendeeService.getAttendees());
		
		addAppEnv(model);
		return "services";
	}

	@RequestMapping(value = "/add-attendee", method = RequestMethod.POST)
	public String addAttendee(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName,
			@RequestParam("emailAddress") String emailAddress, Model model) throws Exception {

		Attendee attendee = new Attendee();
		attendee.setFirstName(firstName);
		attendee.setLastName(lastName);
		attendee.setEmailAddress(emailAddress);

		attendeeService.add(attendee);
		model.addAttribute("attendees", attendeeService.getAttendees());

		addAppEnv(model);
		return "services";
	}

	@RequestMapping("/bluegreen")
	public String bluegreen(Model model) throws Exception {

		for (String key : System.getenv().keySet()) {
			System.out.println(key + ":" + System.getenv(key));
		}

		addAppEnv(model);

		return "bluegreen";
	}


	private void addAppEnv(Model model) throws Exception {

		Map<String, Object> modelMap = environmentHelper.addAppEnv();
		model.addAllAttributes(modelMap);
	}

}