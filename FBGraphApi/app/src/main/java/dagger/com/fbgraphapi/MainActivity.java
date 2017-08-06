package dagger.com.fbgraphapi;

import com.calldorado.Calldorado;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    LoginButton loginButton;
    CallbackManager callbackManager;
    private String firstName, lastName, email, birthday, gender, userId;



    private void initializeCalldorado() {
        Calldorado.startCalldorado(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeCalldorado();

        setContentView(R.layout.activity_main);
        printHashKey();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        callbackManager = CallbackManager.Factory.create();

        loginButton.setReadPermissions( Arrays.asList( "public_profile", "user_posts" ) );

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                GraphRequest request = GraphRequest.newMeRequest( loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback()
                {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response )
                    {

                        try
                        {
                            userId = object.getString( "id" );
                            if ( object.has( "first_name" ) )
                            {
                                firstName = object.getString( "first_name" );
                            }
                            if ( object.has( "last_name" ) )
                            {
                                lastName = object.getString( "last_name" );
                            }
                            if ( object.has( "email" ) )
                            {
                                email = object.getString( "email" );
                            }
                            if ( object.has( "birthday" ) )
                            {
                                birthday = object.getString( "birthday" );
                            }
                            if ( object.has( "gender" ) )
                            {
                                gender = object.getString( "gender" );
                            }

                            Intent main = new Intent( MainActivity.this, UserCredActivity.class );
                            main.putExtra( "fname", firstName );
                            main.putExtra( "lname", lastName );
                            main.putExtra( "email" , email );
                            main.putExtra("gender" , gender);
                            main.putExtra("birthday" , birthday);
                            startActivity( main );
                            finish();
                        }
                        catch ( JSONException e )
                        {
                            e.printStackTrace();
                        }


                    }
                } );
                //Here we put the requested fields to be returned from the JSONObject
                Bundle parameters = new Bundle();
                parameters.putString( "fields", "id, first_name, last_name, email, birthday, gender" );
                request.setParameters( parameters );
                request.executeAsync();

                LoginManager.getInstance().logInWithReadPermissions( MainActivity.this, Arrays.asList( "public_profile", "user_posts" ) );
            }

            @Override
            public void onCancel() {
                // App code
                Toast.makeText(MainActivity.this,"fail",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Toast.makeText(MainActivity.this,exception.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    public void printHashKey() {
            String TAG = "HASH_KEY";
        try {

            PackageInfo info = getPackageManager().getPackageInfo("dagger.com.fbgraphapi", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i(TAG, "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
        }
    }
}
