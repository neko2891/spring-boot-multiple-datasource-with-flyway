/* (C) 2021 */
package com.example.demo.db1.repo;

import com.example.demo.db1.entity.UsersEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepo extends CrudRepository<UsersEntity, Long> {}
