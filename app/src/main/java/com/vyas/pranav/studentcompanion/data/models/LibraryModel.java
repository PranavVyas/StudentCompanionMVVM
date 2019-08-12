package com.vyas.pranav.studentcompanion.data.models;
/*
Student Companion - An Android App that has features like attendance manager, note manager etc
Copyright (C) 2019  Pranav Vyas

This file is a part of Student Companion.

Student Companion is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Student Companion is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.
*/
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


