package com.online.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Entity
@Table(name = "Rating")
@IdClass(RatingKey.class)
@Data
public class Rating {

  @Id
  @Column
  private long studentId;

  @Id
  @Column
  private long courseId;

  @NotEmpty
  @Column
  private Double rating;

  @NotEmpty
  @Column
  private String review;
}
