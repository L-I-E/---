package kr.ac.skhu.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.ac.skhu.project.domain.Driving;

@Repository
public interface DrivingRepository extends JpaRepository<Driving, String> {

}
