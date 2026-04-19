package com.example.personal_notebook.service;

import com.example.personal_notebook.model.Note;
import com.example.personal_notebook.model.User;
import com.example.personal_notebook.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    public Note createNote(String title, String content, String priority, User user) {
        Note note = new Note();
        note.setTitle(title.trim());
        note.setContent(content);
        // User sets priority — default to LOW if invalid value passed
        if (priority != null && priority.equalsIgnoreCase("HIGH")) {
            note.setPriority("HIGH");
        } else {
            note.setPriority("LOW");
        }
        note.setUser(user);
        return noteRepository.save(note);
    }

    public List<Note> getNotesByUser(User user) {
        return noteRepository.findByUserOrderByPriorityAndDate(user);
    }

    public boolean deleteNote(Long id, User user) {
        return noteRepository.findByIdAndUser(id, user).map(note -> {
            noteRepository.delete(note);
            return true;
        }).orElse(false);
    }
}