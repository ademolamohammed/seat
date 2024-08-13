package org.example.seatdemo.repository;


import org.example.seatdemo.entity.SeatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SeatRepository extends JpaRepository<SeatEntity,Long> {
    Optional<SeatEntity> findBySeatCode(String seatCode);

}
