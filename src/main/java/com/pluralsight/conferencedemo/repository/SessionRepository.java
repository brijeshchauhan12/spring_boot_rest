package com.pluralsight.conferencedemo.repository;

import com.pluralsight.conferencedemo.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session,Long> {

}
