package com.team.incidentresponse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.team.incidentresponse.model.Incident;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long> {
    Incident findById(long id);
    void deleteById(long id);
    
}
