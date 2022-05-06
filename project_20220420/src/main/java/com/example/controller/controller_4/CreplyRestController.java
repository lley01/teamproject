package com.example.controller.controller_4;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.entity.entity1.Member;
import com.example.entity.entity2.CReply;
import com.example.jwt.JwtUtil;
import com.example.repository.repository_4.CreplyRepository;
import com.example.service.service_4.CreplyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/creply")
public class CreplyRestController {

    @Autowired
    CreplyRepository cRepository;

    @Autowired
    CreplyService cService;

    @Autowired
    JwtUtil jwtUtil;

    //127.0.0.1:9090/ROOT/creply/insertreply
    @RequestMapping(value = "/insert", 
    method = {RequestMethod.POST},
    consumes = {MediaType.ALL_VALUE},
    produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, Object> InsertPost(
        @ModelAttribute CReply cReply,
        @RequestHeader (name = "token") String token ) {

        Map<String ,Object> map = new HashMap<>();

        try{
            // 토큰 추출
            String userid = jwtUtil.extractUsername(token);
            System.out.println("USERNAME ==>" + userid);

            Member memberEntity = new Member();
            memberEntity.setMId(userid);
            System.out.println(memberEntity);

            cReply.setMember(memberEntity);
            System.out.println(cReply.toString());

            if(token !=null) {
                cRepository.save(cReply);
                map.put("status", 200); // 성공
            }
        }
        catch(Exception e){
            e.printStackTrace();
            map.put("status", 0); // 실패
        }
        return map;
    }
    

    // 127.0.0.1:9090/ROOT/creply/delete
    // {"bno":3}
    @RequestMapping(value = "/delete", method = {RequestMethod.DELETE}, consumes = {MediaType.ALL_VALUE},
        produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, Object> boardDeletePost(
        @RequestBody CReply cReply,
        @RequestHeader (name = "token")String token ){
        // 키를 알고 보내야 함. 틀리면 안감. er다이어그램 보면 됨

        Map<String ,Object> map = new HashMap<>();

        try{
            // 토큰 추출
            String userid = jwtUtil.extractUsername(token);
            System.out.println("USERNAME ==>" + userid);

            CReply cReply1 = cRepository.getById(cReply.getReNumber());
            System.out.println(cReply1.toString());

            System.out.println("번호"+cReply.getReNumber());

            if(userid.equals( cReply1.getMember().getMId() )){
                // Board1 result = b1Service.selectBoard1One(board.getBNo());

                // 삭제
                cRepository.deleteById(cReply1.getReNumber());
                map.put("status", 200); // 성공

                
            }
            else if (!userid.equals( cReply1.getMember().getMId() )){
                map.put("status", 0); 
            }
           
        }
        catch(Exception e){
            e.printStackTrace();
            map.put("status", -1); // 실패
        }
        return map;
    }

    // 127.0.0.1:9090/ROOT/creply/update
    // 제목, 내용, 번호
    // {"bno":2, "btitle":"222", "bcontent":"222"}
    @RequestMapping(value = "/update", method = {RequestMethod.PUT}, consumes = {MediaType.ALL_VALUE},
                    produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, Object> boardUpdatePost(
            @ModelAttribute CReply cReply, 
            @RequestHeader (name = "TOKEN")String token ) {

        Map<String ,Object> map = new HashMap<>();

        try{
            // 토큰 추출
            String userid = jwtUtil.extractUsername(token);
            System.out.println("USERNAME ==>" + userid);

            CReply cReply1 = cRepository.getById(cReply.getReNumber());
            System.out.println(cReply1.toString());

            System.out.println("번호"+cReply.getReNumber());

            if(userid.equals( cReply1.getMember().getMId() )){
                // Board1 result = b1Service.selectBoard1One(board.getBNo());
                CReply result = cRepository.getById(cReply1.getReNumber());

                // 수정
                result.setReContent(cReply.getReContent());
                result.setRePrivate(cReply.getRePrivate());

                cRepository.save(result);
                
                map.put("status", 200); // 성공
                
            }
            else if (!userid.equals( cReply1.getMember().getMId() )){
                map.put("status", 0); 
            }
        }
        catch(Exception e){
            e.printStackTrace();
            map.put("status", -1); // 실패
        }
        return map;
    }

    // 원본 글번호가 일치하는 댓글 조회
    // 127.0.0.1:9090/ROOT/creply/selectone
    @RequestMapping(value = "/selectone", method = {RequestMethod.GET}, consumes = {MediaType.ALL_VALUE},
                    produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, Object> boardSelectOneGET(
        @RequestParam(name = "bNo") Long bNo,
        @RequestHeader (name = "token") String token){

        Map<String ,Object> map = new HashMap<>();

        try{
            if(token != null){

                List<CReply> cReply = cRepository.findByBoard1_bNoOrderByReNumberAsc(bNo);

                map.put("result",cReply);
                map.put("status",200);

            }

        }
        catch(Exception e){
            e.printStackTrace();
            map.put("status", 0); // 실패
        }
        return map;

    }




}
