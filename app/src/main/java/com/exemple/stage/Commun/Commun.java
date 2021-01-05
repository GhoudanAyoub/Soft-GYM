package com.exemple.stage.Commun;

import com.exemple.stage.API.FireBaseClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class Commun {


    public static final String Current_Client_Id = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    public static final String Current_Client_DispalyName = FireBaseClient.getInstance().getFirebaseUser().getDisplayName();
    public static final String Current_Client_Gmail = FireBaseClient.getInstance().getFirebaseUser().getEmail();
    public static final String CLIENT_TOKEN_REFERENCE = "ClientToken" ;
    public static final String User_Class_Name = "Users";
    public static final String Email_User = Current_Client_Gmail;
}
