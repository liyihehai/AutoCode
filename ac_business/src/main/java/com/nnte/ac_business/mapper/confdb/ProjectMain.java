package com.nnte.ac_business.mapper.confdb;

import com.nnte.framework.base.BaseModel;

public class ProjectMain extends BaseModel {
    private Integer projectCode;
    private String projectName;
    private String rootPackage;
    private String subClass;
    private String rootDir;
    private String connDriverName;
    private String connUrl;
    private String connUsername;
    private String connPassword;

    public ProjectMain() {

    }

    public Integer getProjectCode() { return projectCode; }

    public void setProjectCode(Integer projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getRootPackage() {
        return rootPackage;
    }

    public void setRootPackage(String rootPackage) {
        this.rootPackage = rootPackage;
    }

    public String getConnDriverName() {
        return connDriverName;
    }

    public void setConnDriverName(String connDriverName) {
        this.connDriverName = connDriverName;
    }

    public String getConnUrl() {
        return connUrl;
    }

    public void setConnUrl(String connUrl) {
        this.connUrl = connUrl;
    }

    public String getConnUsername() {
        return connUsername;
    }

    public void setConnUsername(String connUserName) {
        this.connUsername = connUserName;
    }

    public String getConnPassword() {
        return connPassword;
    }

    public void setConnPassword(String connPassword) {
        this.connPassword = connPassword;
    }

    public String getSubClass() { return subClass; }

    public void setSubClass(String subClass) { this.subClass = subClass; }

    public String getRootDir() { return rootDir; }

    public void setRootDir(String rootDir) { this.rootDir = rootDir; }
}
