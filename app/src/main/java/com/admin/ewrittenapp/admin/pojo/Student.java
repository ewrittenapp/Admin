package com.admin.ewrittenapp.admin.pojo;

public class Student {

    String fname;
    String mname;
    String lname;
    String branch;
    String sem;
    String div;
    String enroll;
    String email;
    String mobile;

    public Student(String firstName, String middleName, String lastName, String branch, String sem,
                   String div, String enrollNum, String email, String mobile) {
        this.fname = firstName;
        this.mname = middleName;
        this.lname = lastName;
        this.branch = branch;
        this.sem = sem;
        this.div = div;
        this.enroll = enrollNum;
        this.email = email;
        this.mobile = mobile;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getSem() {
        return sem;
    }

    public void setSem(String sem) {
        this.sem = sem;
    }

    public String getDiv() {
        return div;
    }

    public void setDiv(String div) {
        this.div = div;
    }

    public String getEnroll() {
        return enroll;
    }

    public void setEnroll(String enroll) {
        this.enroll = enroll;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
