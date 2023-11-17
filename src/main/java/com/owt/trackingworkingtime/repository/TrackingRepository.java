package com.owt.trackingworkingtime.repository;

import com.owt.trackingworkingtime.model.Tracking;
import com.owt.trackingworkingtime.model.TrackingId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TrackingRepository extends JpaRepository<Tracking, TrackingId> {

    @Query(value = "SELECT count(tag_id) > 0 FROM tracking " +
            "WHERE date_trunc('minute', tracking_time) = date_trunc('minute', Cast(:timeChecking as timestamp with time zone)) " +
            "AND tag_id = :tagId " +
            "LIMIT 1", nativeQuery = true)
    boolean existsTimeTrackingCustomQuery(@Param("tagId") String tagId, @Param("timeChecking") Date timeChecking);

    @Query(value = "SELECT * FROM tracking where tag_id = :tagId " +
            "AND date_trunc('day', (tracking_time at time zone :fromZone)) >= date_trunc('day', (:fromDate at time zone :fromZone)) " +
            "AND date_trunc('day', (tracking_time at time zone :toZone)) <= date_trunc('day', (:toDate at time zone :toZone)) " +
            "ORDER BY tracking_time",
            nativeQuery = true)
    List<Tracking> findByTagIdAndDate(@Param("tagId") String tagId, @Param("fromDate") Date fromDate, @Param("fromZone") String fromZone, @Param("toDate") Date toDate, @Param("toZone") String toZone);

    @Query(value = "SELECT * FROM tracking where tag_id = :tagId " +
            "AND date_trunc('hour', tracking_time) >= date_trunc('hour', Cast(:fromDate as timestamp with time zone)) " +
            "AND date_trunc('hour', tracking_time) <= date_trunc('hour', Cast(:toDate as timestamp with time zone)) " +
            "ORDER BY tracking_time",
            nativeQuery = true)
    List<Tracking> findByTagIdAndDate(@Param("tagId") String tagId, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

    @Query(value = "SELECT * FROM tracking where tag_id = :tagId " +
            "AND date_trunc('day', tracking_time) = date_trunc('day', Cast(:date as timestamp with time zone)) " +
            "ORDER BY tracking_time",
            nativeQuery = true)
    List<Tracking> findByTagIdAndDate(@Param("tagId") String tagId, @Param("date") Date date);

    @Query(value = "SELECT tag_Id FROM tracking " +
            "WHERE date_trunc('day', tracking_time) = date_trunc('day', Cast(:date as timestamp with time zone)) " +
            "ORDER BY tracking_time",
            nativeQuery = true)
    List<String> findTagIdByDate(@Param("date") Date date);

    @Modifying
    @Query(value = "DELETE FROM tracking " +
            "WHERE date_trunc('day', tracking_time) = date_trunc('day', Cast(:date as timestamp with time zone)) ",
            nativeQuery = true)
    void deleteByDate(@Param("date") Date date);
}
