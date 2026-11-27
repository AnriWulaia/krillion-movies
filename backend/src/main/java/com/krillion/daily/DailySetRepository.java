package com.krillion.daily;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface DailySetRepository extends JpaRepository<DailySet, Long> {

    Optional<DailySet> findByPlayDate(LocalDate playDate);
}
