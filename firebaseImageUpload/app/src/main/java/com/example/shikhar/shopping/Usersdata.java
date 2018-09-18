package com.example.shikhar.shopping;

public class Usersdata {
    public String fid;
    public String femail;
    public String fpassword;

    public Usersdata(String fid, String femail, String fpassword) {
        this.fid = fid;
        this.femail = femail;
        this.fpassword = fpassword;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getFemail() {
        return femail;
    }

    public void setFemail(String femail) {
        this.femail = femail;
    }

    public String getFpassword() {
        return fpassword;
    }

    public void setFpassword(String fpassword) {
        this.fpassword = fpassword;
    }
}
