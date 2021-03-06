package com.example.controller.controller_4;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.entity.entity1.Member;
import com.example.entity.entity1.MemberPersonal;
import com.example.entity.entity1.QImage;
import com.example.entity.entity1.Qna;
import com.example.entity.entity2.Qckeditor;
import com.example.entity.entity2.QnaMemberView;
import com.example.jwt.JwtUtil;
import com.example.repository.repository_3.PersonalMemberRepository;
import com.example.repository.repository_4.QckeditorRepository;
import com.example.repository.repository_4.QnaMemberViewRepository;
import com.example.repository.repository_4.QnaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/api/qna")
public class QnaRestController {

    @Autowired
    QnaRepository qRepository;

    @Autowired
    QckeditorRepository qcRepository;
    
    @Autowired
    PersonalMemberRepository pmRep;

    @Autowired
    QnaMemberViewRepository qmvRep;
    
    @Autowired 
    JwtUtil jwtUtil;

    @Autowired 
    ResourceLoader resLoader;

    @Value("${default.image}") String DEFAULT_IMAGE;

    // int PAGECNT = 10
    // global.properties ????????????. ????????? ?????? ????????? ???????????? ????????? ???
    @Value("${board.page.count}") int PAGECNT;

    //127.0.0.1:9090/cluver/api/qna/insert
    @RequestMapping(value = "/insert", 
        method = {RequestMethod.POST},
        consumes = {MediaType.ALL_VALUE},
        produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, Object> InsertPost(
        @ModelAttribute Qna qna,
        @RequestHeader (name = "token") String token ) {

        Map<String ,Object> map = new HashMap<>();

        try{
            // ?????? ??????
            String userid = jwtUtil.extractUsername(token);
            System.out.println("USERNAME ==>" + userid);

            Member memberEntity = new Member();
            memberEntity.setMid(userid);
            System.out.println(memberEntity);

            qna.setMember(memberEntity);
            System.out.println(qna.toString());

            if(token !=null) {
                qRepository.save(qna);
                                
                map.put("status", 200); // ??????
                map.put("result", "????????????");
            }
        }
        catch(Exception e){
            e.printStackTrace();
            map.put("status", 0); // ??????
        }
        return map;
    }

    // ??? 1??? ??????
    // 127.0.0.1:9090/cluver/api/qna/delete
    // {"bno":3}
    @RequestMapping(value = "/delete", method = {RequestMethod.DELETE}, consumes = {MediaType.ALL_VALUE},
                    produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, Object> qnaDeletePost(@RequestParam(name = "qno") Long qno,
                                            @RequestHeader (name = "token")String token){
        // ?????? ?????? ????????? ???. ????????? ??????. er??????????????? ?????? ???

        Map<String ,Object> map = new HashMap<>();

        try{
            // ?????? ??????
            String userid = jwtUtil.extractUsername(token);
            System.out.println("USERNAME ==>" + userid);

            qRepository.deleteByMember_midAndQno(userid, qno);
            map.put("status", 200);
           
        }
        catch(Exception e){
            e.printStackTrace();
            map.put("status", 0); // ??????
        }
        return map;
    }


    // ckeditor?????? ???????????? ????????? ???????????? ???
    // 127.0.0.1:9090/cluver/api/qna/ckimage
    @RequestMapping(value = "/ckimage", 
        method = {RequestMethod.POST},
        consumes = {MediaType.ALL_VALUE},
        produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, Object> ckImagePOST(
      @RequestParam(name = "image") MultipartFile file ) {

        System.out.println(file.getOriginalFilename());

        Map<String ,Object> map = new HashMap<>();

        try{
            Qckeditor qckeditor = new Qckeditor();
            if(file.isEmpty() == false) {
                qckeditor.setQcimage(file.getBytes()); // ?????????
                qckeditor.setQcimagename(file.getOriginalFilename()); // ?????????
                qckeditor.setQcimagesize(file.getSize()); //?????????
                qckeditor.setQcimagetype(file.getContentType());// ??????
            }

            qcRepository.save(qckeditor);

            map.put("status", 200);
            map.put("url", "/cluver/api/qna/image?qimgcode=" + qckeditor.getQcimgcode() ); // url ?????????

        }
        catch(Exception e){
            e.printStackTrace();
            map.put("status", 0); // ??????
        }
        return map;
    }

    // 127.0.0.1:9090/cluver/api/qna/image?qimgcode=1
    // <img th:src="@{/qna/image(qimgcode=1)}" style="width:100px" />
    @RequestMapping(value = "/image", 
        method = {RequestMethod.GET},
        consumes = {MediaType.ALL_VALUE},
        produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<byte[]> imageGET(
      @RequestParam(name = "qimgcode") Long qimgcode ) throws IOException  {

        Qckeditor qckeditor = qcRepository.getById(qimgcode);


        // ????????????, ???????????????, ???????????????, ??????????????????

        if (qckeditor.getQcimagesize() > 0) {
			HttpHeaders headers = new HttpHeaders();
			if (qckeditor.getQcimagetype().equals("image/jpeg")) {
				headers.setContentType(MediaType.IMAGE_JPEG);
			} else if (qckeditor.getQcimagetype().equals("image/png")) {
				headers.setContentType(MediaType.IMAGE_PNG);
			} else if (qckeditor.getQcimagetype().equals("image/gif")) {
				headers.setContentType(MediaType.IMAGE_GIF);
			}
			ResponseEntity<byte[]> response = new ResponseEntity<>(qckeditor.getQcimage(), headers, HttpStatus.OK);
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

    // 1?????????
    // 127.0.0.1:9090/cluver/api/qna/selectone?qno=2
    @RequestMapping(value = "/selectone", method = {RequestMethod.GET}, consumes = {MediaType.ALL_VALUE},
                    produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, Object> boardSelectOneGET(
        @RequestParam(name = "qno") long qno,
        @RequestHeader (name = "token") String token ){

        Map<String ,Object> map = new HashMap<>();
        try{
            if(token != null){
                Qna qna = qRepository.findById(qno).orElse(null);

                QnaMemberView qmv = qmvRep.findById(qno).orElse(null);

                if(qna != null){
                    map.put("status", 200); // ?????? 
                    map.put("result", qna);
                    map.put("nick", qmv);
                }
            }
               
        }
        catch(Exception e){
            e.printStackTrace();
            map.put("status", 0); // ??????
        }
        return map;
       
    }

    // ????????? ??????(????????????????????? ??????, ??????x)
    // 127.0.0.1:9090/cluver/api/qna/selectlist?page=1
    @RequestMapping(value = "/selectlist", method = {RequestMethod.GET}, consumes = {MediaType.ALL_VALUE},
                    produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, Object> boardSelectListGET(
        @RequestParam(name = "page") int page ){

        Map<String ,Object> map = new HashMap<>();
        try{
            PageRequest pageRequest = PageRequest.of(page-1, PAGECNT);
            long total = qRepository.count();
            List<Qna> qList = qRepository.findAllByOrderByQnoDesc(pageRequest);
            // List<MemberPersonal> mplist = new ArrayList<>(); // ????????? ?????????
            List<QnaMemberView> qmvlist = new ArrayList<>();
            for(int i=0; i<qList.toArray().length; i++)
            {
            	// Member member = qList.get(i).getMember();
            	// MemberPersonal mp = pmRep.findByMember_mid(member.getMid());
            	// mplist.add(mp);

                QnaMemberView qmv = qmvRep.findById(qList.get(i).getQno()).orElse(null);
                System.out.println(qmv);
                qmvlist.add(qmv);
            }
            
            // System.out.println(qList);
            map.put("status", 200); // ??????
            map.put("result", qList);
            map.put("result1", total);
            map.put("mlist", qmvlist);

        }
        catch(Exception e){
            e.printStackTrace();
            map.put("status", 0); // ??????
        }
        return map;
    } 

    // ????????? ????????? 1?????? ??????
    // 127.0.0.1:9090/cluver/api/qna/updatehit?qno=2
    @RequestMapping(value = "/updatehit", method = {RequestMethod.PUT}, consumes = {MediaType.ALL_VALUE},
                    produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, Object> boardUpdateHitGET(
        @RequestParam(name = "qno") long bno,
        @RequestHeader (name = "token")String token){

        Map<String ,Object> map = new HashMap<>();
        try{
            if(token != null){
                Qna qna = qRepository.findById(bno).orElse(null);
                qna.setQhit( qna.getQhit() + 1L );
                qRepository.save(qna);
                
                map.put("status", 200); // ??????
                
            }
        }
        catch(Exception e){
            e.printStackTrace();
            map.put("status", 0); // ??????
        }
        return map;
    }

    // ?????? + ??????????????????
    // 127.0.0.1:9090/cluver/api/qna/search
    @RequestMapping(value = "/search", method = {RequestMethod.GET}, consumes = {MediaType.ALL_VALUE},
                    produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, Object> searchGET(
        @RequestParam(name = "page") int page,
        @RequestParam(name = "btn") String btn,
        @RequestParam(name = "qtitle", defaultValue = "") String qtitle,
        @RequestParam(name = "qcontent", defaultValue = "") String qcontent,
        @RequestParam(name = "mid", defaultValue = "") String mid 
        ){

        // System.out.println(qtitle);

        Map<String ,Object> map = new HashMap<>();
        try{
            if(btn.equals("??????")){
                // ??????
                PageRequest pageRequest = PageRequest.of(page-1, PAGECNT);
                List<Qna> qList = qRepository.findByQtitleContainingOrderByQnoDesc(qtitle, pageRequest);
                map.put("status", 200); // ??????
                map.put("result", qList);
            }

            else if(btn.equals("??????")){
                // ??????
                PageRequest pageRequest1 = PageRequest.of(page-1, PAGECNT);
                List<Qna> q1List = qRepository.findByQcontentContainingOrderByQnoDesc(qcontent, pageRequest1);
                // System.out.println(q1List);
                map.put("status", 200); // ??????
                map.put("result", q1List);
            }

            else if(btn.equals("?????????")){
                // ?????????
                PageRequest pageRequest2 = PageRequest.of(page-1, PAGECNT);
                List<Qna> q2List = qRepository.findByMember_midContainingOrderByQnoDesc(mid, pageRequest2);
                // System.out.println(q2List);
                map.put("status", 200); // ??????
                map.put("result", q2List);
            }

            else if(btn.equals("??????")){
                // ??????
                PageRequest pageRequest3 = PageRequest.of(page-1, PAGECNT);
                List<Qna> q3List = qRepository.findByQcontentOrMember_midOrQtitleContainingOrderByQnoDesc(qtitle, qcontent, mid, pageRequest3);
                // System.out.println(q3List);
                map.put("status", 200); // ??????
                map.put("result", q3List);
            }

        }
        catch(Exception e){
            e.printStackTrace();
            map.put("status", 0); // ??????
        }
        return map;
    }

    // ????????? ???????????? ????????? ???????????? ???????????? ?????? ??????
    // 127.0.0.1:9090/cluver/api/qna/selectboard
    @RequestMapping(value = "/selectboard", method = {RequestMethod.GET}, consumes = {MediaType.ALL_VALUE},
                    produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, Object> selectBoardGET(
        @RequestParam(name = "page") int page, 
        @RequestHeader (name = "token") String token ){

        Map<String ,Object> map = new HashMap<>();
        try{
            // ?????? ??????
            String userid = jwtUtil.extractUsername(token);
            System.out.println("USERNAME ==>" + userid);

            PageRequest pageRequest = PageRequest.of(page-1, PAGECNT);
            long total = qRepository.countByMember_mid(userid);

            List<Qna> qList = qRepository.findByMember_midOrderByQnoDesc(userid, pageRequest);
            // System.out.println(qList);
            map.put("status", 200); // ??????
            map.put("result", qList);
            map.put("result1", total);

        }
        catch(Exception e){
            e.printStackTrace();
            map.put("status", 0); // ??????
        }
        return map;
    } 

    
}
