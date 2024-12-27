package com.example.demo.repository;

import com.example.demo.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByCategory(String category);

    List<Event> findByLocation(String location);

    List<Event> findByDate(String date);
    
    Page<Event> findByCategory(String category, Pageable pageable);

    Page<Event> findByLocation(String location, Pageable pageable);

    Page<Event> findByDate(String date, Pageable pageable);

    Page<Event> findAll(Pageable pageable);
}
