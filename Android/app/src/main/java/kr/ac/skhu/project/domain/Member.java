package kr.ac.skhu.project.domain;

public class Member {
    private String id;
    private String name;
    private String password;
    private double longitude;
    private double latitude;
    private int isGuardian;

    public Member() {

    }

    public Member(String id, String name, String password, double longitude, double latitude, int isGuardian) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.longitude = longitude;
        this.latitude = latitude;
        this.isGuardian = isGuardian;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double logitude) {
        this.longitude = logitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int isGuardian() {
        return isGuardian;
    }

    public void setGuardian(int isGuardian) {
        this.isGuardian = isGuardian;
    }

}