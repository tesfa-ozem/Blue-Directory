package com.example.servicesforhome.fragments


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.android.volley.VolleyError
import com.example.servicesforhome.R
import com.example.servicesforhome.Ui.LoadingDialoge
import com.example.servicesforhome.http.Api
import com.example.servicesforhome.utilities.ErrorHandler
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.android.synthetic.main.activity_success_dialoge.view.*
import kotlinx.android.synthetic.main.fragment_signup.view.*
import kotlinx.android.synthetic.main.response_modal.view.*
import org.json.JSONObject


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class Signup : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var auth: FirebaseAuth
    lateinit var main_view: View
    private lateinit var errorView: View
    private lateinit var successView: View
    private lateinit var successDialog: AlertDialog
    private lateinit var failedDialog:AlertDialog
    private lateinit var factory: LayoutInflater
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
        factory = LayoutInflater.from(context)
        errorView = factory.inflate(R.layout.response_modal, null)
        successView = factory.inflate(R.layout.activity_success_dialoge,null)
        successDialog = AlertDialog.Builder(context).create()
        failedDialog = AlertDialog.Builder(context).create()
        main_view.sign_up_button.setOnClickListener {
            if (main_view.email_input.text.toString()
                    .isNotEmpty() && main_view.password_input.text.toString()
                    .isNotEmpty() && main_view.confirm_password_input.text.toString().isNotEmpty()
            ) {
                if (main_view.confirm_password_input.text.toString() == main_view.password_input.text.toString()) {
                    createAccount(
                        main_view.username_input.text.toString().trim(),
                        main_view.email_input.text.toString().trim(),
                        main_view.password_input.text.toString().trim()
                    )
                } else {
                    main_view.password_input.error = "Did not match"
                }

            } else {
                main_view.email_input.validate("Valid email address required") { s -> s.isValidEmail() }
                main_view.password_input.validate("Password must be 6 or more digits") { s -> s.isValidPassword() }
            }


        }
        return main_view
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

    fun String.isValidPassword(): Boolean = this.isNotEmpty() && this.length >= 6

    @SuppressLint("ShowToast")
    fun createAccount(userName: String, email: String, password: String) {
        val newFragment = LoadingDialoge()
        newFragment.show(childFragmentManager, "missiles")

        val header = HashMap<String, String>()
        try {
            val jsonBody = JSONObject()
            jsonBody.put("username", userName)
            jsonBody.put("email", email)
            jsonBody.put("password", password)
            val mRequestBody = jsonBody.toString()
            Api.getVolley(
                activity?.application,
                Api.POST,
                "sign_up",
                mRequestBody,
                object : Api.VolleyCallback {
                    override fun onSuccess(result: String) {
                        Log.i("response", result)
                        newFragment.dismiss()
                        successView.success_message_id.text = "Successfully Created"
                        successView.success_dismiss_button.setOnClickListener {
                            successDialog.dismiss()
                        }
                        successDialog.setCancelable(false)
                        successDialog.setView(successView)
                        successDialog.show()

                    }

                    override fun onError(error: VolleyError) {
                        newFragment.dismiss()
                        errorView.error_message.text = ErrorHandler.volleyHandler(error)

                        errorView.error_dissmis_button.setOnClickListener {
                            failedDialog.dismiss()
                        }

                        failedDialog.setCancelable(false)

                        failedDialog.setView(errorView)
                        failedDialog.show()
                    }
                },
                Api.localUrl,
                form_data = null,
                headers = header
            )

        } catch (ex: FirebaseAuthException) {
            Log.e("createAccount", ex.localizedMessage)
        }


    }


}
