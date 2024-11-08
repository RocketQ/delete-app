package ru.awg.deleteapp.service;

import java.time.LocalDateTime;
import java.util.List;

public interface UserService {

  void deleteOldData(LocalDateTime limitDateTime);

}
