/*Most of the code in this class was made by following this youtube tutorial: https://www.youtube.com/watch?v=7SZO3bT1M0I&t=418s
Title: Android Tutorial Kotlin Firebase Authentication
Author: Shakhawat Hossen
Date: <date>
Code version: <code version>
Availability: https://www.youtube.com/watch?v=7SZO3bT1M0I&t=418s
 */


package ie.wit.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.AuthUI.*
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class SignInActivity : AppCompatActivity() {


    private val MY_REQUEST_CODE = 1000
    lateinit var providers: List<IdpConfig>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ie.wit.R.layout.activity_main)

        btn_sign_out.setOnClickListener {
            //logout
            AuthUI.getInstance().signOut(this@SignInActivity)
                .addOnCompleteListener {
                    btn_sign_out.isEnabled = false
                    showSignInOptions()
                }.addOnFailureListener {
                    Toast.makeText(this, "" + it.message, Toast.LENGTH_SHORT).show()
                }
        }


        //Init Provider
        providers = Arrays.asList<IdpConfig>(
            IdpConfig.EmailBuilder().build(),
            IdpConfig.PhoneBuilder().build(),
            IdpConfig.GoogleBuilder().build()
        )

        showSignInOptions()
    }

     fun showSignInOptions() {
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setTheme(ie.wit.R.style.MyTheme).build(), MY_REQUEST_CODE)
    }


//I modified this function by checking if there was a user signed in and if there was I redirected you to the home activity
     override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MY_REQUEST_CODE){
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK){
                val user = FirebaseAuth.getInstance().currentUser
                if (user != null){
                    startActivity(Intent(this@SignInActivity, Home::class.java))
                }

                Toast.makeText(this, "" + user!!.email, Toast.LENGTH_SHORT).show()

                btn_sign_out.isEnabled = true
            }
            else{
                Toast.makeText(this, "" + response!!.error!!.message, Toast.LENGTH_SHORT).show()
            }
        }
    }




}