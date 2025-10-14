package com.team.incidentresponse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.team.incidentresponse.model.Incident;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long> {
    long countBySeverity(Incident.Severity severity);
    long countByStatus(Incident.Status status);
    long countByType(String type);
    long countByCreatedAtAfter(LocalDateTime dateTime);
    List<Incident> findTop10ByOrderByCreatedAtDesc();
    List<Incident> findByStatus(Incident.Status status);
    
    @Query("SELECT i.type, COUNT(i) FROM Incident i GROUP BY i.type")
    List<Object[]> countIncidentsByType();
    
    @Query("SELECT i FROM Incident i WHERE i.createdAt >= :startDate ORDER BY i.createdAt DESC")
    List<Incident> findRecentIncidents(LocalDateTime startDate);
    
    void deleteByType(String type);
}
