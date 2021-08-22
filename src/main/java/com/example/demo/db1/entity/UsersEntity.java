/* (C) 2021 */
package com.example.demo.db1.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "users", schema = "db1", catalog = "")
public class UsersEntity {
  @Id
  @Column(name = "id")
  private long id;

  @Column(name = "name")
  private String name;
}
