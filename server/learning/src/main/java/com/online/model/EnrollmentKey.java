package com.online.model;

import java.io.Serializable;
import java.util.Objects;

import lombok.Data;

@Data
public class EnrollmentKey implements Serializable {

  private long studentId;
  private long courseId;

  public EnrollmentKey() {
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    EnrollmentKey enrollmentKey = (EnrollmentKey) o;
    return (studentId == enrollmentKey.studentId) &&
        (courseId == enrollmentKey.courseId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(studentId, courseId);
  }
}
