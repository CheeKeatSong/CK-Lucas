package com.point2points.kdusurveysystem;

/**
 * Created by Rattlesnark on 23/10/2016.
 */

public class UserTest {

    public String username;
    public String email;

    public UserTest() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public UserTest(String username, String email) {
        this.username = username;
        this.email = email;
    }
}
