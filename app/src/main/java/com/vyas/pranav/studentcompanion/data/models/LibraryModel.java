package com.vyas.pranav.studentcompanion.data.models;

import java.util.List;

public class LibraryModel {

    private String name, license, extra_license;
    private List<ExternalLibraryModel> external_libraries;

    public LibraryModel(String name, String license, String extra_license, List<ExternalLibraryModel> external_libraries) {
        this.name = name;
        this.license = license;
        this.extra_license = extra_license;
        this.external_libraries = external_libraries;
    }

    public LibraryModel() {
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getExtra_license() {
        return extra_license;
    }

    public void setExtra_license(String extra_license) {
        this.extra_license = extra_license;
    }

    public List<ExternalLibraryModel> getExternal_libraries() {
        return external_libraries;
    }

    public void setExternal_libraries(List<ExternalLibraryModel> external_libraries) {
        this.external_libraries = external_libraries;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}


