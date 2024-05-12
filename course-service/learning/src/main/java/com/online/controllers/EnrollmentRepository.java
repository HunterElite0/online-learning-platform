package com.online.controllers;

import java.util.List;

import com.online.model.Enrollment;

import jakarta.ejb.Stateful;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Stateful
public class EnrollmentRepository {

  @PersistenceContext(unitName = "AppDB")
  private EntityManager em;

  public String makeEnrollment(Enrollment enrollment) {
    try {
      Enrollment obj = new Enrollment();
      obj.setCourseId(enrollment.getCourseId()); // validation that course with this id exists
      obj.setStudentId(enrollment.getStudentId()); // From jwt token
      obj.setStatus("PENDING");
      em.persist(obj);
      return "Enrollment request sent!";
    } catch (Exception e) {
      e.printStackTrace();
      return "Enrollment request failed!";
    }
  }

  public String acceptEnrollment(long id) {
    Enrollment enrollment = em.find(Enrollment.class, id);
    if (enrollment != null) {
      try {
        enrollment.setStatus("ACCEPTED");
        em.merge(enrollment);
        return "Enrollment accepted successfuly!";
      } catch (Exception e) {
        return null;
      }
    }
    return "Enrollment not found!";
  }

  public String rejectEnrollment(long id) {
    Enrollment enrollment = em.find(Enrollment.class, id);
    if (enrollment != null) {
      try {
        enrollment.setStatus("REJECTED");
        em.merge(enrollment);
        return "Enrollment rejected successfuly!";
      } catch (Exception e) {
        return null;
      }
    }
    return "Enrollment not found!";
  }

  public Enrollment getEnrollmentById(long id) {
    return em.find(Enrollment.class, id);
  }

  public String deleteEnrollment(long id) {
    Enrollment enrollment = em.find(Enrollment.class, id);
    if (enrollment != null) {
      try {
        em.remove(enrollment);
        return "Enrollment deleted successfuly!";
      } catch (Exception e) {
        return null;
      }
    }
    return "Enrollment not found!";
  }

  public List<Enrollment> getEnrollmentsByStudentId(long studentId) {
    try {
      TypedQuery<Enrollment> query = em
          .createQuery("SELECT e FROM Enrollment e WHERE e.studentId = :studentId", Enrollment.class)
          .setParameter("studentId", studentId);
      return query.getResultList();
    } catch (Exception e) {
      return null;
    }
  }

}
