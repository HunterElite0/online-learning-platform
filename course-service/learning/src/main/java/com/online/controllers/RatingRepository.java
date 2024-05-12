package com.online.controllers;

import java.util.List;

import com.online.model.Enrollment;
import com.online.model.Rating;

import jakarta.ejb.Stateful;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Stateful
public class RatingRepository {

  @PersistenceContext(unitName = "AppDB")
  private EntityManager em;

  public String makeRating(Rating rating) {
    try {
      Rating obj = new Rating();
      obj.setCourseId(rating.getCourseId());
      obj.setRating(rating.getRating());
      obj.setReview(rating.getReview());
      obj.setStudentId(rating.getStudentId());
      em.persist(obj);

      return "Rating submitted!";
    } catch (Exception e) {
      return "Rating request failed!";
    }
  }

  public int getNumberOfRatings(long courseId) {
    TypedQuery<Rating> query = em.createQuery("SELECT r FROM Rating r WHERE r.courseId = :courseId", Rating.class);
    query.setParameter("courseId", courseId);
    List<Rating> ratings = query.getResultList();
    return ratings.size();
  }

}
