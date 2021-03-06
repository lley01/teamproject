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
	
	// ??????????????? ??????
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
					if(file.length > 0)// ??????????????? ?????????
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
						map.put("result", "???????????????");
					}
				}
			}
			else
			{
				map.put("status", 0);
				map.put("result", "????????????");
			}
		} 
		catch (Exception e) 
		{
			map.put("status", -1);
		}
		return map;
	}
	
	// ??????????????? ??????
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
				//1????????? ??? 10??? ??????
				PageRequest pageRequest = PageRequest.of(page-1, 9); 
				System.out.println(pageRequest);
				
				//????????? ??????, 1????????? 10???, ????????? ????????????
				List<ClubGallery> list = new ArrayList<>();
				if(option.equals("????????????"))
				{
					list = cgRep.findByCgnameContainingAndClub_cnoOrderByCgnoDesc(text, cno, pageRequest);
				}
				else if(option.equals("???????????????"))
				{
					list = cgRep.findByCgdescContainingAndClub_cnoOrderByCgnoDesc(text, cno, pageRequest);
				}
				else if(option.equals("??????????????????"))
				{
					list = cgRep.findByMember_midContainingAndClub_cnoOrderByCgnoDesc(text, cno, pageRequest);
				}
//				else if(option.equals("??????"))
//				{
//					list = cgRep.findByAllOptions(text, pageRequest);
//				}
				else
				{
					list = cgRep.findByClub_cnoOrderByCgnoDesc(cno, pageRequest);
				}
				
//				System.out.println("list : " + list);
				
				//????????? ????????? ??? ??????
				long total = cgRep.countByClub_cno(cno);
//				System.out.println("total = " + total);
				model.addAttribute("total", total);
				
				// pages = 1~9 = 1, 10~18 = 2, 19~27 = 3, ...... // ??? ???????????? 9??? ?????????
//				System.out.println("pages : " + (total-1) / 10 + 1);
				model.addAttribute("pages", (total-1) / 9 + 1);	
				
				List<CGMemView> mlist = new ArrayList<>(); // ????????? ?????????
				for(int i=0; i<list.size(); i++) //list ??? ?????? ???????????? ????????? ????????? ????????? ?????? 
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
				map.put("result", "????????????");
			}
			
		} 
		catch (Exception e) 
		{
			map.put("status", -1);
		}
		return map;
	}
	// ????????? ???????????? + ???????????? + ????????? url
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
				long imagecount = cgiRep.countByClubgallery_cgno(cgno); // idx???
				clubGallery.setGimageurl("/cluver/clubgallery/image?cgno=" + cgno); // ????????? url ?????????(idx?????? ??????????????? ??????????????? ??????)
				CGMemView cgmv = cgmvRep.findById(cgno).orElse(null);
				// ?????? ?????? ????????? ?????? ??????
				List<CReply> replylist = new ArrayList<>();
				// ????????? ?????? ?????? ??????
				List<CReply> rereplylist = new ArrayList<>();
				// ?????? ?????? ????????? ?????? ??????
				List<CReply> replylistlength = crRep.findByClubgallery_cgnoOrderByRenumberDesc(cgno);
				// ????????? ???????????? ?????? ????????? ?????? ????????????
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
				List<CReplyMemberView> mlist = new ArrayList<>(); // ?????? ????????? ????????? ?????????
				List<CReplyMemberView> remlist = new ArrayList<>(); // ????????? ????????? ????????? ?????????
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
				
				model.addAttribute("clubgallery", clubGallery); //???????????????(????????? url ??????)	
				model.addAttribute("nick", cgmv); // ??? ????????? ?????????
				model.addAttribute("replylist", replylist); // ??????
				model.addAttribute("rereplylist", rereplylist); // ???????????????
				model.addAttribute("imagecount", imagecount); // ????????? ??????(idx)
				model.addAttribute("replynicklist", mlist); // ??????????????? ????????? ??????
				model.addAttribute("rereplynicklist", remlist); // ??????????????? ????????? ??????
				
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
	
	// ????????? ????????? ??????
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
				map.put("result", "????????????");
			}
			
		} 
		catch (Exception e) 
		{
			map.put("status", -1);
		}
		return map;
	}
	
	
	
	// ????????? ??????
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
				map.put("result", "????????????");
			}
			
		} 
		catch (Exception e) 
		{
			map.put("status", -1);
		}
		return map;
	}
	
	// ??????????????? ??????/???????????????
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
				creply.setClubgallery(cg); // ?????? ????????? ????????? ?????? ??????
				
				creply.setReparentnumber(Long.parseLong(cr.get("reparentnumber").toString()));
				System.out.println("//////////////////////////"+creply.getReparentnumber());
				crRep.save(creply);
				map.put("status", 200);
			}
			else
			{
				map.put("status", 0);
				map.put("result", "????????????");
			}
			
		} 
		catch (Exception e) 
		{
			map.put("status", -1);
		}
		return map;
	}
	
	// ??????????????? ????????????
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
					map.put("result", "???????????? ?????? ?????????");
				}
			}
			else
			{
				map.put("status", 0);
				map.put("result", "????????????");
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
