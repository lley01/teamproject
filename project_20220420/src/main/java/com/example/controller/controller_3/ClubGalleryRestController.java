package com.example.controller.controller_3;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.entity.entity1.CGMemView;
import com.example.entity.entity1.ClubGallery;
import com.example.entity.entity1.GImage;
import com.example.entity.entity1.Member;
import com.example.entity.entity1.MemberPersonal;
import com.example.entity.entity2.CReply;
import com.example.entity.entity2.CReplyMemberView;
import com.example.entity.entity2.Club;
import com.example.entity.entity2.ClubBoard;
import com.example.jwt.JwtUtil;
import com.example.repository.repository_3.CGMemViewRepository;
import com.example.repository.repository_3.CReplyMemberViewRepository;
import com.example.repository.repository_3.CReplyRepository;
import com.example.repository.repository_3.ClubGalleryImageRepository;
import com.example.repository.repository_3.ClubGalleryRepository;
import com.example.repository.repository_3.PersonalMemberRepository;

@RestController
@RequestMapping(value="/api/clubgallery")
public class ClubGalleryRestController {
	@Autowired
	ClubGalleryRepository cgRep;
	
	@Autowired
	ClubGalleryImageRepository cgiRep;
	
	@Autowired
	CReplyRepository crRep;
	
	@Autowired
	PersonalMemberRepository pmRep;
	
	@Autowired
	CGMemViewRepository cgmvRep;
	
	@Autowired
	CReplyMemberViewRepository crmvRep;
	
	@Autowired
	ResourceLoader resLoader;
	
	@Autowired
	JwtUtil jwtUtil;
	
	// 클럽갤러리 생성
	// /cluver/api/clubgallery/insert
	@RequestMapping(value="/insert", 
			method={RequestMethod.POST}, 
			consumes = {MediaType.ALL_VALUE},
			produces= {MediaType.APPLICATION_JSON_VALUE})
	public Map<String, Object> insertPOST(@ModelAttribute ClubGallery cg, @ModelAttribute MultipartFile[] file,
			@RequestHeader(name="token") String token) throws IOException
	{
		Map<String, Object> map = new HashMap<>();
		try 
		{
			if(token != null)
			{
				System.out.println("cg : " + cg);
				System.out.println(file.length);
				
				Member mid = new Member(); 
				mid.setMid(jwtUtil.extractUsername(token));
				cg.setMember(mid);
				
				if(file != null) 
				{
					if(file.length > 0)// 이미지파일 첨부시
					{
						cgRep.save(cg);
						for(int i=0; i<file.length; i++)
						{
							GImage gImage = new GImage();
							System.out.println("file[i] : " + file[i].getContentType());
//							cg.setGThumbnail(file[0].getBytes());
							gImage.setGimage(file[i].getBytes());
							gImage.setGimagename(file[i].getOriginalFilename());
							gImage.setGimagesize(file[i].getSize());
							gImage.setGimagetype(file[i].getContentType());
							gImage.setClubgallery(cg);
							cgiRep.save(gImage);
							map.put("status", 200);
						}	
					}
					else
					{
						map.put("status", 1);
						map.put("result", "이미지없음");
					}
				}
			}
			else
			{
				map.put("status", 0);
				map.put("result", "토큰없음");
			}
		} 
		catch (Exception e) 
		{
			map.put("status", -1);
		}
		return map;
	}
	
