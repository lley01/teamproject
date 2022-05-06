package com.example.entity.entity2;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.example.entity.entity1.ClubGallery;
import com.example.entity.entity1.Member;
import com.example.entity.entity1.Reaction;
import com.fasterxml.jackson.annotation.JsonBackReference;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Entity
@Table(name = "CREPLYTBL")
@SequenceGenerator(name = "SEQ_CREPLY",
sequenceName = "SEQ_CREPLY_NO", 
allocationSize = 1, initialValue = 1)
public class CReply {

  // 댓글 번호
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, 
  generator = "SEQ_CREPLY") // 시퀀스 적용
  private Long reNumber;

  // 댓글 내용
  @Column(nullable = false)
  @Lob
  private String reContent;

  // 댓글 작성일
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
  @CreationTimestamp // CURRENT_DATE
  private Date reRegdate;

  // 부모댓글번호
  private Long reParentnumber;

  // 수정일
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	@UpdateTimestamp // CURRENT_DATE
	@Column(name = "CBIREGDATE")
  private Date reUpdatedate;

  // 자유게시판 글번호
  @ManyToOne
  @JoinColumn(name = "b_no")
  private Board1 board1;

  // 클럽게시판 글번호
  @ManyToOne
  @JoinColumn(name = "cb_no")
  private ClubBoard clubBoard;

  // 댓글 공개여부
  @Column(nullable = false)
  private String rePrivate;

  // 반응
  @ManyToOne
  @JoinColumn(name = "r_id")
  private Reaction reaction;

  // 회원 아이디
  @ManyToOne
  @JoinColumn(name = "m_id")
  private Member member;

  // 갤러리
  // @OneToMany(mappedBy = "cReply")
  // @JsonBackReference
  // private List<ClubGallery> clubGalleryList = new ArrayList<>();
}