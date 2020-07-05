package kr.ac.skhu.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.ac.skhu.project.domain.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, String>
{
	
}