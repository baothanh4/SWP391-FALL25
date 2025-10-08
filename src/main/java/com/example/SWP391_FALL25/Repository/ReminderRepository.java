package com.example.SWP391_FALL25.Repository;

import com.example.SWP391_FALL25.Entity.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder,Long> {
}
