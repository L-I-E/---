package kr.ac.skhu.project.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import kr.ac.skhu.project.domain.Guardian;
import kr.ac.skhu.project.repository.GuardianRepository;

@RequestMapping("/guardian")
@RestController
public class GuardianController {

	@Autowired
	private GuardianRepository guardianRepository;

	@RequestMapping(value = "/select", method = { RequestMethod.GET, RequestMethod.POST })
	public List<Guardian> selectAll() {
		return guardianRepository.findAll();
	}

	@RequestMapping(value = "/select/{guardianId}", method = { RequestMethod.GET, RequestMethod.POST })
	public Guardian selectOne(@PathVariable("guardianId") String guardianId) {
		return guardianRepository.findById(guardianId).get();
	}

	@RequestMapping(value = "/exist/{guardianId}", method = { RequestMethod.GET, RequestMethod.POST })
	public boolean exist(@PathVariable("guardianId") String guardianId) {
		return guardianRepository.existsById(guardianId);
	}

	@RequestMapping(value = "/insert", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public Guardian insert(@RequestBody Map<String, String> map) {
		return guardianRepository.save(new Guardian(map.get("guardianId"), map.get("wardId")));
	}

	@RequestMapping(value = "/update/{guardianId}", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public Guardian updateLocation(@PathVariable("guardianId") String guardianId,
			@RequestBody Map<String, String> map) {
		Guardian guardian = guardianRepository.findById(guardianId).orElse(null);
		return guardianRepository.save(new Guardian(guardian.getGuardianId(), map.get("wardId")));
	}
	
	@RequestMapping(value = "/delete/{guardianId}", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = "application/json; charset=utf-8")
	public String deleteOne(@PathVariable("guardianId") String guardianId) {
		guardianRepository.deleteById(guardianId);
		return "삭제 완료";
	}
}