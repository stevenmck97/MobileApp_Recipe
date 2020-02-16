package ie.wit.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.firebase.ui.auth.AuthUI
import com.google.android.material.navigation.NavigationView
import ie.wit.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.home.*
import org.jetbrains.anko.toast


//creates class to make use of AppCompatActivity to allow for the use of Activities.
// also implements class Navigation view to allow for use of Nav drawer
class Home : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener {

    val signOut = SignInActivity()

//adds toobar to the action bar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)
        setSupportActionBar(toolbar)

        navView.setNavigationItemSelectedListener(this)

//creates toggle variable to easily use ActionBarDrawerToggle class
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

    }

//assigns navigations items to an activity so when they are pressed they open a new activity
    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.nav_recipe -> startActivity(Intent(this, RecipeActivity::class.java))
            R.id.nav_report -> startActivity(Intent(this, RecipeListActivity::class.java))


            else -> toast("You Selected Something Else")
        }

    //closes the nav drawer if pressed again
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_recipe, menu)
        return true
    }


//allows user to sign out.
//made with help from: https://www.youtube.com/watch?v=7SZO3bT1M0I&t=418s
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.btn_sign_out -> btn_sign_out.setOnClickListener {
                //logout
                AuthUI.getInstance().signOut(signOut)
                    .addOnCompleteListener {
                        btn_sign_out.isEnabled = true
                        signOut.showSignInOptions()
                    }.addOnFailureListener {
                        Toast.makeText(this, "" + it.message, Toast.LENGTH_SHORT).show()
                    }
            }


        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START)
        else
            super.onBackPressed()
    }

}