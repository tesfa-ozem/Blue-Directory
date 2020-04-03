package com.example.servicesforhome.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.*
import com.example.servicesforhome.Dashboard
import com.example.servicesforhome.Gps
import com.example.servicesforhome.R
import com.example.servicesforhome.Ui.LoadingDialoge
import com.example.servicesforhome.http.Api
import com.example.servicesforhome.models.DataToken
import com.example.servicesforhome.utilities.ErrorHandler.volleyHandler
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_sign_in.view.*
import kotlinx.android.synthetic.main.response_modal.view.*
import org.json.JSONObject


class SignIn : Fragment(), View.OnClickListener {
    private lateinit var errorView: View
    private lateinit var successDialog: AlertDialog
    private lateinit var failedDialog:AlertDialog
    private lateinit var factory: LayoutInflater
    override fun onClick(p0: View?) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    lateinit var main_view: View

    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        main_view = inflater.inflate(R.layout.fragment_sign_in, container, false)
        factory = LayoutInflater.from(context)
        errorView = factory.inflate(R.layout.response_modal, null)
        successDialog = AlertDialog.Builder(context).create()
        failedDialog = AlertDialog.Builder(context).create()

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
        try {
            val header = HashMap<String, String>()
            val jsonBody = JSONObject()
            val creds: String = String.format("%s:%s", userName, password)
            val auth = "Basic " + Base64.encodeToString(creds.toByteArray(), Base64.NO_WRAP)
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
                        errorView.error_message.text = volleyHandler(error)

                        errorView.error_dissmis_button.setOnClickListener {
                            failedDialog.dismiss()
                        }

                        failedDialog.setCancelable(false)

                        failedDialog.setView(errorView)
                        failedDialog.show()

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
