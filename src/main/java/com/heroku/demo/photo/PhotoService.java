package com.heroku.demo.photo;

import java.util.List;

public interface PhotoService {
    Photo addPhoto(Photo photo);

    void delete(long id);

    List<Photo> getByType(int type);

    Photo editPhoto(Photo photo);

    List<Photo> getAll();
}
