package com.online.controllers;

import java.util.List;

import com.online.model.Course;

import jakarta.ejb.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Singleton
public class CourseRepository {

  @PersistenceContext(unitName = "AppDB")
  private EntityManager em;

  public Course create(Course course) {
    try {
      Course obj = new Course();
      obj.setName(course.getName());
      obj.setDuration(course.getDuration());
      obj.setCapacity(course.getCapacity());
      obj.setCategory(course.getCategory());
      obj.setContent(course.getContent());
      obj.setRating(0.0);
      obj.setStatus("pending");
      em.persist(obj);
      return obj;
    } catch (Exception e) {
      return null;
    }
  }

  public String acceptCourse(int courseId) {
    try {
      Course course = em.find(Course.class, courseId);
      if (course != null) {
        em.detach(course);
        course.setStatus("ACCEPTED");
        em.getTransaction().begin();
        em.merge(course);
        em.getTransaction().commit();
        return "Course accepted successfuly!";
      }
      return "Course not found!";
    } catch (Exception e) {
      return null;
    }
  }

  public String rejectCourse(int courseId) {
    try {
      Course course = em.find(Course.class, courseId);
      if (course != null) {
        em.detach(course);
        course.setStatus("REJECTED");
        em.getTransaction().begin();
        em.merge(course);
        em.getTransaction().commit();
        return "Course rejected successfuly!";
      }
      return "Course not found!";
    } catch (Exception e) {
      return null;
    }
  }

  public List<Course> listAllCourses() {
    try {
      TypedQuery<Course> courses = em.createQuery("SELECT c FROM Course c", Course.class);
      return courses.getResultList();
    } catch (Exception e) {
      return null;
    }
  }

  public List<Course> listAllValidCourses() {
    try {
      TypedQuery<Course> courses = em.createQuery("SELECT c FROM Course c WHERE c.status = ACCEPTED", Course.class);
      return courses.getResultList();
    } catch (Exception e) {
      return null;
    }
  }

  public List<Course> findCourses(String searchTerm) {
    try {
      TypedQuery<Course> query = em
          .createQuery("SELECT c FROM Course c WHERE c.name = :searchTerm OR c.category = :searchTerm", Course.class);
      query.setParameter("searchTerm", searchTerm);
      return query.getResultList();
    } catch (Exception e) {
      return null;
    }
  }

}
