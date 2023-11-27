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

    @Query(value = "SELECT t FROM Tracking t WHERE tagId = :tagId " +
            "AND extract( day from t.trackingTime ) >= extract( day from cast(:fromDate as timestamp) ) " +
            "AND extract( day from t.trackingTime ) <= extract( day from cast(:toDate as timestamp) ) " +
            "ORDER BY t.trackingTime")
    List<Tracking> findByTagIdAndDate(@Param("tagId") String tagId, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

    @Query(value = "SELECT t FROM Tracking t WHERE tagId = :tagId " +
            "AND extract( day from t.trackingTime ) = extract( day from cast(:date as timestamp) ) " +
            "ORDER BY t.trackingTime")
    List<Tracking> findByTagIdAndDate(@Param("tagId") String tagId, @Param("date") Date date);

    @Query(value = "SELECT DISTINCT (t.tagId) FROM Tracking t " +
            "WHERE extract( day from t.trackingTime ) = extract( day from cast(:date as timestamp) ) " +
            "ORDER BY t.tagId")
    List<String> findTagIdByDate(@Param("date") Date date);

    @Query(value = "SELECT DISTINCT (t.tagId) FROM Tracking t " +
            "WHERE function('date_trunc', 'minute', t.trackingTime) = :date " +
            "ORDER BY t.tagId")
    List<String> findTagIdByDatetime(@Param("date") Date date);

    @Modifying
    @Query(value = "DELETE FROM Tracking t " +
            "WHERE extract( day from t.trackingTime ) = extract( day from cast(:date as timestamp) ) ")
    void deleteByDate(@Param("date") Date date);
}
