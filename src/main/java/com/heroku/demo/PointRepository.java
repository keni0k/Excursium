package com.heroku.demo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointRepository extends JpaRepository<Point, Long> {
//    @Query("select * from news where news.type = :type")
//    List<Record> findByType(@Param("type") int type);

}