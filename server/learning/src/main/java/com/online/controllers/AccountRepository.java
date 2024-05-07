package com.online.controllers;

import java.util.List;

import com.online.model.Account;

import jakarta.ejb.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Singleton
public class AccountRepository {

  @PersistenceContext(unitName = "AppDB")
  private EntityManager em;

  public Account register(String name, String email, String password) {
    try {
      Account account = new Account();
      account.setName(name);
      account.setEmail(email);
      account.setPassword(password);
      em.persist(account);
      return account;
    } catch (Exception e) {
      return null;
    }
  }

  public Account login(String email, String password) {
    TypedQuery<Account> query = em.createQuery("SELECT a FROM Account a WHERE a.email = ?1", Account.class)
        .setParameter(1, email);

    List<Account> resultList = query.getResultList();
    if (resultList.isEmpty()) {
      return null;
    }
    return resultList.get(0);
  }

}
