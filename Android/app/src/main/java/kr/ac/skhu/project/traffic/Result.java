package kr.ac.skhu.project.traffic;

public class Result {
    private long searchType;
    private long outTrafficCheck;
    private long busCount;
    private long subwayCount;
    private long subwayBusCount;
    private long pointDistance;
    private long startRadius;
    private long endRadius;
    private Path[] path;


    public long getSearchType() { return searchType; }

    public void setSearchType(long value) { this.searchType = value; }


    public long getOutTrafficCheck() { return outTrafficCheck; }

    public void setOutTrafficCheck(long value) { this.outTrafficCheck = value; }


    public long getBusCount() { return busCount; }

    public void setBusCount(long value) { this.busCount = value; }


    public long getSubwayCount() { return subwayCount; }

    public void setSubwayCount(long value) { this.subwayCount = value; }


    public long getSubwayBusCount() { return subwayBusCount; }

    public void setSubwayBusCount(long value) { this.subwayBusCount = value; }


    public long getPointDistance() { return pointDistance; }

    public void setPointDistance(long value) { this.pointDistance = value; }


    public long getStartRadius() { return startRadius; }

    public void setStartRadius(long value) { this.startRadius = value; }


    public long getEndRadius() { return endRadius; }

    public void setEndRadius(long value) { this.endRadius = value; }


    public Path[] getPath() { return path; }

    public void setPath(Path[] value) { this.path = value; }
}