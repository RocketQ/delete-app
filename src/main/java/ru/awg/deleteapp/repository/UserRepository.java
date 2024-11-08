package ru.awg.deleteapp.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.awg.deleteapp.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @BatchSize(size = 10_000)
    @Query("SELECT u FROM UserEntity u WHERE u.dateTime < :limitDateTime")
    List<UserEntity> findAllByDateTimeBefore(LocalDateTime limitDateTime);
}
