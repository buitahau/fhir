package com.owt.trackingworkingtime.repository;

import com.owt.trackingworkingtime.model.TrackingCombination;
import com.owt.trackingworkingtime.model.TrackingCombinationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TrackingCombinationRepository extends JpaRepository<TrackingCombination, TrackingCombinationId> {

    @Query(value = "SELECT * FROM tracking_combination where tag_id = :tagId " +
            "AND date_trunc('hour', check_in) >= date_trunc('hour', (:trackingDate::timestamp with time zone)) " +
            "AND date_trunc('hour', check_out) <= date_trunc('hour', (:trackingDate::timestamp with time zone)) " +
            "ORDER BY check_in", nativeQuery = true)
    List<TrackingCombination> findByTagIdAndDate(String tagId, Date trackingDate);
}