	// 클럽갤러리 목록
	// /cluver/api/clubgallery/selectlist?page=&text=&option=&cno=
	@RequestMapping(value="/selectlist", 
			method={RequestMethod.GET}, 
			consumes = {MediaType.ALL_VALUE},
			produces= {MediaType.APPLICATION_JSON_VALUE})
	public Map<String, Object> selectlistGET(Model model, 
			@RequestParam(name="page", defaultValue="1") int page, 
			@RequestParam(name="text", defaultValue="", required=false) String text,
			@RequestParam(name="option", defaultValue="", required=false) String option,
			@RequestParam(name="cno") long cno, 
			@RequestHeader(name="token") String token)
	{
		Map<String, Object> map = new HashMap<>();
		try 
		{
			if(token != null)
			{
				//1페이지 당 10글 표시
				PageRequest pageRequest = PageRequest.of(page-1, 9); 
				System.out.println(pageRequest);
				
				//검색어 포함, 1페이지 10글, 글번호 내림차순
				List<ClubGallery> list = new ArrayList<>();
				if(option.equals("갤러리명"))
				{
					list = cgRep.findByCgnameContainingAndClub_cnoOrderByCgnoDesc(text, cno, pageRequest);
				}
				else if(option.equals("갤러리설명"))
				{
					list = cgRep.findByCgdescContainingAndClub_cnoOrderByCgnoDesc(text, cno, pageRequest);
				}
				else if(option.equals("갤러리생성자"))
				{
					list = cgRep.findByMember_midContainingAndClub_cnoOrderByCgnoDesc(text, cno, pageRequest);
				}
//				else if(option.equals("전체"))
//				{
//					list = cgRep.findByAllOptions(text, pageRequest);
//				}
				else
				{
					list = cgRep.findByClub_cnoOrderByCgnoDesc(cno, pageRequest);
				}
				
//				System.out.println("list : " + list);
				
				//페이지 구현용 글 개수
				long total = cgRep.countByClub_cno(cno);
//				System.out.println("total = " + total);
				model.addAttribute("total", total);
				
				// pages = 1~9 = 1, 10~18 = 2, 19~27 = 3, ...... // 한 페이지에 9개 갤러리
//				System.out.println("pages : " + (total-1) / 10 + 1);
				model.addAttribute("pages", (total-1) / 9 + 1);	
				
				List<CGMemView> mlist = new ArrayList<>(); // 닉네임 리스트
				for(int i=0; i<list.size(); i++) //list 내 모든 갤러리에 각각의 썸네일 주소를 부여 
				{
					ClubGallery cg = list.get(i);
					
					ClubGallery clubGallery = cgRep.findById(cg.getCgno()).orElse(null);
					
					clubGallery.setGimageurl("/cluver/clubgallery/image?cgno=" + cg.getCgno() + "&idx=0");
					
					CGMemView cgmv = cgmvRep.findById(cg.getCgno()).orElse(null);
					
					mlist.add(cgmv);
				}
				
				model.addAttribute("list", list);
				model.addAttribute("mlist", mlist);
				
				map.put("status", 200);
				map.put("result", model);
			}
			else
			{
				map.put("status", 0);
				map.put("result", "토큰없음");
			}
			
		} 
		catch (Exception e) 
		{
			map.put("status", -1);
		}
		return map;
	}
	// 갤러리 상세보기 + 댓글목록 + 이미지 url
	// /cluver/api/clubgallery/select?cgno=
	@RequestMapping(value="/select", 
			method={RequestMethod.GET}, 
			consumes = {MediaType.ALL_VALUE},
			produces= {MediaType.APPLICATION_JSON_VALUE})
	public Map<String, Object> insertPOST(Model model, @RequestParam(name="cgno") long cgno, @RequestParam(name="cno") long cno, @RequestHeader(name="token") String token)
	{
		Map<String, Object> map = new HashMap<>();
		try {
			if(token != null)
			{
				System.out.println(cgno);
				System.out.println(cno);
				ClubGallery clubGallery = cgRep.findByCgnoAndClub_cno(cgno, cno);
				long imagecount = cgiRep.countByClubgallery_cgno(cgno); // idx값
				clubGallery.setGimageurl("/cluver/clubgallery/image?cgno=" + cgno); // 이미지 url 보내기(idx값은 프론트에서 반복문으로 입력)
				CGMemView cgmv = cgmvRep.findById(cgno).orElse(null);
				// 댓글 목록 저장할 배열 변수
				List<CReply> replylist = new ArrayList<>();
				// 대댓글 목록 저장 변수
				List<CReply> rereplylist = new ArrayList<>();
				// 댓글 목록 저장할 배열 변수
				List<CReply> replylistlength = crRep.findByClubgallery_cgnoOrderByRenumberDesc(cgno);
				// 글번호 일치하는 댓글 대댓글 일괄 전달받기
				for(int i=0; i<replylistlength.size(); i++)
				{
					if(replylistlength.get(i).getReparentnumber() == 0)
					{
						replylist.add(replylistlength.get(i));
					}
					else
					{
						rereplylist.add(replylistlength.get(i));
					}
				}
				List<CReplyMemberView> mlist = new ArrayList<>(); // 댓글 작성자 닉네임 리스트
				List<CReplyMemberView> remlist = new ArrayList<>(); // 대댓글 작성자 닉네임 리스트
				for(int i=0; i<replylist.size(); i++)
				{
					CReplyMemberView crmv = crmvRep.findByRenumber(replylist.get(i).getRenumber());
//					System.out.println("crmv : "+crmv);
					mlist.add(crmv);
				}
				
				for(int i=0; i<rereplylist.size(); i++)
				{
					CReplyMemberView crmv1 = crmvRep.findByRenumber(rereplylist.get(i).getRenumber());
//					System.out.println("crmv1 : "+crmv1);
					remlist.add(crmv1);
				}
				
				System.out.println(cno);
				
				model.addAttribute("clubgallery", clubGallery); //글상세내용(이미지 url 포함)	
				model.addAttribute("nick", cgmv); // 글 작성자 닉네임
				model.addAttribute("replylist", replylist); // 댓글
				model.addAttribute("rereplylist", rereplylist); // 대댓글목록
				model.addAttribute("imagecount", imagecount); // 이미지 개수(idx)
				model.addAttribute("replynicklist", mlist); // 댓글작성자 닉네임 목록
				model.addAttribute("rereplynicklist", remlist); // 댓글작성자 닉네임 목록
				
				map.put("status", 200);
				map.put("result", model);
			}
			else
			{
				map.put("status", 0);
			}
		} 
		catch (Exception e) 
		{
			map.put("status", -1);
		}
		return map;
	}
	
