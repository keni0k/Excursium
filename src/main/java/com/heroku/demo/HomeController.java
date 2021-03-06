package com.heroku.demo;

import com.heroku.demo.event.EventRepository;
import com.heroku.demo.message.Message;
import com.heroku.demo.message.MessageRepository;
import com.heroku.demo.order.OrderRepository;
import com.heroku.demo.person.PersonRepository;
import com.heroku.demo.person.PersonServiceImpl;
import com.heroku.demo.photo.Photo;
import com.heroku.demo.photo.PhotoRepository;
import com.heroku.demo.review.ReviewRepository;
import com.heroku.demo.token.TokenRepository;
import com.heroku.demo.utils.Consts;
import com.heroku.demo.utils.Utils;
import com.heroku.demo.utils.UtilsForWeb;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.joda.time.LocalTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static com.heroku.demo.utils.Utils.*;

@Controller
@RequestMapping("/")
public class HomeController {

    private MessageRepository messageRepository;
    private ReviewRepository reviewRepository;
    private OrderRepository orderRepository;
    private PhotoRepository photoRepository;
    private TokenRepository tokenRepository;

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    private Utils utils;

    @Autowired
    public HomeController(MessageRepository messageRepository, ReviewRepository reviewRepository,
                          OrderRepository orderRepository, PhotoRepository photoRepository,
                          EventRepository eventRepository,
                          TokenRepository tokenRepository, PersonRepository personRepository) {

        this.orderRepository = orderRepository;
        this.messageRepository = messageRepository;
        this.reviewRepository = reviewRepository;
        this.photoRepository = photoRepository;
        this.tokenRepository = tokenRepository;
        utils = new Utils(new PersonServiceImpl(personRepository, eventRepository, reviewRepository, photoRepository));

    }

    @RequestMapping("error/403")
    public String access403(ModelMap modelMap, Principal principal) {
        modelMap.addAttribute("person", utils.getPerson(principal));
        modelMap.addAttribute("utils", new UtilsForWeb());
        return "error/403";
    }

    @RequestMapping("error/404")
    public String notFoundMethod(ModelMap modelMap, Principal principal) {
        modelMap.addAttribute("person", utils.getPerson(principal));
        modelMap.addAttribute("utils", new UtilsForWeb());
        return "error/404";
    }

    @RequestMapping("error/500")
    public String internalServerError(ModelMap modelMap, Principal principal) {
        modelMap.addAttribute("person", utils.getPerson(principal));
        modelMap.addAttribute("utils", new UtilsForWeb());
        return "error/500";
    }

    @RequestMapping("upload")
    public String upload(ModelMap modelMap, Principal principal) {
        modelMap.addAttribute("person", utils.getPerson(principal));
        modelMap.addAttribute("utils", new UtilsForWeb());
        return "upload";
    }

    private File streamToFile(String fileExtension, InputStream in) throws IOException {
        File tempFile = File.createTempFile(System.getProperty("catalina.home") + File.separator + "tmpFiles"+randomToken(10), fileExtension);
        tempFile.deleteOnExit();
        FileOutputStream out = new FileOutputStream(tempFile);
        IOUtils.copy(in, out);
        return tempFile;
    }

    @RequestMapping(value = "/upload_images", method = RequestMethod.POST)
    public @ResponseBody
    String uploadMultipleFileHandler(@RequestParam("img") MultipartFile[] files) {

        StringBuilder message = new StringBuilder();
        for (MultipartFile file : files) {
            try {
                String fileName = file.getOriginalFilename();
                String photoToken = randomToken(32);
                InputStream input = file.getInputStream();
                File serverFile = streamToFile(getFileExtension(fileName), input);

                if (getFileSizeMegaBytes(serverFile) > 1)
                    serverFile = compress(serverFile, getFileExtension(fileName), getFileSizeMegaBytes(serverFile));

                photoToken+=".jpg";
                Photo photo = new Photo(0, photoToken, 0);

                putImg(serverFile.getAbsolutePath(), photoToken);
                photoRepository.save(photo);
                message.append(Consts.URL_PATH).append(photoToken);
            } catch (Exception e) {
                message.append("<p>").append(e.getMessage()).append("</p>");
            }
        }
        return message.toString();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String index2(ModelMap modelMap, Principal principal) {
        modelMap.addAttribute("person", utils.getPerson(principal));
        return "redirect:/events/list";
    }

    @RequestMapping({"/", "index"})
    public String index(ModelMap modelMap, Principal principal) {
        modelMap.addAttribute("person", utils.getPerson(principal));
        return "redirect:/events/list";
    }

    @RequestMapping("index_test")
    public String indexTest(ModelMap modelMap, Principal principal) {
        modelMap.addAttribute("person", utils.getPerson(principal));
        modelMap.addAttribute("utils", new UtilsForWeb());
        return "index";
    }

    @RequestMapping("addmsg")
    @ResponseBody
    public String insertMsg(ModelMap model,
                            @ModelAttribute("msg") String message,
                            @ModelAttribute("event_id") int eventId,
                            @ModelAttribute("user_id") int userId,
                            BindingResult result) {
        Message msg = new Message(userId, eventId, new LocalTime().toDateTimeToday().toString(), message);
        if (!result.hasErrors()) {
            //person.setWhat(3);
            messageRepository.save(msg);
        }
        return msg.toString();
    }


    @RequestMapping("/getmsgs")
    @ResponseBody
    public String getMsgs() {
        ArrayList<String> arrayList = new ArrayList<>();
        List<Message> messages = messageRepository.findAll();
        for (Message m : messages) {
            arrayList.add(m.toString());
        }

        StringBuilder stringBuilder = new StringBuilder("{ \"messages\": [");

        for (int i = 0; i < arrayList.size(); i++) {
            stringBuilder.append(arrayList.get(i));
            if (arrayList.size() - i > 1) stringBuilder.append(",\n");
        }
        stringBuilder.append("]}");
        return stringBuilder.toString();
    }

}
