
package com.heroku.demo;

import com.heroku.demo.message.Message;
import com.heroku.demo.message.MessageRepository;
import com.heroku.demo.message.MessageServiceImpl;
import com.heroku.demo.person.Person;
import com.heroku.demo.person.PersonRepository;

import javax.validation.Valid;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
public class HomeController {


    private PersonRepository personRepository;
    private MessageRepository messageRepository;
    MessageServiceImpl messageService;

    @Autowired
    public HomeController(PersonRepository repository, MessageRepository pRepository) {
        this.personRepository = repository;
        this.messageRepository = pRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String index(ModelMap model) {
        return "index";
    }

    @RequestMapping("/news")
    public String news(ModelMap model) {
       // List<Person> records = PersonServiceImpl.getByType(recordRepository, 0);//findByType(0); //ByType(0);
       // model.addAttribute("records", records);
        //model.addAttribute("insertRecord", new Person());
        return "news";
    }

    @RequestMapping("/addnews")
    public String insertData(ModelMap model,
                             @ModelAttribute("insertRecord") @Valid Person person,
                             BindingResult result) {
        if (!result.hasErrors()) {
            //person.setWhat(0);
            personRepository.save(person);
        }
        return news(model);
    }

    @RequestMapping("/deletenews")
    public String deleteData(ModelMap model, @ModelAttribute("id") String id,
                             BindingResult result) {
        personRepository.delete(Long.parseLong(id));
        return news(model);
    }

    @RequestMapping("/guides")
    public String guides(ModelMap model) {
       // List<Person> records = PersonServiceImpl.getByType(recordRepository, 1);//findByType(1);
       // model.addAttribute("records", records);
       // model.addAttribute("insertRecord", new Person());
        return "guides";
    }

    @RequestMapping("/addguide")
    public String insertGuide(ModelMap model,
                              @ModelAttribute("insertRecord") @Valid Person person,
                              BindingResult result) {

        if (!result.hasErrors()) {
            //person.setWhat(1);
            personRepository.save(person);
        }
        return guides(model);
    }

    @RequestMapping("/deleteguide")
    public String deleteGuide(ModelMap model, @ModelAttribute("id") String id,
                              BindingResult result) {
        personRepository.delete(Long.parseLong(id));
        //TODO: delete points
        return guides(model);
    }

    @RequestMapping("/points")
    public String points(ModelMap model) {
       // List<Person> records = PersonServiceImpl.getByType(recordRepository, 1);//findByType(1);
        List<Message> messages = messageRepository.findAll();
       // model.addAttribute("records", records);
        model.addAttribute("messages", messages);
        model.addAttribute("insertPoint", new Message());
        return "messages";
    }

    @RequestMapping("/addpoint")
    public String insertPoint(ModelMap model,
                              @ModelAttribute("insertPoint") @Valid Message message,
                              BindingResult result) {
        if (!result.hasErrors()) {
            messageRepository.save(message);
        }
        return points(model);
    }

    @RequestMapping("/deletepoint")
    public String deletePoint(ModelMap model, @ModelAttribute("id") String id,
                              BindingResult result) {
        messageRepository.delete(Long.parseLong(id));
        return points(model);
    }

    @RequestMapping("/contacts")
    public String contacts(ModelMap model) {
        //List<Person> records = PersonServiceImpl.getByType(recordRepository, 2);//findByType(1);
        //model.addAttribute("records", records);
        //model.addAttribute("insertRecord", new Person());
        return "contacts";
    }

    @RequestMapping("/addcontact")
    public String insertContact(ModelMap model,
                              @ModelAttribute("insertRecord") @Valid Person person,
                              BindingResult result) {

        if (!result.hasErrors()) {
           // person.setWhat(2);
            personRepository.save(person);
        }
        return contacts(model);
    }


    @RequestMapping("/deletecontact")
    public String deleteContact(ModelMap model, @ModelAttribute("id") String id,
                              BindingResult result) {
        personRepository.delete(Long.parseLong(id));
        return contacts(model);
    }


    @RequestMapping("/gallery")
    public String gallery(ModelMap model) {
       // List<Person> records = PersonServiceImpl.getByType(recordRepository, 3);//findByType(1);
       // model.addAttribute("records", records);
        //model.addAttribute("insertRecord", new Person());
        return "gallery";
    }

    @RequestMapping("/addgallery")
    public String insertGallery(ModelMap model,
                                @ModelAttribute("insertRecord") @Valid Person person,
                                BindingResult result) {

        if (!result.hasErrors()) {
            //person.setWhat(3);
            personRepository.save(person);
        }
        return gallery(model);
    }

    @RequestMapping("/deletegallery")
    public String deleteGallery(ModelMap model, @ModelAttribute("id") String id,
                                BindingResult result) {
        personRepository.delete(Long.parseLong(id));
        //TODO: delete photos
        return gallery(model);
    }

    @RequestMapping("/photos")
    public String photos(ModelMap model) {
      //  List<Person> records = PersonServiceImpl.getByType(recordRepository, 3);//findByType(1);
        //List<Person> photos = PersonServiceImpl.getByType(recordRepository, 4);
      //  model.addAttribute("records", records);
      //  model.addAttribute("photos", photos);
        //model.addAttribute("insertPhoto", new Person());
        return "photos";
    }

    @RequestMapping("/addphoto")
    public String insertPhotos(ModelMap model,
                                @ModelAttribute("insertPhoto") @Valid Person person,
                                BindingResult result) {

        if (!result.hasErrors()) {
            //person.setWhat(4);
            personRepository.save(person);
        }
        return photos(model);
    }

    @RequestMapping("/deletephoto")
    public String deletePhotos(ModelMap model, @ModelAttribute("id") String id,
                                BindingResult result) {
        personRepository.delete(Long.parseLong(id));
        return photos(model);
    }

    @RequestMapping("/getjson")
    @ResponseBody
    public String getPoints(ModelMap model, @ModelAttribute("type") int type,
            @ModelAttribute("locate") String locate, BindingResult result){
        ArrayList<String> arrayList = new ArrayList<>();
        switch (type){
            case 5:
                List<Message> messages = messageRepository.findAll();
                for (Message p: messages){
                    arrayList.add(p.toString());
                }
                break;
            default:
                //List<Person> records = PersonServiceImpl.getByTypeAndLocate(recordRepository, type, locate);
              //  for (Person r:records){
              //      arrayList.add(r.toString());
              //  }
        }

        StringBuilder stringBuilder = new StringBuilder("{ \"models\": [");

        for (int i = 0; i<arrayList.size(); i++) {
            stringBuilder.append(arrayList.get(i));
            if (arrayList.size()-i>1) stringBuilder.append(",\n");
        }
        stringBuilder.append("]}");
        return stringBuilder.toString();
    }

    @RequestMapping("/getmarshrut")
    @ResponseBody
    public String getMarshrut(ModelMap model, @ModelAttribute("type") String type,
                            @ModelAttribute("locate") String locate, BindingResult result){
        ArrayList<String> arrayList = new ArrayList<>();
       // List<Person> records = PersonServiceImpl.getMarshrutByLocate(recordRepository, type, locate);
       // for (Person r:records){
       //     arrayList.add(r.toString());
       // }

        StringBuilder stringBuilder = new StringBuilder("{ \"models\": [");

        for (int i = 0; i<arrayList.size(); i++) {
            stringBuilder.append(arrayList.get(i));
            if (arrayList.size()-i>1) stringBuilder.append(",\n");
        }
        stringBuilder.append("]}");
        return stringBuilder.toString();
    }

    @RequestMapping("/addmsg")
    @ResponseBody
    public String insertMsg(ModelMap model,
                               @ModelAttribute("data") String data,
                               @ModelAttribute("time") String time,
                               @ModelAttribute("event_id") int eventId,
                               @ModelAttribute("user_id") int userId,
                               BindingResult result) {
        Message msg = new Message(userId, eventId, data, time);
        if (!result.hasErrors()) {
            //person.setWhat(3);
            messageRepository.save(msg);
        }
        return msg.toString();
    }

    @RequestMapping("/addperson")
    @ResponseBody
    public String insertPerson(ModelMap model,
                               @ModelAttribute("pass") String pass,
                               @ModelAttribute("login") String login,
                               @ModelAttribute("type") int type,
                               @ModelAttribute("last_name") String lastName,
                               @ModelAttribute("first_name") String firstName,
                               @ModelAttribute("phone_number") String phoneNumber,
                               @ModelAttribute("rate") int rate,
                               @ModelAttribute("city") String city,
                               @ModelAttribute("email") String email,
                               @ModelAttribute("about") String about,
                                BindingResult result) {

        Person p = new Person(login, pass, lastName, type, firstName, email, phoneNumber, rate, about, city);
        if (!result.hasErrors()) {
            //person.setWhat(3);
            personRepository.save(p);
        }
        return p.toString();
    }


    @RequestMapping("/getpersons")
    @ResponseBody
    public String getPerson(ModelMap model, BindingResult result){
        ArrayList<String> arrayList = new ArrayList<>();
         List<Person> persons = personRepository.findAll();
        for (Person p:persons){
             arrayList.add(p.toString());
        }

        StringBuilder stringBuilder = new StringBuilder("{ \"persons\": [");

        for (int i = 0; i<arrayList.size(); i++) {
            stringBuilder.append(arrayList.get(i));
            if (arrayList.size()-i>1) stringBuilder.append(",\n");
        }
        stringBuilder.append("]}");
        return stringBuilder.toString();
    }


    @RequestMapping("/getmsgs")
    @ResponseBody
    public String getMsgs(ModelMap model, BindingResult result){
        ArrayList<String> arrayList = new ArrayList<>();
        List<Message> messages = messageRepository.findAll();
        for (Message m:messages){
            arrayList.add(m.toString());
        }

        StringBuilder stringBuilder = new StringBuilder("{ \"messages\": [");

        for (int i = 0; i<arrayList.size(); i++) {
            stringBuilder.append(arrayList.get(i));
            if (arrayList.size()-i>1) stringBuilder.append(",\n");
        }
        stringBuilder.append("]}");
        return stringBuilder.toString();
    }

}
