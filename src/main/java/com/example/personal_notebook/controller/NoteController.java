package com.example.personal_notebook.controller;

import com.example.personal_notebook.model.Note;
import com.example.personal_notebook.model.User;
import com.example.personal_notebook.service.NoteService;
import com.example.personal_notebook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @Autowired
    private UserService userService;

    private User getCurrentUser(Authentication auth) {
        return userService.findByUsername(auth.getName());
    }

    @PostMapping
    public ResponseEntity<?> addNote(@RequestBody Map<String, String> body,
                                     Authentication auth) {
        String title    = body.get("title");
        String content  = body.get("content");
        String priority = body.get("priority"); // HIGH or LOW from user

        if (title == null || title.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Title is required"));
        }
        if (priority == null ||
                (!priority.equalsIgnoreCase("HIGH") && !priority.equalsIgnoreCase("LOW"))) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Priority must be HIGH or LOW"));
        }

        User user = getCurrentUser(auth);
        Note note = noteService.createNote(title.trim(), content, priority.toUpperCase(), user);
        return ResponseEntity.ok(toMap(note));
    }

    @GetMapping
    public ResponseEntity<?> getNotes(Authentication auth) {
        User user = getCurrentUser(auth);
        List<Note> notes = noteService.getNotesByUser(user);
        List<Map<String, Object>> result = new ArrayList<>();
        for (Note n : notes) result.add(toMap(n));
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable Long id,
                                        Authentication auth) {
        User user = getCurrentUser(auth);
        boolean deleted = noteService.deleteNote(id, user);
        if (deleted) {
            return ResponseEntity.ok(Map.of("message", "Note deleted"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Note not found or not authorized"));
    }

    private Map<String, Object> toMap(Note note) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", note.getId());
        map.put("title", note.getTitle());
        map.put("content", note.getContent());
        map.put("priority", note.getPriority());
        map.put("createdAt", note.getCreatedAt()
                .format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a")));
        return map;
    }
}