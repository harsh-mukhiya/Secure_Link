package com.example.securelink;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class DES extends AppCompatActivity {

    public static final String DES_ENCRYPTION_SCHEME="DES";
    private static final String UNICODE_FORMAT="UTF8";
    //public  static String pwdtext="HarshKasana";
  //  private static final int SHORT_DELAY = 2000;


    ImageView sendbut,encbut, decbut;
    EditText editText;
    TextView textView;
  //  Button button;


    String ans="";
    private SecretKeyFactory mySecretKeyFactory;
    SecretKey key;
    private KeySpec myKeySpec;
    byte[] KeyAsBytes;
    private Cipher cipher;

    private String myEncryptionKey;

    private String getMyEncryptionScheme;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_des);
        encbut=findViewById(R.id.encbut);
        decbut=findViewById((R.id.decbut));
        sendbut=findViewById(R.id.sendbut);
        editText=findViewById(R.id.editTextMessage);
        textView=findViewById(R.id.cyphertext);
//        button=findViewById(R.id.button2);

        getMyEncryptionScheme=DES_ENCRYPTION_SCHEME;

        myEncryptionKey="HarshKasana";



        try {
            mySecretKeyFactory=SecretKeyFactory.getInstance(getMyEncryptionScheme);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            cipher=Cipher.getInstance(getMyEncryptionScheme);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        try {
            KeyAsBytes=myEncryptionKey.getBytes(UNICODE_FORMAT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            myKeySpec=new DESKeySpec(KeyAsBytes);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        try {
            key=mySecretKeyFactory.generateSecret(myKeySpec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }


        encbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(DES.this, "encrypt button pressed", Toast.LENGTH_SHORT).show();

                String temp=editText.getText().toString();
                ans=encrypt(temp);
                textView.setText(ans);


            }
        });

        decbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(DES.this, "decrypt button pressed", Toast.LENGTH_SHORT).show();

                String temp=editText.getText().toString();
                ans=decrypt(temp);
                textView.setText(ans);

            }
        });

        sendbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(DES.this, "send button pressed", Toast.LENGTH_SHORT).show();
                try
                {
                    if(ans.length()>0) {
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, ans);
                        sendIntent.setType("text/plain");
                        startActivity(sendIntent);
                    }
                    else
                    {
                        Toast.makeText(DES.this,"Empty output",Toast.LENGTH_SHORT).show();
                    }
                }
                // handling the exception
                catch(Exception e)
                {
                    // displaying the custom message
                    Toast.makeText(DES.this, "Can't send empty output", Toast.LENGTH_SHORT).show();
                }
//                if(ans.length()>0) {
//                    Intent sendIntent = new Intent();
//                    sendIntent.setAction(Intent.ACTION_SEND);
//                    sendIntent.putExtra(Intent.EXTRA_TEXT, ans);
//                    sendIntent.setType("text/plain");
//                    startActivity(sendIntent);
//                }
//                else
//                {
//                    Toast.makeText(DES.this,"Empty output",Toast.LENGTH_SHORT).show();
//                }

            }
        });
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                    Intent intent = new Intent( DES.this, ResetPassword.class );
//                    startActivity(intent);
//
//
//            }
//        });

    }
//method to encrypt string
    public String encrypt(String unencryptedString)
    {
        String encryptedString =null;
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] plainText = unencryptedString.getBytes(UNICODE_FORMAT);
            byte[] encryptedText = cipher.doFinal(plainText);

            encryptedString= Base64.encodeToString(encryptedText, Base64.DEFAULT);
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("cypher", encryptedString);
            clipboardManager.setPrimaryClip(clip);
            Toast.makeText(this, "cypher is copied to clipboard", Toast.LENGTH_SHORT).show();
        }
        catch (InvalidKeyException | UnsupportedEncodingException | IllegalBlockSizeException | BadPaddingException e) {

        }

        return encryptedString;
    }
// method to decrypt a cypher
    public String decrypt(String encrytedString)
    {
        String decryptedText = null;
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] encryptedText = Base64.decode(encrytedString, Base64.DEFAULT);
            byte[] plainText = cipher.doFinal(encryptedText);
            decryptedText = bytes2String(plainText);
        }catch(InvalidKeyException|IllegalBlockSizeException|BadPaddingException e) {

        }
        return decryptedText;
    }
//return string values form array of bytes
    private static String bytes2String(byte[] bytes)
    {
        StringBuilder stringBuffer=new StringBuilder();
        for(int i=0;i<bytes.length;i++)
        {
            stringBuffer.append((char) bytes[i]);

        }
        return stringBuffer.toString();
    }
}