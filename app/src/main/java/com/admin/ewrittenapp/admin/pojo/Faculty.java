package com.admin.ewrittenapp.admin.pojo;

/**
 * Created by Shahrukh Mansuri on 12/11/2016.
 */

public class Faculty {

    String name;
    String branch;
    String email;
    String mobile;

    public Faculty(String name, String branch, String email, String mobile) {
        this.name = name;
        this.branch = branch;
        this.email = email;
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
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
