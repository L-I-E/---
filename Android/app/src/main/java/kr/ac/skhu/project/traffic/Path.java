package kr.ac.skhu.project.traffic;

public class Path {
    private long pathType;
    private Info info;
    private SubPath[] subPath;


    public long getPathType() { return pathType; }

    public void setPathType(long value) { this.pathType = value; }


    public Info getInfo() { return info; }

    public void setInfo(Info value) { this.info = value; }

    public SubPath[] getSubPath() { return subPath; }

    public void setSubPath(SubPath[] value) { this.subPath = value; }
}