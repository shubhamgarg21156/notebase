package com.example.notebase;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import com.google.firebase.firestore.FirebaseFirestore;

public class Register extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;
    private static final String TAG = "Error";
    ProgressBar progressbar;
    FirebaseAuth fAuth;
    FirebaseFirestore firebaseFirestore;
    Button Register;
    EditText Name, Email, Password, ConfirmPassword;
    ImageView Google, facebook;
    private GoogleSignInClient mGoogleSignInClient;
    private LoginButton loginButton;
    private CallbackManager mCallbackManager;
    public FirebaseFirestore firebaseFireStore;

    static class UserDoc {
        String displayName;
        String email;
        String photoURL;

        public UserDoc(String displayName, String email, String photoURL) {
            this.displayName = displayName;
            this.email = email;
            this.photoURL = photoURL;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getEmail() {
            return email;
        }

        public String getPhotoURL() {
            return photoURL;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getWindow().setStatusBarColor(Color.BLACK);

        Register = findViewById(R.id.login);
        Name = findViewById(R.id.Name);
        Email = findViewById(R.id.email);
        Password = findViewById(R.id.password);
        ConfirmPassword = findViewById(R.id.ConfirmPassword);
        progressbar = findViewById(R.id.progressbar);
        Google = findViewById(R.id.google);
        fAuth = FirebaseAuth.getInstance();
        facebook = findViewById(R.id.facebook);

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = Name.getText().toString().trim();
                String email = Email.getText().toString().trim();
                String password = Password.getText().toString().trim();
                String confirmPassword = ConfirmPassword.getText().toString().trim();
                firebaseFireStore = FirebaseFirestore.getInstance();

                if (TextUtils.isEmpty(name)) {
                    Name.setError("Name is Required");
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    Email.setError("Email is Required");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Password.setError("Password is Required");
                    return;
                }
                if (TextUtils.isEmpty(confirmPassword)) {
                    ConfirmPassword.setError("Confirm the Password");
                    return;
                }
                if (password.length() < 6) {
                    Password.setError("Password should be of atleast 6 characters");
                }
                if (!password.equals(confirmPassword)) {
                    Toast.makeText(Register.this, "Password and ConfirmPassword doesn't match", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressbar.setVisibility(View.VISIBLE);

                if (fAuth.getCurrentUser() != null)
                {
                    if(fAuth.getCurrentUser().isEmailVerified())
                        Toast.makeText(Register.this, "Already account exists with that id", Toast.LENGTH_SHORT).show();
                    else{
                        final FirebaseUser Fuser = fAuth.getCurrentUser();
                        Fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                firebaseFireStore = FirebaseFirestore.getInstance();
                                Toast.makeText(Register.this, "verification link sent to your mail", Toast.LENGTH_SHORT).show();
                                progressbar.setVisibility(View.INVISIBLE);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Register.this, "Error", Toast.LENGTH_SHORT).show();
                                progressbar.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                    progressbar.setVisibility(View.INVISIBLE);
                    return;

                }

                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            UserDoc obj = new UserDoc(Name.getText().toString(), Email.getText().toString(), "https://www.worldfuturecouncil.org/wp-content/uploads/2020/02/dummy-profile-pic-300x300-1.png");

                            final FirebaseUser Fuser = fAuth.getCurrentUser();
                            try {

                                firebaseFireStore.document("users/" + Fuser.getUid()).set(obj);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            assert Fuser != null;
                            Fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {


                                    firebaseFireStore = FirebaseFirestore.getInstance();

                                    Toast.makeText(Register.this, "verification link sent to your mail", Toast.LENGTH_SHORT).show();
                                    progressbar.setVisibility(View.INVISIBLE);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Register.this, "Error", Toast.LENGTH_SHORT).show();
                                    progressbar.setVisibility(View.INVISIBLE);
                                }
                            });
                        } else {
                            Toast.makeText(Register.this, "Error occured", Toast.LENGTH_SHORT).show();
                            progressbar.setVisibility(View.INVISIBLE);
                        }

                    }
                });


            }
        });

        createRequest();
        Google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

//        mCallbackManager = CallbackManager.Factory.create();
//        LoginButton loginButton = mBinding.buttonFacebookLogin;
//        loginButton.setReadPermissions("email", "public_profile");
//
//        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                Log.d(TAG, "facebook:onSuccess:" + loginResult);
//                handleFacebookAccessToken(loginResult.getAccessToken());
//            }
//
//            private void handleFacebookAccessToken(AccessToken accessToken) {
//
//            }
//
//            @Override
//            public void onCancel() {
//                Log.d(TAG, "facebook:onCancel");
//
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//                Log.d(TAG, "facebook:onError", error);
//                // ...
//            }
//        });

    }
//    private void handleFacebookAccessToken(AccessToken token) {
//        Log.d(TAG, "handleFacebookAccessToken:" + token);
//
//        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
//        fAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithCredential:success");
//                            FirebaseUser user = fAuth.getCurrentUser();
//                            //updateUI(user);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "signInWithCredential:failure", task.getException());
//                            Toast.makeText(Register.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                            //updateUI(null);
//                        }
//
//                        // ...
//                    }
//                });
//    }

    private void createRequest() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signIn() {

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                assert account != null;
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, "ERROR " + e.getMessage(), Toast.LENGTH_SHORT).show();
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        fAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = fAuth.getCurrentUser();


                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithCredential:failure", task.getException());


                        }
                    }
                });
    }


}
