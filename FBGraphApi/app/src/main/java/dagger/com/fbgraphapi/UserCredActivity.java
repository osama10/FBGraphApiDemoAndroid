package dagger.com.fbgraphapi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.facebook.login.LoginManager;

public class UserCredActivity extends AppCompatActivity {

    TextView tvFname;
    TextView tvLname;
    TextView tvEmail;
    TextView tvBday;
    TextView tvGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_cred);
        initViews();
        Bundle inBundle = getIntent().getExtras();

        tvFname.setText("First Name " + inBundle.getString("fname"));
        tvLname.setText("Last Name " + inBundle.getString("lname"));
        tvBday.setText("Birth day " + inBundle.getString("birthday"));


    }


    private void logout()
    {
        LoginManager.getInstance().logOut();
//        Intent login = new Intent( MainActivity.this, LoginActivity.class );
//        startActivity( login );
//        finish();
    }


    @Override
    protected void onStop() {
        super.onStop();

        logout();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void  initViews(){

        tvFname = (TextView) findViewById(R.id.fname);
        tvLname = (TextView) findViewById(R.id.lname);
        tvEmail = (TextView) findViewById(R.id.email);
        tvBday = (TextView) findViewById(R.id.bday);
        tvGender = (TextView) findViewById(R.id.gender);


    }
}
