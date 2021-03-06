package com.heroku.demo.event;

import com.heroku.demo.photo.Photo;
import com.heroku.demo.photo.PhotoServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.heroku.demo.utils.Consts.EXCURSION_ACTIVE;
import static com.heroku.demo.utils.Consts.EXCURSION_DELETED;

public class EventServiceImpl implements EventService {

    private EventRepository eventRepository;
    private PhotoServiceImpl photoService;
    private static final Logger logger = LoggerFactory.getLogger(EventServiceImpl.class);

    public PhotoServiceImpl getPhotoService(){
        return photoService;
    }

    @Override
    public Event addEvent(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public void delete(long id) {
        eventRepository.delete(id);
    }

    public EventServiceImpl(EventRepository eventRepository, PhotoServiceImpl photoService) {
        this.eventRepository = eventRepository;
        this.photoService = photoService;
    }

    @Override
    public Event getById(long id) {
        Event e = eventRepository.findOne(id);
        List<Photo> photos = photoService.getByEventId(e.getId());
        if (photos != null)
            for (Photo p:photos) {
                e.pathToPhoto.add(p.getData());
            }
        return e;
    }

    @Override
    public Event editEvent(Event event) {
        return eventRepository.saveAndFlush(event);
    }

    @Override
    public List<Event> getAll() {
        return eventRepository.findAll();
    }

    @Override
    public ListEvents getByFilter(Integer priceUp, Integer priceDown, Integer category, Integer language, Integer country, Integer city, String words, boolean isAll) {
        boolean isAllLang = false;
        if (country==null) country = -1;
        if (city==null) city = -1;
        if (words==null) words = "";
        if (priceDown==null) priceDown = -1;
        if (priceUp==null) priceUp = -1;
        if (category==null) category = -1;
        if (language==null) language = -1;
        if (priceUp == -1) priceUp = Integer.MAX_VALUE;
        if (language == -1) isAllLang = true;
        String[] wds = words.split(",");
        long curr1 = System.currentTimeMillis();
        List<Event> list = getAll();//getEventRepository().getByFilter(priceUp, priceDown,category,language,isAll);
        long curr2 = System.currentTimeMillis();
        ListEvents copy = new ListEvents();
        for (Event aList : list) {
            if ((aList.getCountry()==country || country==-1) && (aList.getCity()==city || city==-1))
            if (((aList.getCategory() == category) || (category == -1)) && ((aList.getLanguage() == language) || isAllLang) && (aList.getType() == EXCURSION_ACTIVE || isAll)) {
                if ((aList.getPrice() >= priceDown) && (aList.getPrice() <= priceUp)) {

                    List<Photo> photos = photoService.getByEventId(aList.getId());
                    if (photos != null)
                        for (Photo p:photos) {
                            aList.pathToPhoto.add(p.getData());
                        }

                    if (!words.equals("")) {
                        for (String word : wds) {
                            if (aList.getName().toLowerCase().contains(word.toLowerCase()))
                                aList.cnt += 7;
                            if (aList.getDescription().toLowerCase().contains(word.toLowerCase()))
                                aList.cnt += 3;
                        }
                        if (aList.cnt > 0) copy.add(aList);
                    } else
                        copy.add(aList);
                }
                if (aList.getPrice()>copy.getMaxPrice())
                    copy.setMaxPrice(aList.getPrice());
                if (aList.getPrice()<copy.getMinPrice())
                    copy.setMinPrice(aList.getPrice());
            }
        }
        long curr3 = System.currentTimeMillis();
        if (!words.equals("")) copy.sort((o1, o2) -> Integer.compare(o2.cnt, o1.cnt));
        logger.info("TIME11:"+(curr2-curr1));
        logger.info("TIME22:"+(curr3-curr2));
        return copy;
    }

    @Override
    public List<Event> getByGuideId(long guideId) {
        List<Event> eventList = new ArrayList<>();
        List<Event> copy = getAll();
        for (Event event:copy) {
            if (event.getGuideId()==guideId && event.getType()!=EXCURSION_DELETED) {
                List<Photo> photos = photoService.getByEventId(event.getId());
                if (photos != null)
                    for (Photo p:photos) {
                        event.pathToPhoto.add(p.getData());
                    }
                eventList.add(event);
            }
        }
        return eventList;
    }

    public int getCountInCity(int country, int city, int language){
        List<Event> list = getAll();
        int k = 0;
        for (Event e:list) {
            if (e.getCountry()==country && e.getCity()==city && e.getLanguage()==language && e.getType()==EXCURSION_ACTIVE)
                k++;
        }
        return k;
    }

    public int getCountInCountry(int country, int language){
        List<Event> list = getAll();
        int k = 0;
        for (Event e:list) {
            if (e.getCountry()==country && e.getLanguage()==language && e.getType()== EXCURSION_ACTIVE)
                k++;
        }
        return k;
    }

    @Override
    public void setRate(int rate, long eventId) {
        Event e = getById(eventId);
        if (e!=null) {
            e.setRate((e.getRate() * e.getReviewsCount()) / (e.getReviewsCount() + 1));
            e.setReviewsCount(e.getReviewsCount() + 1);
            editEvent(e);
        }
    }

    ListEvents getByFilter(Integer priceUp, Integer priceDown, Integer category, Integer language, Integer country, Integer city, String words, Integer sortBy, boolean isAll) {
        ListEvents events = getByFilter(priceUp, priceDown, category, language, country, city, words, isAll);
        if (sortBy==null) return events;
        events.sort((o1, o2) -> {
            switch (sortBy) {
                case 0: return Long.compare(o1.getId(), o2.getId());
                case 1: return Long.compare(o2.getId(), o1.getId());
                case 2: return Integer.compare(o1.getPrice(), o2.getPrice());
                case 3: return Integer.compare(o2.getPrice(), o1.getPrice());
                case 4: return Float.compare(o1.getRate(), o2.getRate());
                case 5: return Float.compare(o2.getRate(), o1.getRate());
                case 6: return Integer.compare(o1.getType(), o2.getType());
                case 7: return Integer.compare(o2.getType(), o1.getType());
                case 8: return Integer.compare(o1.getDuration(), o2.getDuration());
                case 9: return Integer.compare(o2.getDuration(), o1.getDuration());
                case 10: return Integer.compare(o1.getUsersCount(), o2.getUsersCount());
                case 11: return Integer.compare(o2.getUsersCount(), o1.getUsersCount());
            }
            return Long.compare(o1.getId(), o2.getId());
        });
        return events;
    }
}
