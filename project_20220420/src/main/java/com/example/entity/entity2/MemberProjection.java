package com.example.entity.entity2;

import org.springframework.beans.factory.annotation.Value;

public interface MemberProjection {

    // 시퀀스번호
    String getMpno();

    String getMpnickname();

    // 닉네임
    // @Value("#{target.member.mid")
    // String getMemberMid();
    
}
