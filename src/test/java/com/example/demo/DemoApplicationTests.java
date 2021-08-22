/* (C) 2021 */
package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.demo.db1.entity.UsersEntity;
import com.example.demo.db1.repo.UsersRepo;
import com.example.demo.db2.entity.ReportsEntity;
import com.example.demo.db2.repo.ReportsRepo;
import com.example.demo.util.LogUtils;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@EnableEncryptableProperties
class DemoApplicationTests {

  @Autowired private UsersRepo usersRepo;

  @Autowired private ReportsRepo reportsRepo;

  @Test
  public void test() {
    Logger log = LogUtils.getExceptionLogger();
    Logger log1 = LogUtils.getBusinessLogger();
    Logger log2 = LogUtils.getDBLogger();
    Logger log3 = LogUtils.getPlatformLogger();

    log.error("Exception Logger");
    log1.info("Business Logger");
    log2.info("DB Logger");
    log3.info("Platform Logger");

    UsersEntity user = new UsersEntity();
    user.setId(2);
    user.setName("Steven");
    usersRepo.save(user);

    ReportsEntity report = new ReportsEntity();
    report.setId(2);
    report.setDate("2020-08-23");
    report.setCount(10);
    reportsRepo.save(report);

    Optional<UsersEntity> o1 = usersRepo.findById(user.getId());
    assertEquals(o1.isPresent(), true);

    Optional<ReportsEntity> o2 = reportsRepo.findById(report.getId());
    assertEquals(o2.isPresent(), true);

    usersRepo.deleteById(user.getId());
    reportsRepo.deleteById(report.getId());
  }
}
