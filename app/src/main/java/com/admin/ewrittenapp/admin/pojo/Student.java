package com.admin.ewrittenapp.admin.pojo;

public class Student {

    String firstName;
    String middleName;
    String lastName;
    String branch;
    int sem;
    String div;
    String enrollNum;
    String email;
    String mobile;

    public Student(String firstName, String middleName, String lastName, String branch, int sem,
                   String div, String enrollNum, String email, String mobile) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.branch = branch;
        this.sem = sem;
        this.div = div;
        this.enrollNum = enrollNum;
        this.email = email;
        this.mobile = mobile;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public int getSem() {
        return sem;
    }

    public void setSem(int sem) {
        this.sem = sem;
    }

    public String getDiv() {
        return div;
    }

    public void setDiv(String div) {
        this.div = div;
    }

    public String getEnrollNum() {
        return enrollNum;
    }

    public void setEnrollNum(String enrollNum) {
        this.enrollNum = enrollNum;
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
