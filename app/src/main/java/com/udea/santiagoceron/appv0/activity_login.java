package com.udea.santiagoceron.appv0;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.LauncherApps;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class activity_login extends AppCompatActivity {

    GoogleApiClient mGoogleApiClient;   //Google login
    private LoginButton FbLogin;       //facebook LogIn
    private CallbackManager callbackManager;  //fb
    private int RC_SIGN_IN = 5678; //google

    private String RegMail,RegPass,pass,mail;
    private EditText eMail,ePass;
    private int optlog=0;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        eMail = (EditText) findViewById(R.id.eEmail);
        ePass = (EditText) findViewById(R.id.ePass);


//--------------------------------------FACEBOOK

        //FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        FbLogin = (LoginButton) findViewById(R.id.login_button);
        FbLogin.setReadPermissions(Arrays.asList("email"));

        FbLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                goMainActivity();
            }
            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(),"Cacncelado FB", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(),"Error FB", Toast.LENGTH_SHORT).show();

            }
        });

//---------------------------------FACEBOOK END

//-------------------------------------GOOGLE

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //.requestIdToken(getString(R.string.default_web_client_id));
                .requestEmail()
                .requestProfile()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(getApplicationContext(),"Error Google",Toast.LENGTH_SHORT).show();
                    }
                } /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

//------------------------GOOGLE END

//RECOVER DATA
        prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        editor = prefs.edit();
        prefs.getString("user",null);
        prefs.getString("pass",null);

        //o del splash o de otro
        //Bundle extras = getIntent().getExtras();
        //Splash = extras.getBoolean("Splash");

       // if (Splash) return;

        //RegMail = extras.getString("mail");
        //RegPass = extras.getString("pass");



    }
//GOOGLE
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void goMainActivity() {
        Intent intent = new Intent(activity_login.this,MainActivity.class);
       // intent.putExtra("mail",RegMail);
       // intent.putExtra("pass",RegPass);
        //Toast.makeText(getApplicationContext(),"ok",Toast.LENGTH_SHORT).show();
        startActivity(intent);
        //finish();
        //preferencias compartidas
        editor.putInt("optlog",1);//2 es optolog
        editor.commit();

    }

    // activity to register user
    public void bRegister(View view){
        Intent intent = new Intent(activity_login.this,activity_registro.class);
        startActivityForResult(intent,1234); //distinguir quien me llamo
    }

    //return from regiter entered
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 1234 && resultCode == RESULT_OK){
            RegMail = data.getExtras().getString("mail");
            RegPass = data.getExtras().getString("pass");

            Toast.makeText(this,RegMail,Toast.LENGTH_SHORT).show();
            optlog=1;

        } else if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
            goMainActivity();
            optlog=3;

        }else{
            callbackManager.onActivityResult(requestCode,resultCode,data);
            optlog=2;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handleSignInResult(GoogleSignInResult result) {
            Log.d("goole", "handleSignInResult:" + result.isSuccess());
            if (result.isSuccess()) {
                // Signed in successfully, show authenticated UI.
                GoogleSignInAccount acct = result.getSignInAccount();
                Toast.makeText(getApplicationContext(),acct.getDisplayName(),Toast.LENGTH_SHORT).show();

            } else {
                // Signed out, show unauthenticated UI.
                Toast.makeText(getApplicationContext(),"errroer",Toast.LENGTH_SHORT).show();
            }
    }

    public void bLogin(View view){//button login pressed

        mail = eMail.getText().toString();
        pass = ePass.getText().toString();

        if(!(mail.equals(RegMail))) return;
        if (!(pass.equals(RegPass))) return;

        Intent intent = new Intent(activity_login.this,MainActivity.class);
        //intent.putExtra("mail", RegMail);
        //intent.putExtra("pass",RegPass);
        startActivity(intent);
        finish();

    }
}
/*
try {
        PackageInfo info = getPackageManager().getPackageInfo(
        "com.udea.santiagoceron.appv0",
        PackageManager.GET_SIGNATURES);
        for (Signature signature : info.signatures) {
        MessageDigest md = MessageDigest.getInstance("SHA");
        md.update(signature.toByteArray());
        Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
        }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
*/