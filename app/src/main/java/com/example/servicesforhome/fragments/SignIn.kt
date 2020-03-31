package com.example.servicesforhome.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.android.volley.VolleyError
import com.example.servicesforhome.Dashboard
import com.example.servicesforhome.Gps

import com.example.servicesforhome.R
import com.example.servicesforhome.Ui.ErrorDialoge
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


        main_view.sign_in_button.setOnClickListener {
            if (main_view.email_input.text.toString()
                    .isNotEmpty() && main_view.password_input.text.toString().isNotEmpty()
            ) {
                if (main_view.password_input.text.toString().isValidPassword()) {
                    signIn(
                        main_view.email_input.text.toString(),
                        main_view.password_input.text.toString()
                    )
                } else {
                    main_view.password_input.error = "Try again"
                }

            } else {
                main_view.email_input.validate("Valid email address required") { s -> s.isValidEmail() }
                main_view.password_input.validate("Password must be 6 or more digits") { s -> s.isValidPassword() }
            }


        }



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
                if (user != null) {
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

    private fun signIn(userName: String, password: String) {
        val newFragment = LoadingDialoge()
        newFragment.show(childFragmentManager, "missiles")
        val errorFragment = ErrorDialoge()
        try {
            val header = HashMap<String, String>()
            val jsonBody = JSONObject()
            var creds: String = String.format("%s:%s", userName, password)
            var auth = "Basic " + Base64.encodeToString(creds.toByteArray(), Base64.NO_WRAP)
            header["Authorization"] = auth
            Api.getVolley(
                activity?.application, Api.POST, "token", "", object : Api.VolleyCallback {
                    override fun onSuccess(result: String) {
                        newFragment.dismiss()
                        Api.clearValue(activity?.application, "Token")
                        val gson = Gson()
                        val token = gson.fromJson(result, DataToken::class.java)

                        Api.save(activity?.application, "Token", token.token)
                        startActivity(Intent(context, Gps::class.java))
                        activity?.finish()
                    }

                    override fun onError(error: VolleyError) {
                        newFragment.dismiss()
                        Toast.makeText(context,error.toString(),Toast.LENGTH_LONG).show()
                        errorFragment.show(childFragmentManager, "missiles")

                    }
                }, Api.localUrl, form_data = null, headers = header
            )
        } catch (e: Exception) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
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
                if (user != null) {
                    startActivity(Intent(context, Dashboard::class.java))
                    activity?.finish()
                }
            } else {
                signOut()
                // If sign in fails, display a message to the user.
                Log.w(TAG, "signInWithCredential:failure", task.exception)
                Toast.makeText(
                    context, "Authentication failed.",
                    Toast.LENGTH_SHORT
                ).show()
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

    fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                afterTextChanged.invoke(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    fun EditText.validate(message: String, validator: (String) -> Boolean) {
        this.afterTextChanged {
            this.error = if (validator(it)) null else message
        }
        this.error = if (validator(this.text.toString())) null else message
    }

    fun String.isValidEmail(): Boolean =
        this.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

    fun String.isValidPassword(): Boolean = this.isNotEmpty() && this.length >= 4

    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }
    // TODO: Rename method, update argument and hook method into UI event

}
