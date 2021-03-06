package com.example.service.service_4;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import com.example.entity.entity1.Member;
import com.example.entity.entity2.Board1;
import com.example.entity.entity2.CReply;
import com.example.repository.repository_4.Board1Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class Board1ServiceImpl implements Board1Service{

    @Autowired 
    Board1Repository b1Repository;

    @Autowired 
    EntityManagerFactory emf;

    // controller-> service + serviceimpl -> repository
    // 컨트롤러는 서비스를 호출하는거고, 원래는 다이렉트로 호출하면 안됨. 
    // 여러 사람이 할 때는 service가 들어가야 함. 그래야 일들이 명시가 됨

    @Override
    public int insertBoard1One(Board1 board1) {
       
        try{
            Board1 insertBoard = new Board1();
            insertBoard.setBtitle(board1.getBtitle());
            insertBoard.setBcontent(board1.getBcontent());
            insertBoard.setMember(board1.getMember());    

            b1Repository.save(board1);
            return 1;
        }
        catch(Exception e){
            e.printStackTrace();
            return 0;
        }        
    }

    @Override
    public int updateBoard1One(Board1 board1) {
        try{
            Board1 updateBoard = new Board1();

            // 변경할 항목만 set
            updateBoard.setBtitle( board1.getBtitle() );
            updateBoard.setBcontent( board1.getBcontent() );
            // updateBoard.setBImageList( board1.getBImageList() ); // 이미지 이렇게 해도 되나?
           

            b1Repository.save(board1);
            return 1;

        }
        catch(Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int deleteBoard1One(Long bno) {
        
        try{
            b1Repository.deleteById(bno);
            return 1;
        }
        catch(Exception e){
            e.printStackTrace();
            return 0;
        }
     }

    // @Override
    // public int deleteBoard1Batch(Long[] bno) {
    //     try{
    //         for( Board1 board1 : list) {
	// 			//기본키를 이용해서 기존 데이터를 꺼냄
             
    //             b1Repository.deleteById(board1.getBNo());
	// 		}
    //         return 1;
    //     }
    //     catch(Exception e){
    //         e.printStackTrace();
    //         return 0;
    //     }
    // }

    @Override
    public int deleteBoard1Batch(Long[] bno) {
        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();

            for( Long tmp : bno) {
				//기본키를 이용해서 기존 데이터를 꺼냄
                Board1 board1 
                = em.find(Board1.class, tmp);
                em.remove(board1);
			}
            
            em.getTransaction().commit();
            return 1;
        }
        catch(Exception e){
            e.printStackTrace();
            em.getTransaction().rollback();
            return 0;
        }
    }

    @Override
    public int updateBoard1HitOne(Long bno) {
        try{
            Board1 board1 = b1Repository.findById(bno).orElse(null);
            board1.setBhit( board1.getBhit() + 1L );

            b1Repository.save(board1);
            return 1;

        }
        catch(Exception e){
            e.printStackTrace();
            return 0;
        }

    }

    @Override
    public Board1 selectBoard1One(Long bno) {
        try{
            return b1Repository.findById(bno).orElse(null);
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public List<Board1> selectBoard1List(String btitle, PageRequest pageRequest) {
        // try{
        //     return b1Repository.findByBTitleContainingOrderByBNoDesc(btitle, pageRequest);

        // }
        // catch(Exception e){
        //     e.printStackTrace();
        //     return null;
        // }
        return null;
    }

     
}
