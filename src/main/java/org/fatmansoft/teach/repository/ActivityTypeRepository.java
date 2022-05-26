package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.ActivityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ActivityTypeRepository extends JpaRepository<ActivityType, Integer> {
    @Query(value = "select id from activity_type where activity_type = ?1",nativeQuery = true)
    Integer AcId(String x);
}
