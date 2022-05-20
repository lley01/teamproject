package com.example.repository.repository_gibum;

import java.util.List;

import com.example.entity.entity2.Club;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubRepository 
    extends JpaRepository<Club, Long>{

        // findBy컬럼명ContainingOrderBy컬럼명Desc
        // SELECT * FROM 테이블명
        // WHERE TITLE LIKE '%' || '검색어' || '%' ORDER BY BNO DESC 검색 + 정렬
        // List<Club> findBycName(String title);

        // @Query(value = "SELECT * FROM"+
        // " ADDRESSTABLEVIEW WHERE A_CODE=:no", nativeQuery = true)
        // public List<Club> addressclublList(
        //         @Param(value="no") Long no);

        Club findByCno(long cno);

        List<Club> findByCprivate(String cprivate);

    
}
