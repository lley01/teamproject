package com.example.controller.controller_4;

import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.example.entity.entity1.Member;
import com.example.entity.entity1.MemberPersonal;
import com.example.entity.entity1.Reaction;
import com.example.entity.entity2.BImage;
import com.example.entity.entity2.Bckeditor;
import com.example.entity.entity2.Board1;
import com.example.entity.entity2.Board1MemberView;
import com.example.entity.entity2.CReply;
import com.example.entity.entity2.MemberNicknameview;
import com.example.jwt.JwtUtil;
import com.example.repository.repository_3.Board1MemberViewRepository;
import com.example.repository.repository_3.PersonalMemberRepository;
import com.example.repository.repository_4.BckeditorRepository;
import com.example.repository.repository_4.Board1ImageRepository;
import com.example.repository.repository_4.Board1Repository;
import com.example.repository.repository_gibum.ReactionRepository;
import com.example.service.UserDetailsServiceImpl;
import com.example.service.service_4.Board1Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/api/board1")
public class Board1RestController {
    
    @Autowired ReactionRepository rRepository;

    @Autowired 
    Board1Repository b1Repository;

    @Autowired
    Board1ImageRepository b1IRepository;

    @Autowired
    BckeditorRepository bcRepository;
  
    @Autowired 
    Board1Service b1Service;
    
    @Autowired
    PersonalMemberRepository pmRep;
    
    @Autowired
    Board1MemberViewRepository bmvRep;
    
    @Autowired 
    JwtUtil jwtUtil;
    
    @Autowired 
    UserDetailsServiceImpl userDetailsService;

    @Autowired 
    ResourceLoader resLoader;

    @Value("${default.image}") String DEFAULT_IMAGE;

    // int PAGECNT = 10
    // global.properties 사용하기. 나중에 숫자 바꾸고 싶은대로 바꾸면 됨
    @Value("${board.page.count}") int PAGECNT;

