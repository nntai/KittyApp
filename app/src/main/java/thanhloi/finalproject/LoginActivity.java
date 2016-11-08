package thanhloi.finalproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;

import thanhloi.finalproject.Firebase.InterfaceAfterSetupAcc;
import thanhloi.finalproject.Firebase.InterfaceLoginReturn;
import thanhloi.finalproject.Firebase.Store;
import thanhloi.finalproject.Firebase.UserFirebase;
import thanhloi.finalproject.Tool.NotiDate;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener, InterfaceAfterSetupAcc, InterfaceLoginReturn {

    ProgressDialog progressDialog;
    static Context context;
    LoginButton fbloginButton;
    SignInButton ggloginButton;
    TextView textv;
    CallbackManager callbackManager;
    String TAG ="tag";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    GoogleApiClient mGoogleApiClient;
    int RC_SIGN_IN=9001;
    boolean date=false;
    boolean storeme=false;
    boolean storepartner=false;
    boolean setup=false;

    private void LoginSucess(FirebaseUser user){
        if(user.getEmail()==null) user.updateEmail(user.getUid()+"@final.com");
        progressDialog=ProgressDialog.show(this, "Please wait.", "Downloading Infomation..!", true);
        UserFirebase.setupAcc(user,this);
        if(user.getPhotoUrl()!=null) UserFirebase.setAvatar(user,user.getPhotoUrl().toString());
        NotiDate notiDate=new NotiDate(this);
        Store store=new Store(this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        context=getBaseContext();
        callbackManager = CallbackManager.Factory.create();

        //FirebaseAuth.getInstance().signOut();

        textv=(TextView)findViewById(R.id.textv);
        //listener neu co login.
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    try{
                        LoginSucess(user);
                    }
                    catch (NullPointerException e){
                        textv.setText(user.getDisplayName().toString());
                    }
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        //set gg login

        ggloginButton=(SignInButton)findViewById(R.id.gglogin_button);
        ggloginButton.setOnClickListener(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        //set fb login
        fbloginButton=(LoginButton)findViewById(R.id.fblogin_button);

        // Xin 1 quyền đọc
        //loginButton.setReadPermissions("user_friends");
        // Xin nhiều quyền đọc
        fbloginButton.setReadPermissions(Arrays.asList("user_friends", "public_profile","email"));
        // Xin quyền đăng bài (2 cách tương tự quyền đọc)
        //loginButton.setPublishPermissions("publish_actions");

        fbloginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
            }
        });
        mAuth = FirebaseAuth.getInstance();

    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);

            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
            //FirebaseAuth.getInstance().signOut();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void signIn() {
        //Toast.makeText(MainActivity.this,"signin G+",Toast.LENGTH_SHORT).show();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gglogin_button:
                signIn();
                break;
            // ...
        }
    }

    static public String getStringResourceByName(String aString) {
        if (context==null) {
            Log.e("context","null context");
        }else {
            String packageName = context.getPackageName();
            int resId = context.getResources()
                    .getIdentifier(aString, "string", packageName);
            if (resId == 0) {
                return aString;
            } else {
                return context.getString(resId);
            }
        }
        return aString;
    }

    void check(){
        if (setup&&storepartner&&storeme&&date){
            progressDialog.dismiss();

            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
        }
        String res = null;
        if (storeme) res=res+"storeme=true";
        else res=res+"storeme=false";
        res=res+" ";
        if (storepartner) res=res+"storepartner=true";
        else res=res+"storepartner=false";
        if (setup) res=res+"setup=true";
        else res=res+"setup=false";
        if (date) res=res+"date=true";
        else res=res+"date=false";
        Log.e("check",res);
    }
    @Override
    public void AfterSetup() {
        setup=true;
        Log.e("check","setup");
        check();
    }

    @Override
    public void afterLogin(int code) {
        switch (code){
            case 1: storeme=true; Log.e("check","storeme"); break;
            case 2: storepartner=true; Log.e("check","storepartner"); break;
            case 3: Log.e("check","date"); date=true;
        }
        check();
    }
}
