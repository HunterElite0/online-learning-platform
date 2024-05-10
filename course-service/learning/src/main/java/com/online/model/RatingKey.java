package com.online.model;

import java.io.Serializable;
import java.util.Objects;

import lombok.Data;

@Data
public class RatingKey implements Serializable {
  
  private Long studentId;
  private Long courseId;

  public RatingKey() {
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    RatingKey ratingKey = (RatingKey) o;
    return (studentId == ratingKey.studentId) &&
        (courseId == ratingKey.courseId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(studentId, courseId);
  }

}
