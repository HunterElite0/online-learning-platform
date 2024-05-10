package com.online.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Entity
@Table(name = "Enrollment")
@IdClass(EnrollmentKey.class)
@Data
public class Enrollment {

  @Id
  @Column
  private long studentId;

  @Id
  @Column
  private long courseId;

  @NotEmpty
  @Column
  private String status;
}
