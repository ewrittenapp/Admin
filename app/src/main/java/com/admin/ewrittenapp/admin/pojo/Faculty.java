package com.admin.ewrittenapp.admin.pojo;

/**
 * Created by Shahrukh Mansuri on 12/11/2016.
 */

public class Faculty {
    String firstName;
    String middleName;
    String lastName;
    String branch;
    String facultyId;
    String email;
    String mobile;

    public Faculty(String firstName, String middleName, String lastName, String branch, String facultyId, String email, String mobile) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.branch = branch;
        this.facultyId = facultyId;
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

    public String getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(String facultyId) {
        this.facultyId = facultyId;
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
