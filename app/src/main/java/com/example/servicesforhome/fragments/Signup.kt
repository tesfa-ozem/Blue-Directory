package com.example.servicesforhome.fragments

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.example.servicesforhome.http.Api
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.android.synthetic.main.fragment_signup.view.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class Signup : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var auth: FirebaseAuth
    lateinit var main_view:View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {




        main_view = inflater.inflate(R.layout.fragment_signup, container, false)


        main_view.sign_up_button.setOnClickListener{
            if(main_view.email_input.text.toString().isNotEmpty()&&main_view.password_input.text.toString().isNotEmpty()&&main_view.confirm_password_input.text.toString().isNotEmpty()){
                if(main_view.confirm_password_input.text.toString()== main_view.password_input.text.toString()){
                    createAccount(main_view.email_input.text.toString().trim(),main_view.password_input.text.toString().trim())
                }else{
                    main_view.password_input.error = "Did not match"
                }

            }else{
                main_view.email_input.validate("Valid email address required") { s -> s.isValidEmail() }
                main_view.password_input.validate("Password must be 6 or more digits"){s ->s.isValidPassword()}
            }



        }
        return main_view
    }

    fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                afterTextChanged.invoke(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        })
    }

    fun EditText.validate(message: String, validator: (String) -> Boolean) {
        this.afterTextChanged {
            this.error = if (validator(it)) null else message
        }
        this.error = if (validator(this.text.toString())) null else message
    }

    fun String.isValidEmail(): Boolean
            = this.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

    fun String.isValidPassword():Boolean
            =this.isNotEmpty()&&this.length>=6

    @SuppressLint("ShowToast")
    fun createAccount(email:String, password:String){
        try {

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    val firebaseUser = auth.currentUser!!
                    userDetaila(firebaseUser.displayName,email,firebaseUser.phoneNumber)
                    Toast.makeText(context,email,Toast.LENGTH_LONG).show()
                    startActivity(Intent(context,
                        Gps::class.java))
                    activity?.finish()

                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    //Log.e("Failed to autheniticate","Failed")
                }
            }
        }catch (ex:FirebaseAuthException){
            Log.e("createAccount",ex.localizedMessage)
        }


    }

    fun userDetaila(name:String?,email: String,phoneNumber:String?){
        var params = HashMap<String, String>()
        params.put("name", name.toString())
        params.put("email", phoneNumber.toString())
        params.put("phone",email)
        Api.getVolley(activity?.application,Api.POST,"user","",object :Api.VolleyCallback{
            override fun onSuccess(result: String){
                Log.i("response",result)




            }

            override fun onError(error: VolleyError) {

            }
        },Api.URL,params)
    }



}
