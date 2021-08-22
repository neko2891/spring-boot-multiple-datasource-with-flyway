/* (C) 2021 */
package com.example.demo.db2.repo;

import com.example.demo.db2.entity.ReportsEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportsRepo extends CrudRepository<ReportsEntity, Long> {
  ReportsEntity findFirstByOrderById();
}
