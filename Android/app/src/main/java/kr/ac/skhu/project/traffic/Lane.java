package kr.ac.skhu.project.traffic;

public class Lane {
    private String busNo;
    private Long type;
    private Long busID;
    private String name;
    private Long subwayCode;
    private Long subwayCityCode;


    public String getBusNo() { return busNo; }

    public void setBusNo(String value) { this.busNo = value; }


    public Long getType() { return type; }

    public void setType(Long value) { this.type = value; }


    public Long getBusID() { return busID; }

    public void setBusID(Long value) { this.busID = value; }


    public String getName() { return name; }

    public void setName(String value) { this.name = value; }


    public Long getSubwayCode() { return subwayCode; }

    public void setSubwayCode(Long value) { this.subwayCode = value; }

    public Long getSubwayCityCode() { return subwayCityCode; }

    public void setSubwayCityCode(Long value) { this.subwayCityCode = value; }
}