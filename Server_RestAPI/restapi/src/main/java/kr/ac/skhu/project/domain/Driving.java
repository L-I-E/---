package kr.ac.skhu.project.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Driving {
	@Id
	private String guardId;
	private String wardId;
	private double startLatitude;
	private double startLongitude;
	private double destinationLatitude;
	private double destinationLongitude;

	public Driving() {

	}

	public Driving(String guardId, String wardId, double startLatitude, double startLongitude,
			double destinationLatitude, double destinationLongitude) {
		this.guardId = guardId;
		this.wardId = wardId;
		this.startLatitude = startLatitude;
		this.startLongitude = startLongitude;
		this.destinationLatitude = destinationLatitude;
		this.destinationLongitude = destinationLongitude;
	}

	public String getGuardId() {
		return guardId;
	}

	public void setGuardId(String guardId) {
		this.guardId = guardId;
	}

	public String getWardId() {
		return wardId;
	}

	public void setWardId(String wardId) {
		this.wardId = wardId;
	}

	public double getStartLatitude() {
		return startLatitude;
	}

	public void setStartLatitude(double startLatitude) {
		this.startLatitude = startLatitude;
	}

	public double getStartLongitude() {
		return startLongitude;
	}

	public void setStartLongitude(double startLongitude) {
		this.startLongitude = startLongitude;
	}

	public double getDestinationLatitude() {
		return destinationLatitude;
	}

	public void setDestinationLatitude(double destinationLatitude) {
		this.destinationLatitude = destinationLatitude;
	}

	public double getDestinationLongitude() {
		return destinationLongitude;
	}

	public void setDestinationLongitude(double destinationLongitude) {
		this.destinationLongitude = destinationLongitude;
	}

}
