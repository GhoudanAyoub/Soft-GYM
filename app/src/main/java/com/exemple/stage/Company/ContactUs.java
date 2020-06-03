package com.exemple.stage.Company;
/**
 * Created By GHOUADN AYOUB
 */


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.exemple.stage.R;
import com.rengwuxian.materialedittext.MaterialEditText;

public class ContactUs extends AppCompatActivity {

    MaterialEditText name, phone, gmail, subject, text;
    Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        name = findViewById(R.id.ContactName);
        phone = findViewById(R.id.ContactPhone);
        gmail = findViewById(R.id.ContactGmail);
        subject = findViewById(R.id.ContactSubject);
        text = findViewById(R.id.ContactText);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        send = findViewById(R.id.ContactSendButton);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSendCurProd = new Intent(Intent.ACTION_SENDTO);
                String uriTextCurProd = "mailto:" + Uri.encode("contact@softgym-sabine.com") +
                        "?subject=" + Uri.encode(subject.getText().toString()) +
                        "&body=" + Uri.encode(Html.fromHtml("<strong>" + text.getText().toString() + "</strong>" +
                        "<br><br>" + gmail.getText().toString() + "<br>" +
                        "name  :" + name.getText().toString() + "<br>" +
                        "Phone Number :" + phone.getText().toString() + "<br>" +
                        "Envoyé de l'application SoftGym").toString());
                Uri uriCurProd = Uri.parse(uriTextCurProd);
                intentSendCurProd.setData(uriCurProd);
                startActivity((Intent.createChooser(intentSendCurProd, "Send mail...")));
                clear();
            }
        });
    }

    public void clear() {
        name.setText("");
        phone.setText("");
        gmail.setText("");
        subject.setText("");
        text.setText("");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
