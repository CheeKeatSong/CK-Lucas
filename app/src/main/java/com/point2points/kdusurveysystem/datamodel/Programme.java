package com.point2points.kdusurveysystem.datamodel;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Programme {

    public String programmeName; // eg. Bachelor of Computer Science
    //public String programmeNameShort; // eg. BCS
    public String programmeCategory; // eg. Diploma
    //public String programmeDepartment; // eg. Department of Computing
    public String programmeSchool; // eg. School of Computing and Creative Media
    public String programmeUid;
    public String date;

    private DatabaseReference ref;

    public Programme(){}

    //public Programme(String programmeName, String programmeCategory, String programmeDepartment, String programmeSchool, String programmeUid, long date){
    public Programme(String programmeName, String programmeCategory, String programmeSchool, String programmeUid, long date){
        this.programmeName = programmeName;
        this.programmeCategory = programmeCategory;
        //this.programmeDepartment = programmeDepartment;
        this.programmeSchool = programmeSchool;
        this.programmeUid = programmeUid;
        this.date = String.valueOf(date);
    }

    public String getProgrammeUid() {
        return programmeUid;
    }

    public void setProgrammeUid(String programmeUid) {
        this.programmeUid = programmeUid;
    }

    /*public String getProgrammeNameShort() {
        return programmeNameShort;
    }

    public void setProgrammeNameShort(String programmeNameShort) {
        this.programmeNameShort = programmeNameShort;
    }*/

    public String getProgrammeName() {
        return programmeName;
    }

    public void setProgrammeName(String programmeName) {
        this.programmeName = programmeName;
    }

    public void setProgrammeCategory(String programmeCategory) {
        this.programmeCategory = programmeCategory;
    }

    /*public String getProgrammeDeparment() {
        return programmeDepartment;
    }

    public void setProgrammeDepartment(String programmeDepartment) {
        this.programmeDepartment = programmeDepartment;
    }*/

    public String getProgrammeSchool() {
        return programmeSchool;
    }

    public void setProgrammeSchool(String programmeSchool) {
        this.programmeSchool = programmeSchool;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void createProgramme(final String programmeName, final String programmeCategory, final String programmeSchool) {

        /*StringBuilder programmeNameBuilder = new StringBuilder();
        for (String s : programmeName.split(" ")) {
            if (s != "Of" && s != "of") {
                programmeNameBuilder.append(s.charAt(0));
            }
        }

        String programmeNameShort = programmeNameBuilder.toString().toUpperCase();*/

        ref = FirebaseDatabase.getInstance().getReference().child("programme");
        DatabaseReference keyref = ref.push();

        //Programme programme = new Programme(programmeName, programmeCategory, programmeDepartment, programmeSchool, keyref.getKey(), System.currentTimeMillis());
        Programme programme = new Programme(programmeName, programmeCategory, programmeSchool, keyref.getKey(), System.currentTimeMillis());
        keyref.setValue(programme);
    }
}
