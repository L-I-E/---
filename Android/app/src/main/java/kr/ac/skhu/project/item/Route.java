package kr.ac.skhu.project.item;

public class Route {
    String start;
    String end;
    long trafficType;
    long sectionTime;
    String no;

    public Route(String start, String end, long trafficType, long sectionTime, String no) {
        this.start = start;
        this.end = end;
        this.trafficType = trafficType;
        this.sectionTime = sectionTime;
        this.no = no;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public long getTrafficType() {
        return trafficType;
    }

    public void setTrafficType(long trafficType) {
        this.trafficType = trafficType;
    }

    public long getSectionTime() {
        return sectionTime;
    }

    public void setSectionTime(long sectionTime) {
        this.sectionTime = sectionTime;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }
}