    // 글쓰기
    //127.0.0.1:9090/cluver/api/board1/insert
    @RequestMapping(value = "/insert", 
        method = {RequestMethod.POST},
        consumes = {MediaType.ALL_VALUE},
        produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, Object> InsertPost(
        @ModelAttribute Board1 board1, 
        @RequestHeader (name = "token") String token ) {

        Map<String ,Object> map = new HashMap<>();

        try{
            // 토큰 추출
            String userid = jwtUtil.extractUsername(token);
            System.out.println("USERNAME ==>" + userid);

            Member memberEntity = new Member();
            memberEntity.setMid(userid);
            System.out.println(memberEntity);

            board1.setMember(memberEntity);
            System.out.println(board1.toString());

           
            if(token !=null) {
                int ret = b1Service.insertBoard1One(board1);
                System.out.println(board1.toString());
                if(ret == 1){
                    map.put("status", 200); // 성공
                    map.put("result", "등록완료");
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
            map.put("status", 0); // 실패
        }
        return map;
    }

    // @RequestMapping(value = "/insert", 
    //     method = {RequestMethod.POST},
    //     consumes = {MediaType.ALL_VALUE},
    //     produces = {MediaType.APPLICATION_JSON_VALUE})
    // public Map<String, Object> InsertPost(
    //     @ModelAttribute Board1 board1) {

    //     Map<String ,Object> map = new HashMap<>();

    //     try{

    //         int ret = b1Service.insertBoard1One(board1);
    //         System.out.println(board1.toString());
    //         if(ret == 1){
    //             map.put("status", 200); // 성공
    //             map.put("result", "등록완료");
    //         }
            
    //     }
    //     catch(Exception e){
    //         e.printStackTrace();
    //         map.put("status", 0); // 실패
    //     }
    //     return map;
    // }

    // ckeditor에서 첨부하는 이미지 보관하는 곳
    // 127.0.0.1:9090/cluver/api/board1/ckimage
    @RequestMapping(value = "/ckimage", 
        method = {RequestMethod.POST},
        consumes = {MediaType.ALL_VALUE},
        produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, Object> ckImagePOST(
      @RequestParam(name = "image") MultipartFile file ) {

        System.out.println(file.getOriginalFilename());

        Map<String ,Object> map = new HashMap<>();

        try{
            Bckeditor bckeditor = new Bckeditor();
            if(file.isEmpty() == false) {
                bckeditor.setBcimage(file.getBytes()); // 이미지
                bckeditor.setBcimagename(file.getOriginalFilename()); // 파일명
                bckeditor.setBcimagesize(file.getSize()); //사이즈
                bckeditor.setBcimagetype(file.getContentType());// 타입
            }

            bcRepository.save(bckeditor);

            map.put("status", 200);
            map.put("url", "/cluver/api/board1/image?biimgcode=" + bckeditor.getBcimgcode() ); // url 보내기

        }
        catch(Exception e){
            e.printStackTrace();
            map.put("status", 0); // 실패
        }
        return map;
    }

    // 127.0.0.1:9090/cluver/api/board1/image?biimgcode=1
    // <img th:src="@{/board1/image(biimgcode=1)}" style="width:100px" />
    @RequestMapping(value = "/image", 
        method = {RequestMethod.GET},
        consumes = {MediaType.ALL_VALUE},
        produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<byte[]> imageGET(
      @RequestParam(name = "biimgcode") Long biimgcode ) throws IOException  {

        Bckeditor bckeditor = bcRepository.getById(biimgcode);

        // 이미지명, 이미지크기, 이미지종류, 이미지데이터
        // BImage bImage = b1IRepository.getById(biimgcode);
        // System.out.println(bImage.getBiimagetype());
		// System.out.println(bImage.getBiimage().length);

        if (bckeditor.getBcimagesize() > 0) {
			HttpHeaders headers = new HttpHeaders();
			if (bckeditor.getBcimagetype().equals("image/jpeg")) {
				headers.setContentType(MediaType.IMAGE_JPEG);
			} else if (bckeditor.getBcimagetype().equals("image/png")) {
				headers.setContentType(MediaType.IMAGE_PNG);
			} else if (bckeditor.getBcimagetype().equals("image/gif")) {
				headers.setContentType(MediaType.IMAGE_GIF);
			}
			ResponseEntity<byte[]> response = new ResponseEntity<>(bckeditor.getBcimage(), headers, HttpStatus.OK);
			return response;
		} 
        else {
			InputStream is = resLoader.getResource(DEFAULT_IMAGE).getInputStream();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.IMAGE_JPEG);
			ResponseEntity<byte[]> response = new ResponseEntity<>(is.readAllBytes(), headers, HttpStatus.OK);
			return response;
		}
    }

    // 글 1개 삭제
    // 127.0.0.1:9090/cluver/api/board1/delete1
    // {"bno":3}
    @RequestMapping(value = "/delete1", method = {RequestMethod.DELETE}, consumes = {MediaType.ALL_VALUE},
                    produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, Object> boardDeletePost(@RequestParam(name = "bno") Long bno,
                                            @RequestHeader (name = "token")String token){
        // 키를 알고 보내야 함. 틀리면 안감. er다이어그램 보면 됨

        Map<String ,Object> map = new HashMap<>();

        try{
            // 토큰 추출
            String userid = jwtUtil.extractUsername(token);
            System.out.println("USERNAME ==>" + userid);

            b1Repository.deleteByMember_midAndBno(userid, bno);
            map.put("status", 200);
           
        }
        catch(Exception e){
            e.printStackTrace();
            map.put("status", 0); // 실패
        }
        return map;
    }

     // 127.0.0.1:9090/cluver/api/board1/delete
    // {"bno":3}
    @RequestMapping(value = "/delete", method = {RequestMethod.DELETE}, consumes = {MediaType.ALL_VALUE},
                    produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, Object> boardDeletePost(@RequestBody Board1 board1,
                                            @RequestHeader (name = "token")String token){
        // 키를 알고 보내야 함. 틀리면 안감. er다이어그램 보면 됨

        Map<String ,Object> map = new HashMap<>();

        try{
            // 토큰 추출
            String userid = jwtUtil.extractUsername(token);
            System.out.println("USERNAME ==>" + userid);

            Board1 board = b1Repository.getById(board1.getBno());
            System.out.println(board.toString());

            System.out.println("번호"+board1.getBno());

            if(userid.equals( board.getMember().getMid() )){
                // Board1 result = b1Service.selectBoard1One(board.getBno());

                // 삭제
                int ret = b1Service.deleteBoard1One(board.getBno());
                if(ret == 1){
                    map.put("status", 200); // 성공
                }
            }
            else if (!userid.equals( board.getMember().getMid() )){
                map.put("status", 0); 
            }
           
        }
        catch(Exception e){
            e.printStackTrace();
            map.put("status", -1); // 실패
        }
        return map;
    }

    // 글 수정
    // 127.0.0.1:9090/cluver/api/board1/update?bno=3
    // 제목, 내용, 번호
    // {"bno":2, "btitle":"222", "bcontent":"222"}
    @RequestMapping(value = "/update", method = {RequestMethod.PUT}, consumes = {MediaType.ALL_VALUE},
                    produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, Object> boardUpdatePost(
            // @RequestParam(name = "bno") Long bno,
            @ModelAttribute Board1 board1, 
            @RequestHeader (name = "TOKEN")String token ) {

        System.out.println(board1.toString());

        Map<String ,Object> map = new HashMap<>();

        try{
            // 토큰 추출
            String userid = jwtUtil.extractUsername(token);
            System.out.println("USERNAME ==>" + userid);

            Board1 board = b1Repository.getById(board1.getBno());
            System.out.println(board.toString());

            System.out.println("=====" + board.getMember().getMid());

            if(userid.equals( board.getMember().getMid() )){
                Board1 result = b1Service.selectBoard1One(board1.getBno());

                // 수정
                result.setBtitle(board1.getBtitle());
                result.setBcontent(board1.getBcontent());
                result.setBtype(board1.getBtype());

                int ret = b1Service.updateBoard1One(result);
                if(ret == 1){
                    map.put("status", 200); // 성공
                }
            }
            else if (!userid.equals( board.getMember().getMid() )){
                map.put("status", 0); 
            }
        }
        catch(Exception e){
            e.printStackTrace();
            map.put("status", -1); // 실패
        }
        return map;
    }

    // 1개 조회
    // 127.0.0.1:9090/cluver/api/board1/selectone?bno=2
    @RequestMapping(value = "/selectone", method = {RequestMethod.GET}, consumes = {MediaType.ALL_VALUE},
                    produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, Object> boardSelectOneGET(
        @RequestParam(name = "bno") Long bno,
        @RequestHeader (name = "token") String token ){

        Map<String ,Object> map = new HashMap<>();
        map.put("status", 0);
        try{
            if(token != null){
                // 1개 조회
                Board1 board1 = b1Service.selectBoard1One(bno);

                // 이전글
                Board1 prev = b1Repository.findTop1ByBnoLessThanOrderByBnoDesc(bno);
                // System.out.println(prev);

                // 다음글
                Board1 next = b1Repository.findTop1ByBnoGreaterThanOrderByBnoAsc(bno);
                // System.out.println(next);
                
                Board1MemberView bmv = bmvRep.findById(bno).orElse(null);
                
                if (prev != null){
                    map.put("prev", prev.getBno());
                }
                else {
                    map.put("prev", 0L);
                }
                if ( next != null) {
                    map.put("next", next.getBno());
                }
                else { 
                    map.put("next", 0L);
                }

                map.put("status", 200); // 성공 
                map.put("result", board1);
                map.put("nick", bmv);
                    
                
            }
               
        }
        catch(Exception e){
            e.printStackTrace();
            map.put("status", -1); // 실패
        }
        return map;
       
    }

    // 게시판 목록(페이지네이션만 있음, 검색x)
    // 127.0.0.1:9090/cluver/api/board1/selectlist?page=1
    @RequestMapping(value = "/selectlist", method = {RequestMethod.GET}, consumes = {MediaType.ALL_VALUE},
                    produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, Object> boardSelectListGET(
      
        @RequestParam(name = "page") int page ){

        Map<String ,Object> map = new HashMap<>();
        try{
            PageRequest pageRequest = PageRequest.of(page-1, PAGECNT);
            long total = b1Repository.count();
            // System.out.println(total);
            List<Board1> bList = b1Repository.findAllByOrderByBnoDesc(pageRequest);
            List<Board1MemberView> bmvlist = new ArrayList<>();
            for(int i =0;i<bList.size();i++){
                List<Reaction> reaction = rRepository.findByReaction_B_no(bList.get(i).getBno());
                bList.get(i).setBlike(reaction.size());
                
                
            	Board1MemberView bmv = bmvRep.findById(bList.get(i).getBno()).orElse(null);
                System.out.println(bmv);
                bmvlist.add(bmv);
            }
            
			map.put("mlist", bmvlist);
            // System.out.println(bList);
            map.put("status", 200); // 성공
            map.put("result", bList);
            map.put("result1", total);
       

        }
        catch(Exception e){
            e.printStackTrace();
            map.put("status", 0); // 실패
        }
        return map;
    }
    

    // 검색(제목기준) + 페이지네이션
    // 127.0.0.1:9090/cluver/api/board1/search
    @RequestMapping(value = "/search", method = {RequestMethod.GET}, consumes = {MediaType.ALL_VALUE},
                    produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, Object> searchGET(
        @RequestParam(name = "page") int page,
        @RequestParam(name = "btitle", defaultValue = "") String btitle ){

        System.out.println(btitle);

        Map<String ,Object> map = new HashMap<>();
        try{
            PageRequest pageRequest = PageRequest.of(page-1, PAGECNT);
            List<Board1> bList = b1Repository.findByBtitleContainingOrderByBnoDesc(btitle, pageRequest);
            
            List<Board1MemberView> bmvlist = new ArrayList<>();
            for(int i =0;i<bList.size();i++){
            	Board1MemberView bmv = bmvRep.findById(bList.get(i).getBno()).orElse(null);
                System.out.println(bmv);
                bmvlist.add(bmv);
            }
            
			map.put("mlist", bmvlist);
            // System.out.println(bList);
            map.put("status", 200); // 성공
            map.put("result", bList);

        }
        catch(Exception e){
            e.printStackTrace();
            map.put("status", 0); // 실패
        }
        return map;
    }

    // 127.0.0.1:9090/cluver/api/board1/search1
    @RequestMapping(value = "/search1", method = {RequestMethod.GET}, consumes = {MediaType.ALL_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
        public Map<String, Object> search1GET(
        @RequestParam(name = "page") int page,
        @RequestParam(name = "bcontent", defaultValue = "") String bcontent ){

        System.out.println(bcontent);

        Map<String ,Object> map = new HashMap<>();
        try{
        PageRequest pageRequest = PageRequest.of(page-1, PAGECNT);
        List<Board1> bList = b1Repository.findByBcontentContainingOrderByBnoDesc(bcontent, pageRequest);
        
        List<Board1MemberView> bmvlist = new ArrayList<>();
        for(int i =0;i<bList.size();i++){
        	Board1MemberView bmv = bmvRep.findById(bList.get(i).getBno()).orElse(null);
            System.out.println(bmv);
            bmvlist.add(bmv);
        }
        
		map.put("mlist", bmvlist);
        // System.out.println(bList);
        map.put("status", 200); // 성공
        map.put("result", bList);

        }
        catch(Exception e){
        e.printStackTrace();
        map.put("status", 0); // 실패
        }
        return map;
    }

    ////////////////////////////////////////////////////////////////////

    // 127.0.0.1:9090/cluver/api/board1/selectlist1?page=1&btitle=y
    @RequestMapping(value = "/selectlist1", method = {RequestMethod.GET}, consumes = {MediaType.ALL_VALUE},
                    produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, Object> boardSelectListGET(
        Model model,
        @RequestParam(name = "page", defaultValue = "1") int page,
        @RequestParam(name = "btitle", defaultValue = "") String btitle ){

        Map<String ,Object> map = new HashMap<>();
        try{
            // 페이지네이션 (시작페이지(0부터. 1페이지는 0임), 개수)
            // Pageable pageable = PageRequest.of(page-1, PAGECNT);

            // 검색어, 페이지네이션
            // List<Board1> list = b1Service.selectBoard1List(bTitle, pageRequest);
            // List<Board1> list = b1Repository.findByBtitleContainingOrderByBnoDesc(btitle, pageable);
            // model.addAttribute("list", list);

            // if(!list.isEmpty()){
            //     map.put("status", 200);
            //     map.put("result", list);
            // }

            // 검색어, 페이지네이션
            // List<Board1> list = b1Repository.findByBTitleContainingOrderByBNoDesc(bTitle, pageRequest);
            // List<Board1> blist = b1Service.selectBoard1List(bNo, pageable, bTitle);
            // List<Board1> blist = b1Service.selectBoard1List(bNo, bTitle, pageable);
           
            // System.out.println(list);
            // if(!list.isEmpty()){
            //     map.put("status", 200); // 성공
            //     map.put("result", list); 
            // }

            // 전체개수
            // long total = b1Repository.countByBtitleContaining(btitle);
            // map.put("pages", (total-1)/PAGECNT +1);
            // model.addAttribute("pages", (total-1)/PAGECNT +1 );

        }
        catch(Exception e){
            e.printStackTrace();
            map.put("status", 0); // 실패
        }
        return map;
    }



    // 게시물 조회수 1증가 시킴
    // 127.0.0.1:9090/cluver/api/board1/updatehit?bno=2
    @RequestMapping(value = "/updatehit", method = {RequestMethod.PUT}, consumes = {MediaType.ALL_VALUE},
                    produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, Object> boardUpdateHitGET(
        @RequestParam(name = "bno") long bno,
        @RequestHeader (name = "token")String token){

        Map<String ,Object> map = new HashMap<>();
        try{
            if(token != null){
                int ret = b1Service.updateBoard1HitOne(bno);
                if(ret == 1){
                    map.put("status", 200); // 성공
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
            map.put("status", 0); // 실패
        }
        return map;
    }

    // 이전글
    // 127.0.0.1:9090/cluver/api/board1/prev
    @RequestMapping(value = "/prev", method = {RequestMethod.GET}, consumes = {MediaType.ALL_VALUE},
                    produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, Object> prevGET(
        @RequestParam(name = "bno") long bno,
        @RequestHeader (name = "token")String token){

        Map<String ,Object> map = new HashMap<>();
        try{
            if(token != null){

                Board1 prev = b1Repository.findTop1ByBnoLessThanOrderByBnoDesc(bno);
                if(prev.getBno()!=null){
                    Board1 board1 = b1Repository.findById(prev.getBno()).orElse(null);
                
                    map.put("result", board1);
                    map.put("status", 200); // 성공
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
            map.put("status", 0); // 실패
        }
        return map;
    }

    // 다음글
    // 127.0.0.1:9090/cluver/api/board1/next
    @RequestMapping(value = "/next", method = {RequestMethod.GET}, consumes = {MediaType.ALL_VALUE},
                    produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, Object> nextGET(
        @RequestParam(name = "bno") long bno,
        @RequestHeader (name = "token")String token){

        Map<String ,Object> map = new HashMap<>();
        try{
            if(token != null){

                Board1 next = b1Repository.findTop1ByBnoGreaterThanOrderByBnoAsc(bno);
                if(next.getBno()!=null){
                    Board1 board1 = b1Repository.findById(next.getBno()).orElse(null);
                
                    map.put("result", board1);
                    map.put("status", 200); // 성공

                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
            map.put("status", 0); // 실패
        }
        return map;
    }

    // 일괄삭제
    // 관리자만 가능하도록 해야하는데 아직 그건 구현 못함
    // 127.0.0.1:9090/cluver/api/board1/deletebatch
    @RequestMapping(value = "/deletebatch", method = {RequestMethod.POST}, consumes = {MediaType.ALL_VALUE},
                    produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, Object> deleteBatch(
        @RequestParam(name = "bno") Long[] bno,
        @RequestHeader (name = "token")String token){

        Map<String ,Object> map = new HashMap<>();
        try{
            // 토큰 추출
            String userid = jwtUtil.extractUsername(token);
            System.out.println("USERNAME ==>" + userid);

            b1Repository.deleteByMember_midAndBnoIn(userid, bno);
            map.put("status", 200); // 성공
        }
        catch(Exception e){
            e.printStackTrace();
            map.put("status", 0); // 실패
        }
        return map;
    }

   
    // 게시글 작성자와 토큰의 아이디가 일치하는 글만 조회
    // 127.0.0.1:9090/cluver/api/board1/selectboard
    @RequestMapping(value = "/selectboard", method = {RequestMethod.GET}, consumes = {MediaType.ALL_VALUE},
                    produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, Object> selectBoardGET(
        @RequestParam(name = "page") int page, 
        @RequestHeader (name = "token") String token ){

        Map<String ,Object> map = new HashMap<>();
        try{
            // 토큰 추출
            String userid = jwtUtil.extractUsername(token);
            System.out.println("USERNAME ==>" + userid);

            PageRequest pageRequest = PageRequest.of(page-1, PAGECNT);
            long total = b1Repository.countByMember_mid(userid);

            List<Board1> bList = b1Repository.findByMember_midOrderByBnoDesc(userid, pageRequest);
            // System.out.println(qList);
            map.put("status", 200); // 성공
            map.put("result", bList);
            map.put("total", total);

        }
        catch(Exception e){
            e.printStackTrace();
            map.put("status", 0); // 실패
        }
        return map;
    } 


    
}
