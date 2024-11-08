package ru.awg.deleteapp.service.impl;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.awg.deleteapp.entity.UserEntity;
import ru.awg.deleteapp.repository.UserRepository;
import ru.awg.deleteapp.service.UserService;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final ExecutorService executorService = Executors.newFixedThreadPool(3);
  private final Semaphore semaphore = new Semaphore(3);

  @Transactional
  public void deleteOldData(LocalDateTime limitDateTime) {
    List<UserEntity> usersToDelete = userRepository.findAllByDateTimeBefore(limitDateTime);
    int batchSize = 10_000;
    IntStream.range(0, (usersToDelete.size() + batchSize - 1) / batchSize)
        .forEach(batchIndex -> {
          int start = batchIndex * batchSize;
          int end = Math.min(start + batchSize, usersToDelete.size());
          List<UserEntity> batch = usersToDelete.subList(start, end);

          try {
            semaphore.acquire();
            executorService.submit(() -> {
              try {
                deleteBatch(batch);
              } finally {
                semaphore.release();
              }
            });
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Interrupted while acquiring semaphore", e);
          }
        });
  }

  private void deleteBatch(List<UserEntity> batch) {
    userRepository.deleteAll(batch);
    log.info("Deleting batch with size: {}", batch.size());
  }
}