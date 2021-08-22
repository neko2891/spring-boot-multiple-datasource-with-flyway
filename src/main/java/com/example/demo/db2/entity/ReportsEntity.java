/* (C) 2021 */
package com.example.demo.db2.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "reports", schema = "db2", catalog = "")
public class ReportsEntity {

  @Id
  @Column(name = "id")
  private long id;

  @Column(name = "date")
  private String date;

  @Column(name = "count")
  private int count;
}
