package com.orbidroid.orbidroid_backend.repository;

import com.orbidroid.orbidroid_backend.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, String> {
    public static final String GET_SCHEDULE_CONTAINS = "SELECT * FROM schedules where schedule_work_start<=?1 and schedule_work_end>=?1";
    public static final String DELETE_SCHEDULE_WITH_DOCNUM_IN_BETWEEN =
            "DELETE FROM schedules where schedule_doc_num=:docNum and schedule_work_start>=:dateStart and schedule_work_end<=:dateEnd";

    @Query(value = GET_SCHEDULE_CONTAINS, nativeQuery = true)
    List<Schedule> getScheduleContains(String start, String end);

    @Transactional
    @Modifying
    @Query(value = DELETE_SCHEDULE_WITH_DOCNUM_IN_BETWEEN, nativeQuery = true)
    void deleteScheduleWithDocNumAndRange(
            @Param("docNum") Integer docNum,
            @Param("dateStart") String dateStart,
            @Param("dateEnd") String dateEnd
    );

    String deleteByDocNum(Integer docNum);

    String deleteById(Integer id);

    Optional<Schedule> getById(Integer id);

    List<Schedule> getByDocNum(Integer docNum);
}
