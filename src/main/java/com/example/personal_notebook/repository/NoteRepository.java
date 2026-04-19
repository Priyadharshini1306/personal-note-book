package com.example.personal_notebook.repository;

import com.example.personal_notebook.model.Note;
import com.example.personal_notebook.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {

    @Query("SELECT n FROM Note n WHERE n.user = :user ORDER BY " +
            "CASE n.priority WHEN 'HIGH' THEN 0 ELSE 1 END, n.createdAt DESC")
    List<Note> findByUserOrderByPriorityAndDate(User user);

    Optional<Note> findByIdAndUser(Long id, User user);
}