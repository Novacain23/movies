package com.example.scheduleservice.scheduleservice.repository;


import com.example.scheduleservice.scheduleservice.model.ScheduleInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<ScheduleInfo, Integer> {

    //@Query("update ScheduleInfo s set s.isSent = true where s.id = :id")
    @Modifying
    @Query("Update ScheduleInfo s SET s.isSent=1 WHERE s.id=:id")
    public void setToSent(@Param("id") int id);

}
