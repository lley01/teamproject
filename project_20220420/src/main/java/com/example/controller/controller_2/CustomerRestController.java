package com.example.controller.controller_2;

import java.util.HashMap;
import java.util.Map;

import com.example.entity.entity1.Member;
// import com.example.jwt.JwtUtil;
import com.example.repository.MemberRepository;
import com.example.service.UserDetailsServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

// backend만 구현함. 화면구현 X, vue.js 또는 react.js 연동

@RestController
@RequestMapping(value = "/member")
public class CustomerRestController {
    
	@Autowired MemberRepository mRepository;

	// @Autowired JwtUtil jwtUtil;

	@Autowired UserDetailsServiceImpl userDetailservice;

	// 로그인
	// 127.0.0.1:9090/ROOT/member/login
	//{"uemail":"c1", "upw":"c1" };
	// @RequestMapping(value = "/login", 
	// 		method = { RequestMethod.POST },
	// 		consumes = { MediaType.ALL_VALUE },
	// 		produces = { MediaType.APPLICATION_JSON_VALUE })
	// public Map<String, Object> customerLoginPost(
	// 	@RequestBody Member member){
	// 	Map<String, Object> map = new HashMap<>();
	// 	map.put("status", 0); // 정상적이지 않을때

		
	// 	UserDetails user = userDetailservice.loadUserByUsername(member.getUemail());

	// 	BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();
	// 	// user.getPassword() 는 암호화 된거  member.getUpw() 는 암호화x
	// 	// 암호회 되지 않은 것과 암호화 된것 비교하기
	// 	if(bcpe.matches(member.getUpw(), user.getPassword())){
	// 		String token = jwtUtil.generatorToKen(member.getUemail());
	// 		map.put("status", 200); // 0 -> 200
	// 		map.put("token", token);
	// 	}
	// 	return map;

	// }

	// 회원가입(고객만customer)
	// 127.0.0.1:9090/ROOT/member/join.json
	//{"mid":"c1", "mpw":"c1" };
	@PostMapping(value = "/join.json", 
			consumes = { MediaType.ALL_VALUE },
			produces = { MediaType.APPLICATION_JSON_VALUE })
	public Map<String, Object> customerJoinPost(
			@RequestBody Member member,
			@RequestParam(name = "file",required = false) MultipartFile file){

		BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();

		Map<String, Object> map = new HashMap<>();
		try {if(file != null){
			if(!file.isEmpty()){
				member.setMProfile(file.getBytes());
				member.setMImagesize(file.getSize());
				member.setMImagetype(file.getContentType());
				member.setMImagename(file.getOriginalFilename());
			}
	}
			mRepository.save(member);
				map.put("status", 200);
            }
            catch (Exception e) {
                e.printStackTrace();
                map.put("status", 0);
            }
            
            return null;
        }
}