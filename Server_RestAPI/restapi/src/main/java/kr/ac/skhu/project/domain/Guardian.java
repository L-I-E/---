package kr.ac.skhu.project.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Guardian {
	@Id
	private String guardianId;
	private String wardId;

	public Guardian() {

	}

	public Guardian(String guardianId, String wardId) {
		this.guardianId = guardianId;
		this.wardId = wardId;
	}

	public String getGuardianId() {
		return guardianId;
	}

	public void setGuardianId(String guardianId) {
		this.guardianId = guardianId;
	}

	public String getWardId() {
		return wardId;
	}

	public void setWardId(String wardId) {
		this.wardId = wardId;
	}
}
