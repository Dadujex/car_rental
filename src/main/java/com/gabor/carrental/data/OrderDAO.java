package com.gabor.carrental.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface OrderDAO extends JpaRepository<OrderEntity, Integer> {
    void deleteByCarId(int carId);

    @Query(value = """
            SELECT *
            FROM orders o
            WHERE o.car_id=:carId
            AND ((o.start_date<=:start AND o.end_date>=:start)
            OR (o.start_date<=:end AND o.end_date>=:end))
""", nativeQuery = true)
    List<OrderEntity> findByCarIdWithDates(@Param("carId") int carId, @Param("start") Date start,
                                           @Param("end") Date end);

    List<OrderEntity> findAllByOrderByIdAsc();
}
