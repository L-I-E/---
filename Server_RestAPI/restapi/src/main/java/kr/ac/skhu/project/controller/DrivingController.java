package kr.ac.skhu.project.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import kr.ac.skhu.project.domain.Driving;
import kr.ac.skhu.project.repository.DrivingRepository;

@RequestMapping("/driving")
@RestController
public class DrivingController {

	@Autowired
	private DrivingRepository drivingRepository;

	@RequestMapping(value = "/select", method = { RequestMethod.GET, RequestMethod.POST })
	public List<Driving> selectAll() {
		return drivingRepository.findAll();
	}

	@RequestMapping(value = "/select/{guardId}", method = { RequestMethod.GET, RequestMethod.POST })
	public Driving selectOne(@PathVariable("guardId") String guardId) {
		return drivingRepository.findById(guardId).get();
	}

	@RequestMapping(value = "/exist/{guardId}", method = { RequestMethod.GET, RequestMethod.POST })
	public boolean exist(@PathVariable("guardId") String guardId) {
		return drivingRepository.existsById(guardId);
	}

	@RequestMapping(value = "/insert", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public Driving insert(@RequestBody Map<String, String> map) {
		return drivingRepository.save(new Driving(map.get("guardId"), map.get("wardId"),
				Double.parseDouble(map.get("startLatitude")), Double.parseDouble(map.get("startLongitude")),
				Double.parseDouble(map.get("destinationLatitude")),
				Double.parseDouble(map.get("destinationLongitude"))));
	}

	@RequestMapping(value = "/update/{guardId}", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public Driving updateLocation(@PathVariable("guardId") String guardId, @RequestBody Map<String, String> map1,
			@RequestBody Map<String, Double> map2) {
		Driving driving = drivingRepository.findById(guardId).orElse(null);
		return drivingRepository.save(new Driving(driving.getGuardId(), map1.get("wardId"), map2.get("startLatitude"),
				map2.get("startLongitude"), map2.get("destinationLatitude"), map2.get("destinationLongitude")));
	}

	@RequestMapping(value = "/delete/{guardId}", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = "application/json; charset=utf-8")
	public String deleteOne(@PathVariable("guardId") String guardId) {
		drivingRepository.deleteById(guardId);
		return "삭제 완료";
	}
}