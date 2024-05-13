package com.online.controllers;

import com.online.model.Notification;
import com.online.model.Rating;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateful;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Stateful
public class NotificationRepository {
    @PersistenceContext(unitName = "AppDB")
    private EntityManager em;

    @EJB
    CourseRepository courseRepository;

    public Notification makeNotification(Notification notification) {
    try {
        Notification obj = new Notification();
        obj.setStudentId(notification.getStudentId());
        obj.setContent(notification.getContent());
        em.persist(obj);
        return obj;

    } catch (Exception e) {
      return null;
    }
  }
    
}
