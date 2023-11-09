package com.owt.trackingworkingtime.repository;

import com.owt.trackingworkingtime.model.Tracking;
import com.owt.trackingworkingtime.model.TrackingId;
import org.springframework.data.jpa.repository.JpaRepository;
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
            "AND date_trunc('day', (tracking_time at time zone :zone)) = date_trunc('day', (:date at time zone :zone))",
            nativeQuery = true)
    List<Tracking> findByTagIdAndDate(@Param("tagId") String tagId, @Param("date") Date date, @Param("zone") String zone);
}
