package com.example.entity.entity2;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Data
@Entity
@Table(name = "ADDRESSTBL")
@SequenceGenerator(name = "SEQ_ADDRESS",
sequenceName = "SEQ_ADDRESS_CODE", 
allocationSize = 1, initialValue = 1)
public class Address {
  
  // 주소코드
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, 
  generator = "SEQ_ADDRESS") // 시퀀스 적용
  @Column(name = "ACODE")
  private Long acode;
  // 시, 도
  @Column(name = "A1")
  private String a1;
  // 구, 군
  @Column(name = "A2")
  private String a2;
  // 주소상세
  @Column(name = "ADETAIL")
  private String adetail;
  // 동호회
  // @OneToMany(mappedBy = "address")
  // @JsonBackReference
  // private List<Club> clubList = new ArrayList<>();
}
