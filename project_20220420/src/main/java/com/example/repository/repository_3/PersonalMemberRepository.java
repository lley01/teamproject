package com.example.repository.repository_3;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.entity1.MemberPersonal;

@Repository
public interface PersonalMemberRepository extends JpaRepository<MemberPersonal, Long>{
	MemberPersonal findByMember_mid(String mid); 
	
	List<MemberPersonal> findByMember_midOrderByMpnicknameAsc(String[] mid);
	
	MemberPersonal findByMpnicknameContaining(String mpnickname);

}