	// 갤러리 조회수 증가
	@RequestMapping(value="/updatehit", 
			method={RequestMethod.POST}, 
			consumes = {MediaType.ALL_VALUE},
			produces= {MediaType.APPLICATION_JSON_VALUE})
	public Map<String, Object> clubgalleryUpdatehit1PUT(@RequestParam(name="cgno") long cgno, @RequestParam(name="cno") long cno, @RequestHeader(name="token") String token)
	{
		Map<String, Object> map = new HashMap<>();
		try 
		{
			if(token != null)
			{
				ClubGallery gallery = cgRep.findByCgnoAndClub_cno(cgno, cno);
				gallery.setCghit( gallery.getCghit() + 1L );
				System.out.println(gallery.toString());
				cgRep.save(gallery);
				map.put("status", 200);
			}
			else
			{
				map.put("status", 0);
				map.put("result", "토큰없음");
			}
			
		} 
		catch (Exception e) 
		{
			map.put("status", -1);
		}
		return map;
	}
	
	
	
	// 갤러리 삭제
	// /cluver/api/clubgallery/delete
	@Transactional
	@RequestMapping(value="/delete", 
	method={RequestMethod.POST}, 
	consumes = {MediaType.ALL_VALUE},
	produces= {MediaType.APPLICATION_JSON_VALUE})
	public Map<String, Object> delete(@RequestBody ClubGallery cg, @RequestHeader(name="token") String token)
	{
		Map<String, Object> map = new HashMap<>();
		try 
		{
			if(token != null)
			{
				crRep.deleteByClubgallery_cgno(cg.getCgno());
				cgiRep.deleteByClubgallery_cgno(cg.getCgno());
				cgRep.deleteById(cg.getCgno());
				map.put("status", 200);
			}
			else
			{
				map.put("status", 0);
				map.put("result", "토큰없음");
			}
			
		} 
		catch (Exception e) 
		{
			map.put("status", -1);
		}
		return map;
	}
	
