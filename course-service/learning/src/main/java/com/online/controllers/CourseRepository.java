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
      obj.setStatus("PENDING");
      em.persist(obj);
      return obj;
    } catch (Exception e) {
      return null;
    }
  }

  public Course findCourseById(long id) {
    Course course = em.find(Course.class, id);
    if (course != null) {
      return course;
    }
    return null;
  }

  public String acceptCourse(long id) {
    Course course = em.find(Course.class, id);
    if (course != null) {
      try {
        course.setStatus("ACCEPTED");
        em.merge(course);
        return "Course accepted successfuly!";
      } catch (Exception e) {
        throw e;
      }
    }
    return "Course not found!";
  }

  public String rejectCourse(long id) {
    Course course = em.find(Course.class, id);
    if (course != null) {
      try {
        course.setStatus("REJECTED");
        em.merge(course);
        return "Course rejected successfuly!";
      } catch (Exception e) {
        throw e;
      }
    }
    return "Course not found!";
  }

  public String removeCourse(long id) {
    Course course = em.find(Course.class, id);
    if (course != null) {
      try {
        em.remove(course);
        return "Course removed successfully!";
      } catch (Exception e) {
        throw e;
      }
    }
    return "Course not found!";
  }

  public String updateCourse(long id, Course course) {
    Course obj = em.find(Course.class, id);
    if (obj != null) {
      try {
        obj.setName(course.getName());
        obj.setDuration(course.getDuration());
        obj.setCapacity(course.getCapacity());
        obj.setCategory(course.getCategory());
        obj.setContent(course.getContent());
        em.merge(obj);
        return "Course updated successfuly!";
      } catch (Exception e) {
        return null;
      }
    }
    return "Course not found!";
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
      TypedQuery<Course> courses = em.createQuery("SELECT c FROM Course c WHERE c.status = 'ACCEPTED'", Course.class);
      return courses.getResultList();
    } catch (Exception e) {
      return null;
    }
  }

  public List<Course> findCourses(String searchTerm) {
    try {
      TypedQuery<Course> query = em
          .createQuery(
              "SELECT c FROM Course c WHERE (c.name = :searchTerm OR c.category = :searchTerm) AND c.status = 'ACCEPTED'",
              Course.class);
      query.setParameter("searchTerm", searchTerm);
      return query.getResultList();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

}
