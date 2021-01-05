package com.exemple.stage.Commun;

import android.content.Context;

import com.exemple.stage.modele.TokenModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class UserUtils {
    public static void UpdateToken(Context context, String token) {
        TokenModel token1 = new TokenModel(token);
        FirebaseDatabase.getInstance()
                .getReference(Commun.CLIENT_TOKEN_REFERENCE)
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .setValue(token1)
                .addOnFailureListener(Throwable::printStackTrace)
                .addOnSuccessListener(aVoid -> {
                });
    }
}
