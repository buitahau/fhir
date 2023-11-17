package com.owt.trackingworkingtime.repository;

import com.owt.trackingworkingtime.model.TrackingCombination;
import com.owt.trackingworkingtime.model.TrackingCombinationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TrackingCombinationRepository extends JpaRepository<TrackingCombination, TrackingCombinationId> {

    @Query(value = "SELECT * FROM tracking_combination WHERE tag_id = :tagId " +
            "AND date_trunc('minute', check_in) >= date_trunc('minute', Cast(:checkIn as timestamp with time zone)) " +
            "AND date_trunc('minute', check_out) <= date_trunc('minute', Cast(:checkOut as timestamp with time zone)) " +
            "ORDER BY check_in", nativeQuery = true)
    List<TrackingCombination> findByTagIdAndDate(@Param("tagId") String tagId, @Param("checkIn") Date checkIn, @Param("checkOut") Date checkOut);

    @Query(value = "SELECT * FROM tracking_combination WHERE tag_id = :tagId " +
            "AND date_trunc('day', check_in) >= date_trunc('day', Cast(:date as timestamp with time zone)) " +
            "ORDER BY check_in", nativeQuery = true)
    List<TrackingCombination> findByTagIdAndDate(@Param("tagId") String tagId, @Param("date") Date date);
}
