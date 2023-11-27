package com.owt.trackingworkingtime.repository;

import com.owt.trackingworkingtime.model.TrackingCombination;
import com.owt.trackingworkingtime.model.TrackingCombinationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface TrackingCombinationRepository extends JpaRepository<TrackingCombination, TrackingCombinationId> {

    @Query(value = "SELECT t FROM TrackingCombination t WHERE tagId = :tagId " +
            "AND extract( day from t.checkIn ) = extract( day from cast(:date as timestamp) ) " +
            "ORDER BY checkIn")
    List<TrackingCombination> findByTagIdAndDate(@Param("tagId") String tagId, @Param("date") Date date);

    @Query(value = "SELECT t FROM TrackingCombination t WHERE tagId IN (:tagIds) " +
            "AND function('date_trunc', 'minute', t.checkIn) >= :checkIn " +
            "AND function('date_trunc', 'minute', t.checkOut) <= :checkOut " +
            "ORDER BY tagId,checkIn")
    List<TrackingCombination> findByTagIdsAndDate(@Param("tagIds") List<String> tagIds,
                                                  @Param("checkIn") Date checkIn, @Param("checkOut") Date checkOut);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM TrackingCombination t WHERE tagId = :tagId " +
            "AND extract( day from t.checkIn ) = extract( day from cast(:date as timestamp) ) ")
    void deleteByTagIdDate(@Param("tagId") String tagId, @Param("date") Date date);
}
