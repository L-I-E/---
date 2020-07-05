package kr.ac.skhu.project.item;

public class User {
    String id;
    String name;
    String wardId;
    String guardianId;

    public static User shared = new User();

    public User() {

    }

    public User(String id, String name, String wardId, String guardianId) {
        this.id = id;
        this.name = name;
        this.wardId = wardId;
        this.guardianId = guardianId;
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

    public String getWardId() {
        return wardId;
    }

    public void setWardId(String wardId) {
        this.wardId = wardId;
    }

    public String getGuardianId() {
        return guardianId;
    }

    public void setGuardianId(String guardianId) {
        this.guardianId = guardianId;
    }
}
