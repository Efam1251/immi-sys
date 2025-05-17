package com.immi.system.repositories;

import com.immi.system.models.UscisProcessEventModel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UscisProcessEventRepository extends JpaRepository<UscisProcessEventModel, Long> {

    public List<UscisProcessEventModel> findAllByProcessId(Long processId);

}
