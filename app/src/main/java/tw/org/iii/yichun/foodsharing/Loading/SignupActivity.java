package tw.org.iii.yichun.foodsharing.Loading;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import tw.org.iii.yichun.foodsharing.Global.Utils;
import tw.org.iii.yichun.foodsharing.Global.VolleyApp;
import tw.org.iii.yichun.foodsharing.R;

/**
 * 註冊頁面
 */
public class SignupActivity extends AppCompatActivity {

    @BindView(R.id.account)
    EditText account;
    @BindView(R.id.passwd)
    EditText passwd;
    @BindView(R.id.passwd_verify)
    EditText passwdVerify;
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.captcha)
    EditText captcha;


    private String getaccount;
    private String getphone;
    private String getpassword;
    private Date createTime;

    private boolean verifyOK;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);


    }
    /**
     * 查詢帳號是否重複
     */
    private void verify(){
        String url = "http://"+ Utils.ip +"/FoodSharing_war/Sql_Signp_Verify_Servlet";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        verify_check(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String > params = new HashMap<>();
                params.put("account",getaccount);
                params.put("phone",getphone);
                return params;
            }
        };
        VolleyApp.queue.add(request);
    }

    /**
     * 讀出後台網頁是否有值
     * @param count
     * @return
     */
    private Boolean verify_check(String count){
        if (count!=null){
            verifyOK = true;
        }else {
            verifyOK = false;
        }
        return  verifyOK;
    }


    /**
     * 將帳密加入到sql中保存
     * @param view
     */
    public void Signup_Btn(View view) {

        verify();

        String url = "";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();




                return super.getParams();
            }
        };


        VolleyApp.queue.add(request);
    }


    /**
     * 手機驗證碼
     * @param view
     */
    public void Verify_Btn(View view) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+1 123456",        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {

                    }
                });

//        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//
//            @Override
//            public void onVerificationCompleted(PhoneAuthCredential credential) {
//                // This callback will be invoked in two situations:
//                // 1 - Instant verification. In some cases the phone number can be instantly
//                //     verified without needing to send or enter a verification code.
//                // 2 - Auto-retrieval. On some devices Google Play services can automatically
//                //     detect the incoming verification SMS and perform verification without
//                //     user action.
//                Log.d(TAG, "onVerificationCompleted:" + credential);
//
//                signInWithPhoneAuthCredential(credential);
//            }
//
//            @Override
//            public void onVerificationFailed(FirebaseException e) {
//                // This callback is invoked in an invalid request for verification is made,
//                // for instance if the the phone number format is not valid.
//                Log.w(TAG, "onVerificationFailed", e);
//
//                if (e instanceof FirebaseAuthInvalidCredentialsException) {
//                    // Invalid request
//                    // ...
//                } else if (e instanceof FirebaseTooManyRequestsException) {
//                    // The SMS quota for the project has been exceeded
//                    // ...
//                }
//
//                // Show a message and update the UI
//                // ...
//            }
//
//            @Override
//            public void onCodeSent(@NonNull String verificationId,
//                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
//                // The SMS verification code has been sent to the provided phone number, we
//                // now need to ask the user to enter the code and then construct a credential
//                // by combining the code with a verification ID.
//                Log.d(TAG, "onCodeSent:" + verificationId);
//
//                // Save verification ID and resending token so we can use them later
//                mVerificationId = verificationId;
//                mResendToken = token;
//
//                // ...
//            }
//        };

    }
}
