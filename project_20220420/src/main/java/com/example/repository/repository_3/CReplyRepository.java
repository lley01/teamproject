package com.example.repository.repository_3;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.entity2.CReply;

@Repository
public interface CReplyRepository extends JpaRepository<CReply, Long>{
	List<CReply> findByClubboard_CbnoOrderByRenumberDesc(long cbno);
	
	List<CReply> findByClubgallery_cgnoOrderByRenumberDesc(long cgno);
	
	void deleteByClubboard_cbno(long cbno);
	
	void deleteByClubgallery_cgno(long cgno);
	
	@Transactional
	void deleteByReparentnumber(long repnum);
}