	// 클럽갤러리 댓글/대댓글쓰기
	// /cluver/api/clubgallery/insertreply
	@RequestMapping(value="/insertreply", 
			method={RequestMethod.POST}, 
			consumes = {MediaType.ALL_VALUE},
			produces= {MediaType.APPLICATION_JSON_VALUE})
	public Map<String, Object> insertreplyPOST(@RequestBody Map<String, Object> cr, @RequestHeader(name="token") String token)
	{
		Map<String, Object> map = new HashMap<>();
		try 
		{
			if(token != null)
			{
				CReply creply = new CReply();
				
				creply.setRecontent(cr.get("recontent").toString());
				
				Member member = new Member();
				member.setMid(jwtUtil.extractUsername(token));
				
				creply.setMember(member);
				
				ClubGallery cg = cgRep.findById(Long.valueOf(cr.get("cgno").toString())).orElse(null);
				creply.setClubgallery(cg); // 댓글 작성한 갤러리 번호 저장
				
				creply.setReparentnumber(Long.parseLong(cr.get("reparentnumber").toString()));
				System.out.println("//////////////////////////"+creply.getReparentnumber());
				crRep.save(creply);
				map.put("status", 200);
			}
			else
			{
				map.put("status", 0);
				map.put("result", "토큰없음");
			}
			
		} 
		catch (Exception e) 
		{
			map.put("status", -1);
		}
		return map;
	}
	
	// 클럽갤러리 댓글삭제
	// /cluver/api/clubgallery/deletereply
	@RequestMapping(value="/deletereply", 
			method={RequestMethod.POST}, 
			consumes = {MediaType.ALL_VALUE},
			produces= {MediaType.APPLICATION_JSON_VALUE})
	public Map<String, Object> deletereplyDELETE(@RequestBody CReply cr, @RequestHeader(name="token") String token)
	{
		Map<String, Object> map = new HashMap<>();
		try 
		{
			if(token != null)
			{
				String mid = jwtUtil.extractUsername(token);
				System.out.println("token mid : " + mid);
				CReply member = crRep.findById(cr.getRenumber()).orElse(null);
				System.out.println("CReply mid : " + member.getMember().getMid());
				System.out.println("cr.getReparentnumber : "+cr.getReparentnumber());
				if(mid.equals(member.getMember().getMid()))
				{
					if(Long.parseLong(cr.getReparentnumber().toString()) == 0)
					{
						crRep.deleteByReparentnumber(cr.getRenumber());
						crRep.deleteById(cr.getRenumber());
						
					}
					else
					{
						crRep.deleteById(cr.getRenumber());
					}
					
					map.put("status", 200);
				}
				else
				{
					map.put("status", 1);
					map.put("result", "일치하지 않는 아이디");
				}
			}
			else
			{
				map.put("status", 0);
				map.put("result", "토큰없음");
			}
		}
		catch (Exception e)
		{
			map.put("status", -1);
		}
		return map;
	}
	
	@RequestMapping(value="/updatereply", 
			method={RequestMethod.PUT}, 
			consumes = {MediaType.ALL_VALUE},
			produces= {MediaType.APPLICATION_JSON_VALUE})
	public Map<String, Object> updaterepPUT(@RequestBody CReply cr, @RequestHeader(name="token") String token)
	{
		Map<String, Object> map = new HashMap<>();
		try
		{
			CReply crep = crRep.findById(cr.getRenumber()).orElse(null);
			crep.setRecontent(cr.getRecontent());
			crRep.save(crep);
			map.put("status", 200);
		}
		catch (Exception e)
		{
			map.put("status", -1);
		}
		return map;
	}
	
}
