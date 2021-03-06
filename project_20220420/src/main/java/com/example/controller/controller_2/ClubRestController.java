package com.example.controller.controller_2;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.entity.entity1.CDCkeditor;
import com.example.entity.entity1.ClubDetail;
import com.example.entity.entity1.Like;
import com.example.entity.entity1.Member;
import com.example.entity.entity2.Address;
import com.example.entity.entity2.Category;
import com.example.entity.entity2.Cimage;
import com.example.entity.entity2.Club;
import com.example.entity.entity2.ClubProjection;
import com.example.entity.entity2.Clublistview;
import com.example.entity.entity2.Combineaddr;
import com.example.entity.entity2.Membermid;
import com.example.jwt.JwtUtil;
import com.example.repository.MemberRepository;
import com.example.repository.repository_4.CDCkeditorRepository;
import com.example.repository.repository_4.ClubDetailRepository;
import com.example.repository.repository_4.LikeRepository;
import com.example.repository.repository_gibum.CategoryRepository;
import com.example.repository.repository_gibum.CimageRepository;
import com.example.repository.repository_gibum.ClubListViewRepository;
import com.example.repository.repository_gibum.ClubRepository;
import com.example.repository.repository_gibum.CombineaddrViewRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/club")
public class ClubRestController {
    @Autowired ClubRepository cRepository;

    @Autowired JwtUtil jwtUtil;

    @Autowired CimageRepository ciRepository;

    @Autowired
    LikeRepository lRepository;

    @Autowired
    ClubDetailRepository cdRepository;

    @Autowired
    CDCkeditorRepository ccRepository;

    @Autowired 
    ResourceLoader resLoader;

    @Autowired
    CombineaddrViewRepository addrRepository;

    @Autowired CategoryRepository cgRepository;

    @Autowired ClubListViewRepository clubListViewRepository;

    @Value("${default.image}") String DEFAULT_IMAGE;


    // 127.0.0.1:9090/cluver/club/image?cno=179
	@GetMapping(value ="/cimage")
    public ResponseEntity<byte[]> imageGET(
        @RequestParam(name ="cno") Long cno) throws IOException {
            // ????????????, ???????????????, ???????????????, ??????????????????
			Cimage cimage = ciRepository.findByClub_Cno(cno);

            if(cimage != null){ // ??????????????? ????????????
                if(cimage.getCimagesize() > 0) { // ????????? ?????? ??????
                    HttpHeaders headers = new HttpHeaders();
                    
                    if(cimage.getCimagetype().equals("image/jpeg")){
                        headers.setContentType(MediaType.IMAGE_JPEG);
                    }

                    else if(cimage.getCimagetype().equals("image/png")){
                        headers.setContentType(MediaType.IMAGE_PNG);
                    }

                    else if(cimage.getCimagetype().equals("image/gif")){
                        headers.setContentType(MediaType.IMAGE_GIF);
                    }

                    // ????????? byte[], headers, HttpStatus.OK
                    ResponseEntity<byte[]> response 
                    = new ResponseEntity<>(cimage.getCimage(),
                        headers, HttpStatus.OK);
                        return response;
                }
                // else {
                //         InputStream is =
                //         resLoader
                //         .getResource("classpath:/static/img/default.png")
                //         .getInputStream();

                //     HttpHeaders headers = new HttpHeaders();
                //     headers.setContentType(MediaType.IMAGE_PNG);

                //     ResponseEntity<byte[]> response
                //     = new ResponseEntity<>(is.readAllBytes(),
                //     headers, HttpStatus.OK);

                //     return response;
                // }
            }
            return null;
        }

    
    
    

