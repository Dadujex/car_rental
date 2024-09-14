package com.gabor.carrental.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface CarDAO extends JpaRepository<CarEntity, Integer> {

    @Query(value = """
            SELECT * FROM (SELECT *
            FROM cars
            WHERE active
            EXCEPT
            SELECT DISTINCT c.*
            FROM cars c JOIN orders
            ON c.id = car_id
            WHERE (:start <= start_date AND :end >= start_date) OR
            (:start >= start_date AND :end <= end_date) OR
            (:start <= end_date AND :end >= end_date)) as "c*coc.*"
            ORDER BY ID
""", nativeQuery = true)
    List<CarEntity> findBetweenDates(@Param("start") Date start, @Param("end") Date end);

    List<CarEntity> findAllByOrderByIdAsc();
}
