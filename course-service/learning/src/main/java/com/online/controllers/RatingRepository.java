package com.online.controllers;

import java.util.List;

import com.online.model.Rating;
import com.online.controllers.CourseRepository;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateful;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Stateful
public class RatingRepository {

  @PersistenceContext(unitName = "AppDB")
  private EntityManager em;

  @EJB
  CourseRepository courseRepository;

  public Rating makeRating(Rating rating) {
    try {
      if (courseRepository.findCourseById(rating.getCourseId()) != null){
        Rating obj = new Rating();
        obj.setCourseId(rating.getCourseId()); // validation that course with this id exists
        obj.setRating(rating.getRating());
        obj.setReview(rating.getReview());
        obj.setStudentId(rating.getStudentId()); // From jwt token
        em.persist(obj);
        return obj;
      }
      else {
        throw new Exception("Course with ID " + rating.getCourseId() + " does not exist"); 
      }
    } catch (Exception e) {
      return null;
    }
  }

  public int getNumberOfRatings(long courseId) {
    try {
      TypedQuery<Rating> query = em.createQuery("SELECT r FROM Rating r WHERE r.courseId = :courseId", Rating.class);
      query.setParameter("courseId", courseId);
      List<Rating> ratings = query.getResultList();
      return ratings.size();
    } catch (Exception e) {
      return -1;
    }
  }

  public List<Rating> getAllRatings() {
    try {
      TypedQuery<Rating> query = em.createQuery("SELECT r FROM Rating r", Rating.class);
      return query.getResultList();
    } catch (Exception e) {
      return null;
    }
  }

}
