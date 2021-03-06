package com.example.entity.entity1;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.example.entity.entity2.Club;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Entity
@Table(name = "JOINCLUBTBL")
@SequenceGenerator(name = "SEQ_JOINCLUB", sequenceName = "SEQ_JOINCLUB_NO", allocationSize = 1, initialValue = 1)
public class JoinClub {
  // 시퀀스
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_JOINCLUB") // 시퀀스 적용
  private Long no;
  // 신청날짜
  @Column(name = "JCDATE")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	@CreationTimestamp // CURRENT_DATE
  private Date jcdate;
  // 동호회
  @ManyToOne
  @JoinColumn(name = "c_no")
  private Club club;
  // 회원
  @ManyToOne
  @JoinColumn(name = "m_id")
  private Member member;
  // 클럽활동내역
  @ManyToOne
  @JoinColumn(name = "s_code")
  private Steptbl steptbl;
}