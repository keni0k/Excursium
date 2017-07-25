package com.heroku.demo.point;

/**
 * Created by Keni0k on 25.07.2017.
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PointServiceImpl implements PointService {

    public PointRepository getPointRepository() {
        return pointRepository;
    }

    @Autowired
    private PointRepository pointRepository;

    @Override
    public Point addPoint(Point point) {

        return pointRepository.saveAndFlush(point);
    }

    @Override
    public void delete(long id) {
        pointRepository.delete(id);
    }

    public PointServiceImpl() {
    }

    @Override
    public List<Point> getByGuide(int guide) {
        List<Point> list = pointRepository.findAll();
        for (int i = 0; i<list.size(); i++) {
            if (list.get(i).getId_of_guide()!=guide)list.remove(list.get(i));
        }
        return list;
    }

    @Override
    public Point editPoint(Point point) {
        return pointRepository.saveAndFlush(point);
    }

    @Override
    public List<Point> getAll() {
        return pointRepository.findAll();
    }
}
