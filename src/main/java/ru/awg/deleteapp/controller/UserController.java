package ru.awg.deleteapp.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.awg.deleteapp.service.UserService;

@RestController
@RequestMapping("/api/data")
@RequiredArgsConstructor

public class UserController {

  private final UserService service;

  @DeleteMapping("/delete-old")
  public ResponseEntity<String> deleteOldData(@RequestParam("before") LocalDateTime before) {
      service.deleteOldData(before);
      return ResponseEntity.ok("Deletion process started for records before " + before);
  }
}
