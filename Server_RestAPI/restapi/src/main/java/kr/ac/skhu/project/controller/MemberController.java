package kr.ac.skhu.project.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import kr.ac.skhu.project.domain.Member;
import kr.ac.skhu.project.repository.MemberRepository;

@RequestMapping("/member")
@RestController
public class MemberController {

	@Autowired
	private MemberRepository memberRepository;

	@RequestMapping(value = "/select", method = { RequestMethod.GET, RequestMethod.POST })
	public List<Member> selectAll() {
		return memberRepository.findAll();
	}

	@RequestMapping(value = "/select/{id}", method = { RequestMethod.GET, RequestMethod.POST })
	public Member selectOne(@PathVariable("id") String id) {
		return memberRepository.findById(id).get();
	}

	@RequestMapping(value = "/exist/{id}", method = { RequestMethod.GET, RequestMethod.POST })
	public boolean exist(@PathVariable("id") String id) {
		return memberRepository.existsById(id);
	}

	@RequestMapping(value = "/insert", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public Member insert(@RequestBody Map<String, String> map) {
		return memberRepository.save(new Member(map.get("id"), map.get("name"), map.get("password"), 126.9783740,
				37.5670135, Integer.parseInt(map.get("isGuardian"))));
	}

	@RequestMapping(value = "/update/{id}", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public Member updateLocation(@PathVariable("id") String id, @RequestBody Map<String, Double> map) {
		Member member = memberRepository.findById(id).orElse(null);
		return memberRepository.save(new Member(member.getId(), member.getName(), member.getPassword(),
				map.get("longitude").doubleValue(), map.get("latitude").doubleValue(), member.getIsGuardian()));
	}

	@RequestMapping(value = "/delete/{id}", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = "application/json; charset=utf-8")
	public String deleteOne(@PathVariable("id") String id) {
		memberRepository.deleteById(id);
		return "삭제 완료";
	}
}