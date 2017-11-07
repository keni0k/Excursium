package com.heroku.demo.person;

import com.heroku.demo.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.heroku.demo.utils.Utils.randomToken;

@Controller
@RequestMapping("/users")
public class PersonController {

    private static String AUTH_KEY = "DGgttMjxGUuuLvr49LnEWVFBbkxSNXnH";

    private PersonServiceImpl personService;

    private final MessageSource messageSource;

    @Autowired
    public PersonController(PersonRepository personRepository, MessageSource messageSource) {
        personService = new PersonServiceImpl(personRepository);
        this.messageSource = messageSource;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String persons(ModelMap model) {
        model.addAttribute("insertPerson", new Person());
        return "registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String signUp(@ModelAttribute("insertPerson") @Valid Person person,
                         BindingResult result,
                         @RequestParam("file") MultipartFile file,
                         @ModelAttribute("pass2") String pass2,
                         ModelMap model, Locale locale) {
        person.setToken(randomToken(32));

        if (!personService.throwsErrors(person, pass2) || result.hasErrors()) {
            model.addAttribute("error_login", !personService.isLoginFree(person.getLogin()));
            model.addAttribute("error_phone", !personService.isPhoneFree(person.getPhoneNumber()));
            model.addAttribute("error_pass", !person.getPass().equals(pass2));
            model.addAttribute("error_email_free", !personService.isEmailFree(person.getEmail()));
            model.addAttribute("error_email_valid", !personService.isEmailCorrect(person.getEmail()));
            model.addAttribute("insertPerson", person);
            model.addAttribute("message", new MessageUtil("danger", messageSource.getMessage("error.user.add", null, locale)));
            return "registration";
        }
        person.setEmail(person.getEmail().toLowerCase());
        person.setLogin(person.getLogin().toLowerCase());
        person.setImageUrl(file.getOriginalFilename());
        person.setRole("ROLE_USER");
        personService.addPerson(person);
        model.addAttribute("message", new MessageUtil("success", messageSource.getMessage("success.user.registration", null, locale)));
        return persons(model);
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String signIn() {
        return "login";
    }

    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET, value = "/getbytoken")
    @ResponseBody
    public String getPerson(@ModelAttribute("token") String token, @ModelAttribute("auth") String authKey) {
        Person p = null;
        if (authKey.equals(AUTH_KEY))
            p = personService.getByToken(token);
        return p == null ? "{}" : p.toString();
    }

    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET, value = "/getbyemail")
    @ResponseBody
    public String getPersonByEmail(@ModelAttribute("email") String email, @ModelAttribute("auth") String authKey) {
        Person p = null;
        if (authKey.equals(AUTH_KEY))
            p = personService.getByEmail(email);
        return p == null ? "{}" : p.toString();
    }

    @RequestMapping("/updatedb")
    @ResponseBody
    public String updateDBPersons() {
        List<Person> persons = personService.getAll();
        for (Person person : persons) {
//                person.setRole("ROLE_USER");
//                personService.editPerson(person);
        }
        return "YES";
    }

    @RequestMapping("/moderation")
    public String persons_last(ModelMap model,
                               @RequestParam(value = "id", required = false) Long id) {
        List<Person> persons = personService.getAll();
        model.addAttribute("persons", persons);
        if (id != null) {
            Person editPerson = personService.getById(id);
            model.addAttribute("insertPerson", editPerson);
        } else model.addAttribute("insertPerson", new Person());
        return "persons";
    }

    @RequestMapping(value = "/moderation", method = RequestMethod.POST)
    public String signUpModer(@ModelAttribute("insertPerson") @Valid Person person,
                              BindingResult result,
                              @RequestParam("file") MultipartFile file,
                              @ModelAttribute("pass2") String pass2,
                              ModelMap model, Locale locale) {
        person.setToken(randomToken(32));

        if (!personService.throwsErrors(person, pass2) || result.hasErrors()) {
            model.addAttribute("error_login", !personService.isLoginFree(person.getLogin()));
            model.addAttribute("error_phone", !personService.isPhoneFree(person.getPhoneNumber()));
            model.addAttribute("error_pass", !person.getPass().equals(pass2));
            model.addAttribute("error_email_free", !personService.isEmailFree(person.getEmail()));
            model.addAttribute("error_email_valid", !personService.isEmailCorrect(person.getEmail()));
            model.addAttribute("insertPerson", person);
            model.addAttribute("message", new MessageUtil("danger", messageSource.getMessage("error.user.add", null, locale)));
            return persons_last(model, null);
        }
        person.setEmail(person.getEmail().toLowerCase());
        person.setLogin(person.getLogin().toLowerCase());
        person.setImageUrl(file.getOriginalFilename());
        person.setRole(person.getRole().equals("") ? "ROLE_USER" : person.getRole());
        personService.editPerson(person);
        model.addAttribute("message", new MessageUtil("success", messageSource.getMessage("success.user.registration", null, locale)));
        return persons_last(model, null);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String deleteContact(@ModelAttribute("id") String id) {
        personService.delete(Long.parseLong(id));
        return persons_last(new ModelMap(), null);
    }

  /*
    @RequestMapping("/addperson")
    @ResponseBody
    public String insertPerson(@ModelAttribute("pass") String pass,
                               @ModelAttribute("login") String login,
        =                        @ModelAttribute("type") int type,
                               @ModelAttribute("last_name") String lastName,
                               @ModelAttribute("first_name") String firstName,
                               @ModelAttribute("phone_number") String phoneNumber,
                               @ModelAttribute("rate") int rate,
                               @ModelAttribute("city") String city,
                               @ModelAttribute("email") String email,
                               @ModelAttribute("about") String about,
                               @ModelAttribute("date") String date,
                               @ModelAttribute("image_url") String imageUrl,
                               BindingResult result) {

        Person p = new Person(login, pass, lastName, type, email, firstName, rate, phoneNumber, about, city, date, imageUrl);
        if (!result.hasErrors()) {
            //person.setWhat(3);
            personService.addPerson(p);
        }
        return p.toString();
    }*/

    @RequestMapping("/listjson")
    @ResponseBody
    public String getPersons(@ModelAttribute("auth") String authKey) {
        ArrayList<String> arrayList = new ArrayList<>();

        List<Person> persons = new ArrayList<>();
        if (authKey.equals(AUTH_KEY))
            persons = personService.getAll();


        StringBuilder stringBuilder = new StringBuilder("{ \"persons\": [");

        for (Person p : persons) {
            arrayList.add(p.toString());
        }

        for (int i = 0; i < arrayList.size(); i++) {
            stringBuilder.append(arrayList.get(i));
            if (arrayList.size() - i > 1) stringBuilder.append(",\n");
        }
        stringBuilder.append("]}");
        return stringBuilder.toString();
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public String profile(ModelMap model, @RequestParam("id") long id) {
        Person p = personService.getById(id);
        model.addAttribute("person", p != null ? p : new Person());
        //model.addAttribute("insertEvent", new Event());
        return "profile";
    }

}
