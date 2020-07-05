package kr.ac.skhu.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import kr.ac.skhu.project.domain.Guardian;

@Repository
public interface GuardianRepository extends JpaRepository<Guardian, String>
{

}