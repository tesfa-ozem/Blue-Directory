package com.example.servicesforhome.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.volley.VolleyError
import com.example.servicesforhome.Dashboard
import com.example.servicesforhome.Gps

import com.example.servicesforhome.R
import com.example.servicesforhome.Ui.LoadingDialoge
import com.example.servicesforhome.http.Api
import com.example.servicesforhome.models.DataToken
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.TokenData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_sign_in.view.*
import org.json.JSONObject
import java.lang.Exception

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [SignIn.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [SignIn.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignIn : Fragment(), View.OnClickListener {
    override fun onClick(p0: View?) {

    }

    // TODO: Rename and change types of parameters
    private lateinit var callbackManager: CallbackManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    private lateinit var googleSignInClient: GoogleSignInClient

    lateinit var main_view: View

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        main_view = inflater.inflate(R.layout.fragment_sign_in, container, false)
        /*val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        main_view.facebook_login.setOnClickListener{

        }

        callbackManager = CallbackManager.Factory.create()*/

        main_view.sign_in_button.setOnClickListener {
            signIn(main_view.email_input.text.toString(),main_view.password_input.text.toString())


        }

        /*main_view.facebook_login.setReadPermissions("email", "public_profile")
        main_view.facebook_login.registerCallback(callbackManager, object :
            FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.d(TAG, "facebook:onSuccess:$loginResult")
                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {
                Log.d(TAG, "facebook:onCancel")
                // [START_EXCLUDE]
                //updateUI(null)
                // [END_EXCLUDE]
            }

            override fun onError(error: FacebookException) {
                Log.d(TAG, "facebook:onError", error)
                // [START_EXCLUDE]
                //updateUI(null)
                // [END_EXCLUDE]
            }
        })
        googleSignInClient = GoogleSignIn.getClient(context!!.applicationContext, gso)*/


        // Inflate the layout for this fragment
        return main_view
    }


    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.id!!)

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener { task: Task<AuthResult> ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d(TAG, "signInWithCredential:success")
                val user = auth.currentUser
                if(user!=null){
                    startActivity(Intent(context, Dashboard::class.java))
                    activity?.finish()
                }
            } else {
                signOut()
                // If sign in fails, display a message to the user.
                Log.w(TAG, "signInWithCredential:failure", task.exception)
                Snackbar.make(main_view, "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
                //updateUI(null)
            }


        }
    }

    private fun signIn(userName:String,password:String) {
        val newFragment = LoadingDialoge()
        newFragment.show(childFragmentManager, "missiles")
        /*startActivity(Intent(context,Gps::class.java))
        activity?.finish()*/
       /* val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)*/

        try {
            val header = HashMap<String,String>()
            val jsonBody = JSONObject()
            var creds:String = String.format("%s:%s",userName,password)
            var auth = "Basic " + Base64.encodeToString(creds.toByteArray(), Base64.NO_WRAP)
            header["Authorization"] = auth
            Api.getVolley(activity?.application, Api.POST, "token","", object : Api.VolleyCallback {
                override fun onSuccess(result: String) {
                    newFragment.dismiss()
                    Api.clearValue(activity?.application,"Token")
                    val gson = Gson()
                    val token = gson.fromJson(result, DataToken::class.java)

                    Api.save(activity?.application,"Token",token.token)
                    startActivity(Intent(context, Gps::class.java))
                    activity?.finish()
                }

                override fun onError(error: VolleyError) {
                    newFragment.dismiss()
                }
            }, Api.URL,form_data = null,headers = header
            )
        }catch (e:Exception){
            Toast.makeText(context,e.toString(),Toast.LENGTH_LONG).show()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
                // ...
            }
        }
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d(TAG, "handleFacebookAccessToken:$token")
        // [START_EXCLUDE silent]
        //showProgressDialog()
        // [END_EXCLUDE]

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential).addOnCompleteListener { task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    if(user!=null){
                        startActivity(Intent(context, Dashboard::class.java))
                        activity?.finish()
                    }
                } else {
                    signOut()
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(context, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    //updateUI(null)
                }

                // [START_EXCLUDE]
                //hideProgressDialog()
                // [END_EXCLUDE]
            }
    }


    fun signOut() {
       auth.signOut()
       LoginManager.getInstance().logOut()
   }

    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }
    // TODO: Rename method, update argument and hook method into UI event

}
