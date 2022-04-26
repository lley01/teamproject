package com.example.controller.controller_3;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.repository.repository_3.CReplyRepository;
import com.example.repository.repository_3.ClubBoardImageRepository;
import com.example.repository.repository_3.ClubBoardReactionRepository;
import com.example.repository.repository_3.ClubBoardRepository;
import com.example.service.service_3.ClubBoardImageService;
import com.example.service.service_3.ClubBoardService;
import com.example.entity.entity2.ClubBoard;
import com.example.entity.entity2.CbImage;
import com.example.entity.entity2.CReply;
import com.example.entity.entity1.Reaction;

@Controller
@RequestMapping(value = "/clubboard")
public class ClubBoardController {
	
	@Autowired
	ClubBoardRepository cbRep;
	
	@Autowired
	ClubBoardService cbService;
	
	@Autowired
	ClubBoardImageService cbiService;
	
	@Autowired
	ClubBoardReactionRepository cbrRep;
	
	@Autowired
	CReplyRepository crRep;
	
	@Autowired
	ClubBoardImageRepository cbiRep;
	
	@GetMapping(value="/insert")
	public String insertGET()
	{
		return "/clubboard/insert";
	}
	
	// 클럽게시판 글쓰기
	// 127.0.0.1:9090/ROOT/clubboard/insert
	@PostMapping(value="/insert")
	public String insertPOST(@ModelAttribute ClubBoard clubBoard, @RequestParam(name="file") MultipartFile file) throws IOException
	{
		try 
		{
			CbImage cbImage = new CbImage();
			if(file != null)
			{
				cbImage.setCbiImage(file.getBytes()); 
				cbImage.setCbiImagename(file.getOriginalFilename());
				cbImage.setCbiImagesize(file.getSize());
				cbImage.setCbiImagetype(file.getContentType());
			}
			
			cbRep.save(clubBoard);
			cbiRep.save(cbImage);
			
			return "redirect:/clubboard/selectlist";
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return "redirect:/";
		}
	}
	
	// 클럽게시판 글목록 페이지
	// 127.0.0.1:9090/ROOT/clubboard/selectlist
	@GetMapping(value="/selectlist")
	public String selectlistGET(Model model, @RequestParam(name="page", defaultValue="1") int page, @RequestParam(name="text", defaultValue="") String text)
	{
		try 
		{
			//1페이지 당 20글 표시
			PageRequest pageRequest = PageRequest.of(page-1, 20); 
			System.out.println(pageRequest);
			
			//검색어 포함, 1페이지 20글, 글번호 내림차순
			List<ClubBoard> list = cbRep.findByCbTitleContainingOrderByCbNoDesc(text, pageRequest);
			model.addAttribute("list", list);
			System.out.println(list.toString());
			
			//페이지네이션 구현용 글 개수 가져와서 model에 넣기
			long total = cbRep.countByCbTitleContaining(text);
			
			//1~20 = 1, 21~40 = 2, 41~60 = 3, ......
			model.addAttribute("pages", (total-1) / 20 + 1);
			System.out.println(total);
			return "/clubboard/selectlist";
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return "redirect:/";
		}
	}

	// 클럽게시판 글 상세내용 페이지 (첨부이미지, 댓글 포함)
	// 127.0.0.1:9090/ROOT/clubboard/select?cbNo=(?)
	@GetMapping(value="/select")
	public String selectGET(Model model, @RequestParam(name="cbNo") long cbNo
//			@RequestParam(name="rType") String rType
			)
	{
		try 
		{
			List<CReply> list = cbService.selectCReplylist(cbNo);
			
//			long rtype = cbrRep.selectReactionCount(cbNo, rType);
			
			
			model.addAttribute("clubboard", cbService.selectClubBoard(cbNo)); //글상세내용
			model.addAttribute("cbimage", cbiService.selectClubBoardImage(cbNo)); //이미지
			model.addAttribute("replylist", list); // 댓글
//			model.addAttribute("rtype", rtype); // 좋아요 수
			
			System.out.println(model.toString());
			
			return "/clubboard/select?cbNo=" + cbNo;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return "redirect:/";
		}
	}
	
	
	
