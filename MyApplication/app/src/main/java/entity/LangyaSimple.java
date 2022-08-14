package entity;

import java.io.Serializable;

/**
 * Created by 13520 on 2016/11/21.
 */

public class LangyaSimple  implements Serializable{

    private String id;
    private String proj_id;
    private String title;
    private String desc;
    private String project_title;
    private boolean ischeck=false;
    private  int  picid;
    private int evalue;

    /**
     * @param id
     * @param proj_id
     * @param title
     * @param project_title
     * @param evalue
     * @param picid
     */
    public LangyaSimple(String id, String proj_id, String title,String project_title,int evalue,int picid) {
        this.id = id;
        this.proj_id = proj_id;
        this.title = title;
        this.evalue=evalue;
        this.picid=picid;

        this.project_title = project_title;
    }

    public int getPicid() {
        return picid;
    }

    public void setPicid(int picid) {
        this.picid = picid;
    }

    public int getEvalue() {
        return evalue;
    }

    public void setEvalue(int evalue) {
        this.evalue = evalue;
    }

    public boolean ischeck() {
        return ischeck;
    }

    public void setIscheck(boolean ischeck) {
        this.ischeck = ischeck;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProj_id() {
        return proj_id;
    }

    public void setProj_id(String proj_id) {
        this.proj_id = proj_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getProject_title() {
        return project_title;
    }

    public void setProject_title(String project_title) {
        this.project_title = project_title;
    }

    @Override
    public String toString() {
        return "LangyaSimple{" +
                "id='" + id + '\'' +
                ", proj_id='" + proj_id + '\'' +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", project_title='" + project_title + '\'' +
                ", ischeck=" + ischeck +
                ", evalue=" + evalue +
                '}';
    }
}