    // cimage  ????????? ???????????? ???
    // 127.0.0.1:9090/cluver/club/cbimage
    @RequestMapping(value = "/cbimage", 
        method = {RequestMethod.POST},
        consumes = {MediaType.ALL_VALUE},
        produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, Object> ckImagePOST(
        @ModelAttribute Club club,
        @RequestParam(name = "file") MultipartFile file ) {
        System.out.println(file.getOriginalFilename());

        Map<String ,Object> map = new HashMap<>();

        try{
            Cimage cimage = new Cimage();
            if(file.isEmpty() == false) {
                cimage.setCimage(file.getBytes()); // ?????????
                cimage.setCimagename(file.getOriginalFilename()); // ?????????
                cimage.setCimagesize(file.getSize()); //?????????
                cimage.setCimagetype(file.getContentType());// ??????
            }

                cimage.setClub(cRepository.findByCno(club.getCno()));
                System.out.println(cimage.getClub());
                ciRepository.save(cimage);

            map.put("status", 200);
            map.put("url", "/cluver/club/cimage?cno=" + cimage.getClub().getCno() ); // url ?????????

        }
        catch(Exception e){
            e.printStackTrace();
            map.put("status", 0); // ??????
        }
        return map;
    }

    // ????????????
	// 127.0.0.1:9090/cluver/club/insert.json
	//{"mid":"c1", "mpw":"c1" };
	@RequestMapping(value = "/insert.json", 
    method = { RequestMethod.POST },
    consumes = { MediaType.ALL_VALUE },
    produces = { MediaType.APPLICATION_JSON_VALUE })
public Map<String, Object> JoinClubpost(
    @ModelAttribute Club club,
    @RequestParam(name = "file",required = false) MultipartFile file,
    @RequestHeader(name = "TOKEN") String token){
    System.out.println(club);
Map<String, Object> map = new HashMap<>();
try {
    String username = jwtUtil.extractUsername(token);
    System.out.println(username);
    System.out.println(file);
    // if(
//     file != null){
//     if(!file.isEmpty()){
//         club.setCthumbnail(file.getBytes());
//         club.setCimagesize(file.getSize());
//         club.setCimagetype(file.getContentType());
//         club.setCimagename(file.getOriginalFilename());
        
//     }
// }
    // Address address = new Address();
    // address.setAcode((Long) club.getAddress().getAcode());

    // Category category = new Category();
    // category.setCgcode((Long) club.getCategory().getCgcode());
    
    cRepository.save(club);
    map.put("status", 200);
    map.put("result", club);
    map.put("mid", username);
    }
    catch (Exception e) {
        e.printStackTrace();
        map.put("status", 0);
    }
    
    return map;
}
	// ???????????? ????????????
	// 127.0.0.1:9090/cluver/club/cnamecheck
	@PostMapping(value = "/cnamecheck", 
	consumes = MediaType.ALL_VALUE, 
	produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> CnameCheckPost(
		@RequestParam(value = "cname") String cname){
		Map<String, Object> map = new HashMap<>();
		try {
			System.out.println(cname);
			Club club = cRepository.findByCname(cname);
			if(club == null){
				System.out.println("???????????? ?????????");	
				map.put("status", 200);
			}
		}
            catch (Exception e) {
                e.printStackTrace();
                map.put("status", 0);
            }
            
            return map;
        }

    // ?????? ?????????1?????????(??????????????? ?????? ????????????)
	// 127.0.0.1:9090/cluver/club/selectone?cno=139
	@RequestMapping(value = "/selectone", 
			method = { RequestMethod.GET },
			consumes = { MediaType.ALL_VALUE },
			produces = { MediaType.APPLICATION_JSON_VALUE })
	public Map<String, Object> stomerMypageGet(
		@RequestParam(value = "cno")long cno){
            Map<String, Object> map = new HashMap<>();
            try {
                
                Club club = cRepository.findByCno(cno);
                Cimage cimage = ciRepository.findByClub_Cno(cno);
                System.out.println(cimage);
                if(cimage != null)
                {
                	club.setCimageurl("/cluver/club/cimage?cno=" +club.getCno());
                }
                System.out.println(club);
                map.put("status", 200); 
                map.put("result", club); 
            }
            catch (Exception e) {
                e.printStackTrace();
                map.put("status", 0);
            }

		return map;
	}
    // ?????? cname cno ??????
	// 127.0.0.1:9090/cluver/club/cnamesearch
	@RequestMapping(value = "/cnamesearch", 
			method = { RequestMethod.GET },
			consumes = { MediaType.ALL_VALUE },
			produces = { MediaType.APPLICATION_JSON_VALUE })
	public Map<String, Object> CnameSearchGet(
		@RequestParam(value = "cname")String cname){
            Map<String, Object> map = new HashMap<>();
            try {
                
                Club club = cRepository.findByCname(cname);
                System.out.println(club);
                map.put("status", 200); 
                map.put("result", club); 
            }
            catch (Exception e) {
                e.printStackTrace();
                map.put("status", 0);
            }

		return map;
	}
    // ?????? cno??? cname ??????
	// 127.0.0.1:9090/cluver/club/cnosearch
	@RequestMapping(value = "/cnosearch", 
			method = { RequestMethod.GET },
			consumes = { MediaType.ALL_VALUE },
			produces = { MediaType.APPLICATION_JSON_VALUE })
	public Map<String, Object> CnoSearchGet(
		@RequestParam(value = "cno")long cno){
            Map<String, Object> map = new HashMap<>();
            try {
                
                Club club = cRepository.findByCno(cno);
                System.out.println(club);
                map.put("status", 200); 
                map.put("result", club); 
            }
            catch (Exception e) {
                e.printStackTrace();
                map.put("status", 0);
            }

		return map;
	}

    // ????????? ?????????????????????
	// 127.0.0.1:9090/cluver/club/selectlist1
	@RequestMapping(value = "/selectlist1", 
			method = { RequestMethod.GET },
			consumes = { MediaType.ALL_VALUE },
			produces = { MediaType.APPLICATION_JSON_VALUE })
	public Map<String, Object> clubimagelist1Get(){
            Map<String, Object> map = new HashMap<>();
            try {
                
                    map.put("status", 200); 
            }
            catch (Exception e) {
                e.printStackTrace();
                map.put("status", 0);
            }

		return map;
	}
    // ??????????????? ??????()
	// 127.0.0.1:9090/cluver/club/selectlist2
	@RequestMapping(value = "/selectlist2", 
			method = { RequestMethod.GET },
			consumes = { MediaType.ALL_VALUE },
			produces = { MediaType.APPLICATION_JSON_VALUE })
	public Map<String, Object> ssGet1(){
            Map<String, Object> map = new HashMap<>();
            try {
                String private12 = "??????";

                List<Clublistview> club = clubListViewRepository.findByCprivateOrderByCnoDesc(private12);
                // club.setCimageurl("/cluver/club/cimage?cno=" +club.getCno());
                // List<ClubProjection> cp = cRepository.find
                List<Map <String, Object>> list = new ArrayList<>();
                for(Clublistview obj:club  ){
                    Map <String, Object> map1 = new HashMap<>();
                    map1.put("obj", obj);
                    Cimage cimage = ciRepository.findByClub_Cno(obj.getCno());

                    if(cimage != null){

                        map1.put("imgurl","/cluver/club/cimage?cno=" +obj.getCno());
                    }
                    else{
                        map1.put("imgurl",null);

                    }
                    list.add(map1);
                }


                // System.out.println(club);
		        // club.set ("/cluver/member/image?mid=" +username);
                map.put("status", 200); 
                map.put("result", list); 
                map.put("??????", club.size()); 
            }
            catch (Exception e) {
                e.printStackTrace();
                map.put("status", 0);
            }

		return map;
	}
    // ??????????????? ??????()
	// 127.0.0.1:9090/cluver/club/selectlist
	@RequestMapping(value = "/selectlist", 
			method = { RequestMethod.GET },
			consumes = { MediaType.ALL_VALUE },
			produces = { MediaType.APPLICATION_JSON_VALUE })
	public Map<String, Object> ssGet(){
            Map<String, Object> map = new HashMap<>();
            try {
                String private12 = "??????";

                List<ClubProjection> club = cRepository.findByCprivateOrderByCnoDesc(private12);
                // club.setCimageurl("/cluver/club/cimage?cno=" +club.getCno());
                // List<ClubProjection> cp = cRepository.find
                List<Map <String, Object>> list = new ArrayList<>();
                for(ClubProjection obj:club  ){
                    Map <String, Object> map1 = new HashMap<>();
                    map1.put("obj", obj);
                    Cimage cimage = ciRepository.findByClub_Cno(obj.getCno());

                    if(cimage != null){

                        map1.put("imgurl","/cluver/club/cimage?cno=" +obj.getCno());
                    }
                    else{
                        map1.put("imgurl",null);

                    }
                    list.add(map1);
                }


                // System.out.println(club);
		        // club.set ("/cluver/member/image?mid=" +username);
                map.put("status", 200); 
                map.put("result", list); 
                map.put("??????", club.size()); 
            }
            catch (Exception e) {
                e.printStackTrace();
                map.put("status", 0);
            }

		return map;
	}
    // ?????? ?????? ??????????????? ??????()
	// 127.0.0.1:9090/cluver/club/likelist
	@RequestMapping(value = "/likelist", 
			method = { RequestMethod.GET },
			consumes = { MediaType.ALL_VALUE },
			produces = { MediaType.APPLICATION_JSON_VALUE })
	public Map<String, Object> likelist(
        Like like,
        @RequestHeader (name = "token") String token){
            Map<String, Object> map = new HashMap<>();
            try {

                String userid = jwtUtil.extractUsername(token);
                System.out.println("USERNAME ==>" + userid);

                Member memberEntity = new Member();
                memberEntity.setMid(userid);
                System.out.println(memberEntity);

                like.setMember(memberEntity);
                System.out.println(like.toString());
                List<Like> like1 = lRepository.findByMember_mid(userid);
                List<Map <String, Object>> list = new ArrayList<>();
                for(Like obj:like1  ){
                    Map <String, Object> map1 = new HashMap<>();
                    map1.put("obj", obj);
                    map1.put("imgurl","/cluver/club/cimage?cno=" +obj.getClub().getCno());
                    Cimage cimage = ciRepository.findByClub_Cno(obj.getClub().getCno());

                    if(cimage != null){

                        map1.put("imgurl","/cluver/club/cimage?cno=" +obj.getClub().getCno());
                    }
                    else{
                        map1.put("imgurl",null);

                    }
                    list.add(map1);
                }
                map.put("status", 200); 
                map.put("result", list); 
                map.put("??????", like1.size()); 
            }
            catch (Exception e) {
                e.printStackTrace();
                map.put("status", 0);
            }
            
            return map;
            
	}

    
    //  ??????????????? ???????????? ex ?????????)??????
    // 127.0.0.1:9090/cluver/club/searchcateclub1
    @RequestMapping(value = "/searchcateclub1", 
    method = { RequestMethod.GET },
    consumes = { MediaType.ALL_VALUE },
    produces = { MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> CateSearch1Get(
        @Param(value = "cate") String cate
        ){
        Map<String, Object> map = new HashMap<>();
        map.put("status", 0);
        try {
            String private1 = "??????";
            // List<Combineaddr> combineaddr = addrRepository.findByCprivateAndCgcate1Containing(private1,cate);
            List<Combineaddr> combineaddr1 = addrRepository.findByCprivateAndCgcate2Containing(private1,cate);
            // System.out.println("======1"+ combineaddr);
            System.out.println("======2"+ combineaddr1);
            

            map.put("status", 200); 
            // map.put("result", combineaddr); 
            map.put("result", combineaddr1); 
        }
         catch (Exception e) {
            e.printStackTrace();
        }

    return map;
    }   

    //  ?????????????????? ?????? ex)??????
    // 127.0.0.1:9090/cluver/club/searchcateclub
    @RequestMapping(value = "/searchcateclub", 
    method = { RequestMethod.GET },
    consumes = { MediaType.ALL_VALUE },
    produces = { MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> CateclubGet(
        @Param(value = "cate") String cate
        ){
        Map<String, Object> map = new HashMap<>();
        map.put("status", 0);
        try {
            
            String private1 = "??????";

            List<Clublistview> club = clubListViewRepository.findByCprivateAndCgcate1OrCgcate2(private1,cate,cate);
            System.out.println(club);
            List<Map <String, Object>> list = new ArrayList<>();
            for(Clublistview obj:club  ){
                Map <String, Object> map1 = new HashMap<>();
                map1.put("obj", obj);
                Cimage cimage = ciRepository.findByClub_Cno(obj.getCno());

                if(cimage != null){

                    map1.put("imgurl","/cluver/club/cimage?cno=" +obj.getCno());
                }
                else{
                    map1.put("imgurl",null);

                }
                list.add(map1);
            }


            // System.out.println(club);
            // club.set ("/cluver/member/image?mid=" +username);
            map.put("status", 200); 
            map.put("result", list); 
            map.put("??????", club.size()); 
        }
         catch (Exception e) {
            e.printStackTrace();
        }

    return map;
    }    
    
    //  ?????????????????? ex??????
    // 127.0.0.1:9090/cluver/club/searchclub
    @RequestMapping(value = "/searchclub", 
    method = { RequestMethod.GET },
    consumes = { MediaType.ALL_VALUE },
    produces = { MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> addressclubGet(
        @Param(value = "address") String address) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", 0);
        try {
            String private1 = "??????";

            List<Clublistview> club = clubListViewRepository.findByCprivateAndCaddressContaining(private1,address);
            System.out.println(club);
            List<Map <String, Object>> list = new ArrayList<>();
            for(Clublistview obj:club  ){
                Map <String, Object> map1 = new HashMap<>();
                map1.put("obj", obj);
                Cimage cimage = ciRepository.findByClub_Cno(obj.getCno());

                if(cimage != null) {
                    map1.put("imgurl","/cluver/club/cimage?cno=" +obj.getCno());
                }else {
                    map1.put("imgurl",null);
                }
                list.add(map1);
            }
            map.put("status", 200); 
            map.put("result", list); 
            map.put("??????", club.size()); 
        }catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

        //  ?????????????????? ex??????
    // 127.0.0.1:9090/cluver/club/searchclub
    // @RequestMapping(value = "/searchclub1", 
    // method = { RequestMethod.GET },
    // consumes = { MediaType.ALL_VALUE },
    // produces = { MediaType.APPLICATION_JSON_VALUE })
    // public Map<String, Object> addressclub1Get(
    //     @Param(value = "address1") String address) {
    //     Map<String, Object> map = new HashMap<>();
    //     map.put("status", 0);
    //     try {
    //         String private1 = "??????";

    //         List<Clublistview> club = clubListViewRepository.findByCprivateInCaddressContaining(private1, address);
    //         System.out.println(club);
    //         List<Map <String, Object>> list = new ArrayList<>();
    //         for(Clublistview obj: club){
    //             Map <String, Object> map1 = new HashMap<>();
    //             map1.put("obj", obj);
    //             Cimage cimage = ciRepository.findByClub_Cno(obj.getCno());

    //             if(cimage != null) {
    //                 map1.put("imgurl","/cluver/club/cimage?cno=" +obj.getCno());
    //             }else {
    //                 map1.put("imgurl",null);
    //             }
    //             list.add(map1);
    //         }
    //         map.put("status", 200); 
    //         map.put("result", list); 
    //         map.put("??????", club.size()); 
    //     }catch (Exception e) {
    //         e.printStackTrace();
    //     }
    //     return map;
    // }

    //  ???????????????(????????????)
    // 127.0.0.1:9090/cluver/club/busanclublist
    @RequestMapping(value = "/busanclublist", 
    method = { RequestMethod.GET },
    consumes = { MediaType.ALL_VALUE },
    produces = { MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> addressclublistGet(){
        Map<String, Object> map = new HashMap<>();
        map.put("status", 0);
        try {
            String private1 = "??????";
            String address = "??????";

            List<ClubProjection> club = cRepository.findByCprivateAndCaddressContaining(private1,address);
            System.out.println(club);
            List<Map <String, Object>> list = new ArrayList<>();
            for(ClubProjection obj:club  ){
                Map <String, Object> map1 = new HashMap<>();
                map1.put("obj", obj);
                map1.put("imgurl","/cluver/club/cimage?cno=" +obj.getCno());
                list.add(map1);
            }


            // System.out.println(club);
            // club.set ("/cluver/member/image?mid=" +username);
            map.put("status", 200); 
            map.put("result", list); 
            map.put("??????", club.size()); 
        }
         catch (Exception e) {
            e.printStackTrace();
        }

    return map;
    }   

    // ?????? ???????????? ?????????
    // 127.0.0.1:9090/cluver/club/noticeinsert
    @RequestMapping(value = "/noticeinsert", 
    method = {RequestMethod.POST},
    consumes = {MediaType.ALL_VALUE},
    produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, Object> InsertPost(
    @ModelAttribute ClubDetail clubDetail,
    @RequestHeader (name = "token") String token ) {

        Map<String ,Object> map = new HashMap<>();

        try{
            if(token !=null) {
                cdRepository.save(clubDetail);
                System.out.println(clubDetail.toString());
            
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

    // ???????????? - ckeditor?????? ???????????? ????????? ???????????? ???
    // 127.0.0.1:9090/cluver/club/ckimage
      @RequestMapping(value = "/ckimage", 
      method = {RequestMethod.POST},
      consumes = {MediaType.ALL_VALUE},
      produces = {MediaType.APPLICATION_JSON_VALUE})
  public Map<String, Object> ckImagePOST(
    @RequestParam(name = "image") MultipartFile file ) {

      System.out.println(file.getOriginalFilename());

      Map<String ,Object> map = new HashMap<>();

      try{
            CDCkeditor cdckeditor = new CDCkeditor();
            if(file.isEmpty() == false) {
                cdckeditor.setCdimage(file.getBytes()); // ?????????
                cdckeditor.setCdimagename(file.getOriginalFilename()); // ?????????
                cdckeditor.setCdimagesize(file.getSize()); //?????????
                cdckeditor.setCdimagetype(file.getContentType());// ??????
            }

            ccRepository.save(cdckeditor);

          map.put("status", 200);
          map.put("url", "/cluver/club/cnimage?cdimgcode=" + cdckeditor.getCdimgcode() ); // url ?????????

      }
      catch(Exception e){
          e.printStackTrace();
          map.put("status", 0); // ??????
      }
      return map;
    }

    // 127.0.0.1:9090/cluver/club/cnimage?cdimgcode=1
    // <img th:src="@{/club/cnimage(cdimgcode=1)}" style="width:100px" />
    @RequestMapping(value = "/cnimage", 
        method = {RequestMethod.GET},
        consumes = {MediaType.ALL_VALUE},
        produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<byte[]> cnimageGET(
      @RequestParam(name = "cdimgcode") Long cdimgcode ) throws IOException  {

        CDCkeditor cdckeditor = ccRepository.getById(cdimgcode);

        if (cdckeditor.getCdimagesize() > 0) {
			HttpHeaders headers = new HttpHeaders();
			if (cdckeditor.getCdimagetype().equals("image/jpeg")) {
				headers.setContentType(MediaType.IMAGE_JPEG);
			} else if (cdckeditor.getCdimagetype().equals("image/png")) {
				headers.setContentType(MediaType.IMAGE_PNG);
			} else if (cdckeditor.getCdimagetype().equals("image/gif")) {
				headers.setContentType(MediaType.IMAGE_GIF);
			}
			ResponseEntity<byte[]> response = new ResponseEntity<>(cdckeditor.getCdimage(), headers, HttpStatus.OK);
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




    
}















