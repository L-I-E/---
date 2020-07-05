package kr.ac.skhu.project.traffic;

public class SubPath {
    private long trafficType;
    private long distance;
    private long sectionTime;
    private Long stationCount;
    private Lane[] lane;
    private String startName;
    private Double startX;
    private Double startY;
    private String endName;
    private Double endX;
    private Double endY;
    private Long startID;
    private Long endID;
    private PassStopList passStopList;
    private String way;
    private Long wayCode;
    private String door;
    private String startExitNo;
    private Double startExitX;
    private Double startExitY;
    private String endExitNo;
    private Double endExitX;
    private Double endExitY;


    public long getTrafficType() { return trafficType; }

    public void setTrafficType(long value) { this.trafficType = value; }


    public long getDistance() { return distance; }

    public void setDistance(long value) { this.distance = value; }

    public long getSectionTime() { return sectionTime; }

    public void setSectionTime(long value) { this.sectionTime = value; }

    public Long getStationCount() { return stationCount; }

    public void setStationCount(Long value) { this.stationCount = value; }


    public Lane[] getLane() { return lane; }

    public void setLane(Lane[] value) { this.lane = value; }


    public String getStartName() { return startName; }

    public void setStartName(String value) { this.startName = value; }


    public Double getStartX() { return startX; }

    public void setStartX(Double value) { this.startX = value; }


    public Double getStartY() { return startY; }

    public void setStartY(Double value) { this.startY = value; }


    public String getEndName() { return endName; }

    public void setEndName(String value) { this.endName = value; }


    public Double getEndX() { return endX; }

    public void setEndX(Double value) { this.endX = value; }


    public Double getEndY() { return endY; }

    public void setEndY(Double value) { this.endY = value; }


    public Long getStartID() { return startID; }

    public void setStartID(Long value) { this.startID = value; }


    public Long getEndID() { return endID; }

    public void setEndID(Long value) { this.endID = value; }


    public PassStopList getPassStopList() { return passStopList; }

    public void setPassStopList(PassStopList value) { this.passStopList = value; }


    public String getWay() { return way; }

    public void setWay(String value) { this.way = value; }


    public Long getWayCode() { return wayCode; }

    public void setWayCode(Long value) { this.wayCode = value; }


    public String getDoor() { return door; }

    public void setDoor(String value) { this.door = value; }


    public String getStartExitNo() { return startExitNo; }

    public void setStartExitNo(String value) { this.startExitNo = value; }


    public Double getStartExitX() { return startExitX; }

    public void setStartExitX(Double value) { this.startExitX = value; }


    public Double getStartExitY() { return startExitY; }

    public void setStartExitY(Double value) { this.startExitY = value; }


    public String getEndExitNo() { return endExitNo; }

    public void setEndExitNo(String value) { this.endExitNo = value; }


    public Double getEndExitX() { return endExitX; }

    public void setEndExitX(Double value) { this.endExitX = value; }

    public Double getEndExitY() { return endExitY; }

    public void setEndExitY(Double value) { this.endExitY = value; }
}