package ie.wit.activities

import android.content.Intent
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import ie.wit.R
import ie.wit.fragments.*
import ie.wit.main.RecipesApp
import ie.wit.utils.*
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.home.*
import kotlinx.android.synthetic.main.nav_header_home.view.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class Home : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener {

    lateinit var ft: FragmentTransaction
    lateinit var app: RecipesApp


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)
        setSupportActionBar(toolbar)
        app = application as RecipesApp

//        app.currentLocation = Location("Default").apply {
//            latitude = 52.245696
//            longitude = -7.139102
//        }

        val bottomNavView: BottomNavigationView = findViewById(R.id.navigation)
        bottomNavView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)


        app.locationClient = LocationServices.getFusedLocationProviderClient(this)

        if(checkLocationPermissions(this)) {
            // todo get the current location
            setCurrentLocation(app)
        }

        navView.setNavigationItemSelectedListener(this)
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.getHeaderView(0).nav_header_email.text = app.auth.currentUser?.email

        //Checking if Google User, upload google profile pic

        checkExistingPhoto(app,this)
        checkExistingPhoto2(app,this)

        navView.getHeaderView(0).imageView
            .setOnClickListener { showImagePicker(this,1) }

        ft = supportFragmentManager.beginTransaction()

        val fragment = RecipeFragment.newInstance()
        ft.replace(R.id.homeFrame, fragment)
        ft.commit()


    }


    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_recipe -> {
                    navigateTo(RecipeFragment.newInstance())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_report -> {
                    navigateTo(ReportFragment.newInstance())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_report_all -> {
                    navigateTo(ReportAllFragment.newInstance())
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.nav_recipe ->
                navigateTo(RecipeFragment.newInstance())
            R.id.nav_report ->
                navigateTo(ReportFragment.newInstance())
            R.id.nav_report_all ->
                navigateTo(ReportAllFragment.newInstance())
            R.id.nav_aboutus ->
                navigateTo(AboutUsFragment.newInstance())
            R.id.nav_favourites ->
                navigateTo(FavouritesFragment.newInstance())
            R.id.nav_sign_out -> signOut()

            else -> toast("You Selected Something Else")
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//
//        when (item.itemId) {
//            R.id.action_recipe -> toast("You Selected Recipe")
//            R.id.action_report -> toast("You Selected Report")
//        }
//        return super.onOptionsItemSelected(item)
//    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START)
         else
            super.onBackPressed()
    }

    private fun navigateTo(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.homeFrame, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun signOut() {
        app.googleSignInClient.signOut().addOnCompleteListener(this) {
            app.auth.signOut()
            startActivity<Login>()
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1 -> {
                if (data != null) {
                    writeImageRef(app,readImageUri(resultCode, data).toString())
                    Picasso.get().load(readImageUri(resultCode, data).toString())
                        .resize(180, 180)
                        .transform(CropCircleTransformation())
                        .into(navView.getHeaderView(0).imageView, object : Callback {
                            override fun onSuccess() {
                                // Drawable is ready
                                uploadImageView(app,navView.getHeaderView(0).imageView)
                            }
                            override fun onError(e: Exception) {}
                        })
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (isPermissionGranted(requestCode, grantResults)) {
            // todo get the current location
            setCurrentLocation(app)
        } else {
            // permissions denied, so use a default location
            app.currentLocation = Location("Default").apply {
                latitude = 52.245696
                longitude = -7.139102
            }
        }
        Log.v("Recipes", "Home LAT: ${app.currentLocation.latitude} LNG: ${app.currentLocation.longitude}")
    }
}
