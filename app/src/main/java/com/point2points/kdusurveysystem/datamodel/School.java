package com.point2points.kdusurveysystem.datamodel;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class School {

    public String schoolName;
    public String schoolNameShort;
    public String schoolUid;
    public String date;

    private DatabaseReference ref;

    public School(){}

    public School(String schoolName, String schoolNameShort, String schoolUid, long date){
        this.schoolName = schoolName;
        this.schoolNameShort = schoolNameShort;
        this.schoolUid = schoolUid;
        this.date = String.valueOf(date);
    }

    public String getSchoolUid() {
        return schoolUid;
    }

    public void setSchoolUid(String schoolUid) {
        this.schoolUid = schoolUid;
    }

    public String getSchoolNameShort() {
        return schoolNameShort;
    }

    public void setSchoolNameShort(String schoolNameShort) {
        this.schoolNameShort = schoolNameShort;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void createSchool(final String schoolName) {

        String schoolNameReformatted = schoolName.toLowerCase();
        StringBuffer res = new StringBuffer();

        String[] strArr = schoolNameReformatted.split(" ");
        for (String str : strArr) {
            if (str.equals("&")){
                str = new String("and");
            }
            if (str.equals("of") || str.equals("and")){
                res.append(str).append(" ");
            }
            else{
                char[] stringArray = str.trim().toCharArray();
                stringArray[0] = Character.toUpperCase(stringArray[0]);
                str = new String(stringArray);
                res.append(str).append(" ");
            }
        }
        schoolNameReformatted = res.toString().trim();

        StringBuilder schoolNameBuilder = new StringBuilder();
        for (String s : schoolNameReformatted.split(" ")) {
            if (!s.equals("of") && !s.equals("and")){
                schoolNameBuilder.append(s.charAt(0));
            }
        }

        String schoolNameShort = schoolNameBuilder.toString().toUpperCase();

        ref = FirebaseDatabase.getInstance().getReference().child("school");
        DatabaseReference keyref = ref.push();

        School school = new School(schoolNameReformatted, schoolNameShort, keyref.getKey(), System.currentTimeMillis());
        keyref.setValue(school);
    }
}
