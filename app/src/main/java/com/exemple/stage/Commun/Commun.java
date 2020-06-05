package com.exemple.stage.Commun;

import com.exemple.stage.API.FireBaseClient;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class Commun {

    public static final String Email_User = Objects.requireNonNull(FireBaseClient.getInstance().getFirebaseAuth().getCurrentUser()).getEmail();
    public static final FirebaseUser Current_User = FireBaseClient.getInstance().getFirebaseAuth().getCurrentUser();
}
