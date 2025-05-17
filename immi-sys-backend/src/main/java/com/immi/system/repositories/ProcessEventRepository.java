package com.immi.system.repositories;

import com.immi.system.models.ApplicationEventModel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessEventRepository extends JpaRepository<ApplicationEventModel, Long> {

    public List<ApplicationEventModel> findBySourceTypeAndProcessId(String sourceType, Long processId);

    public boolean existsBySourceTypeAndProcessId(String sourceType, Long id);

    public void deleteBySourceTypeAndProcessId(String sourceType, Long id);
    
}
