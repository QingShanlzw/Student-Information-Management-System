package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Score;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AchievementRepository extends JpaRepository<Score, Integer> {
}