	// 클럽게시판 글삭제
	// 127.0.0.1:9090/ROOT/clubboard/delete
	@PostMapping(value="/delete")
	public String deletePOST(@ModelAttribute ClubBoard clubboard)
	{
		try 
		{
			cbRep.deleteById(clubboard.getCbNo());
			return "redirect:/clubboard/selectlist";
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return "redirect:/";
		}
	}
	
	// 클럽게시판 글수정 페이지
	// 127.0.0.1:9090/ROOT/clubboard/update?cbNo=
	@GetMapping(value="/update")
	public String updateGET(Model model, @RequestParam(name="cbNo") long cbNo)
	{
		try 
		{
			model.addAttribute("clubboard", cbService.selectClubBoard(cbNo)); //글내용 수정페이지로 넘겨주기
			model.addAttribute("cbimage", cbiService.selectClubBoardImage(cbNo)); //이미지파일 데이터 넘겨주기
			return "/clubboard/update?cbNo=" + cbNo;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return "redirect:/";
		}
	}
	
	// 클럽게시판 글수정
	// 127.0.0.1:9090/ROOT/clubboard/update
	@PostMapping(value="/update")
	public String updatePOST(@ModelAttribute ClubBoard clubboard, @ModelAttribute CbImage cbimage, @RequestParam(name="cbimage") MultipartFile file)
	{
		try 
		{
			if(file != null) //파일 첨부시 cbimage에 첨부한 파일 데이터 넣기
			{
				cbimage.setCbiImage(file.getBytes());
				cbimage.setCbiImagename(file.getOriginalFilename());
				cbimage.setCbiImagesize(file.getSize());
				cbimage.setCbiImagetype(file.getContentType());
			}
			else //파일 미첨부시 cbimage에 기존 이미지 데이터 넣기
			{
				cbimage.setCbiImage(cbimage.getCbiImage());
				cbimage.setCbiImagename(cbimage.getCbiImagename());
				cbimage.setCbiImagesize(cbimage.getCbiImagesize());
				cbimage.setCbiImagetype(cbimage.getCbiImagetype());
			}
			
			int ret = cbService.updateClubBoard(clubboard);
			int ret1 = cbiService.updateClubBoardImage(cbimage);
			if(ret == 1)
			{
				if(ret1 == 1)
				{
					return "/clubboard/select?cbNo=" + clubboard.getCbNo();
				}
				cbRep.deleteById(clubboard.getCbNo());
				return "redirect:/clubboard/update?cbNo=" + clubboard.getCbNo();
			}
			return "redirect:/clubboard/update?cbNo=" + clubboard.getCbNo();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return "redirect:/";
		}
	}
	
	// 클럽게시판 댓글쓰기
	// 127.0.0.1:9090/ROOT/clubboard/insertreply
	@PostMapping(value="/insertreply")
	public String insertreplyPOST(@ModelAttribute CReply cReply, Model model)
	{
		try 
		{
			System.out.println(cReply.getClubBoard().getCbNo());
			cbService.insertCReply(cReply);
			model.addAttribute("reply", cReply);
			return "redirect:/clubboard/select?cbNo=" + cReply.getClubBoard().getCbNo();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return "redirect:/";
		}
	}
	
	// 클럽게시판 댓글삭제
	// 127.0.0.1:9090/ROOT/clubboard/deletereply
	// {"reNumber":""}
	@PostMapping(value="/deletereply")
	public String deletereplyPOST(@ModelAttribute CReply creply)
	{
		try 
		{
			crRep.deleteById(creply.getReNumber());
			return "redirect:/clubboard/selectlist";
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return "redirect:/";
		}
	}
	
	// 127.0.0.1:9090/ROOT/clubboard/insertreaction
	// 클럽게시판 글 반응(좋아요/엄지) 넣기
	@PostMapping(value="/insertreaction")
	public String insertreactionPOST(@ModelAttribute Reaction reaction, @RequestParam(name="cbNo") long cbNo) 
	// 반응종류 -> <input type="submit" name="reaction.rType" value="좋아요 or 따봉" />
	{
		try 
		{
			cbrRep.save(reaction); //rType(반응종류) = "좋아요" or "따봉"
			return "redirect:/clubboard/select?cbNo=" + cbNo;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return "redirect:/";
		}
	}
}
