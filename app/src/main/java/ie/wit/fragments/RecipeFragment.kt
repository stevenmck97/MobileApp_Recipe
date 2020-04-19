package ie.wit.fragments


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

import ie.wit.R
import ie.wit.main.RecipesApp
import ie.wit.models.RecipesModel
import ie.wit.utils.*
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.fragment_about_us.*
import kotlinx.android.synthetic.main.fragment_recipe.*
import kotlinx.android.synthetic.main.fragment_recipe.view.*
import kotlinx.android.synthetic.main.fragment_recipe.view.imageView
import kotlinx.android.synthetic.main.home.*
import kotlinx.android.synthetic.main.nav_header_home.view.*
//import kotlinx.android.synthetic.main.nav_header_home.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
//import org.jetbrains.anko.toast
//import java.lang.String.format
import java.util.HashMap


class RecipeFragment : Fragment(), AnkoLogger {

    lateinit var app: RecipesApp
    var recipe = RecipesModel()
    var ImageView = imageView
    lateinit var loader : AlertDialog
    lateinit var eventListener : ValueEventListener
    var favourite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as RecipesApp

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View? {

        val root = inflater.inflate(R.layout.fragment_recipe, container, false)
        loader = createLoader(activity!!)
        activity?.title = getString(R.string.action_recipe)
//
//        val image_view1 = ImageView
//        val image_view = image_view1.findViewById<RecyclerView>(R.id.imageView)



        setButtonListener(root)
        setFavouriteListener(root)
        setImageListener(root)
        return root;


    }

    companion object {
        @JvmStatic
        fun newInstance() =
            RecipeFragment().apply {
                arguments = Bundle().apply {}
            }
    }



    fun setButtonListener( layout: View) {
        layout.btnAdd.setOnClickListener {
//            val amount = if (
//                layout.recipeTitle.text.isNotEmpty()
//                layout.recipeTitle.text.toString().toInt()
            writeNewRecipes(RecipesModel(
            title = recipeTitle.text.toString(),
            description = recipeDescription.text.toString(),
            profilepic = app.userImage.toString(),
            recipeImage = app.recipeImage.toString(),
            isfavourite = favourite,
            latitude = app.currentLocation.latitude,
            longitude = app.currentLocation.longitude,
            email = app.auth.currentUser?.email))


//                val paymentmethod = if(layout.paymentMethod.checkedRadioButtonId == R.id.Direct) "Direct" else "Paypal"
//                writeNewRecipes(RecipesModel(paymenttype = paymentmethod, amount = amount,

            }
        }


    fun setFavouriteListener (layout: View) {
        layout.imagefavourite.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                if (!favourite) {
                    layout.imagefavourite.setImageResource(android.R.drawable.star_big_on)
                    favourite = true
                }
                else {
                    layout.imagefavourite.setImageResource(android.R.drawable.star_big_off)
                    favourite = false
                }
            }
        })
    }

    fun setImageListener (layout: View) {
        layout.imageView.setOnClickListener { showImagePicker2(this,1) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1 -> {
                if (data != null) {
                    writeImageRef(app,readImageUri(resultCode, data).toString())
                    Picasso.get().load(readImageUri(resultCode, data).toString())
                        .into(imageView, object : Callback {
                            override fun onSuccess() {
                                // Drawable is ready
                                uploadImageView(app,imageView)
                            }
                            override fun onError(e: Exception) {}
                        })
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getTotalReciped(app.auth.currentUser?.uid)
    }

    override fun onPause() {
        super.onPause()
        if(app.auth.uid != null)
            app.database.child("user-recipes")
                    .child(app.auth.currentUser!!.uid)
                    .removeEventListener(eventListener)
    }

    fun writeNewRecipes(recipes: RecipesModel) {
        // Create new recipes at /recipes & /recipes/$uid
        showLoader(loader, "Adding Recipes to Firebase")
        info("Firebase DB Reference : $app.database")
        val uid = app.auth.currentUser!!.uid
        val key = app.database.child("recipes").push().key
        if (key == null) {
            info("Firebase Error : Key Empty")
            return
        }
        recipes.uid = key
        val recipesValues = recipes.toMap()

        val childUpdates = HashMap<String, Any>()
        childUpdates["/recipes/$key"] = recipesValues
        childUpdates["/user-recipes/$uid/$key"] = recipesValues

        app.database.updateChildren(childUpdates)
        hideLoader(loader)


    }

    fun getTotalReciped(userId: String?) {
        eventListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                info("Firebase Recipes error : ${error.message}")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
//                totalReciped = 0
                val children = snapshot.children
                children.forEach {
                    val recipes = it.getValue<RecipesModel>(RecipesModel::class.java)
//                    totalReciped += recipes!!.amount
                }

            }
        }

        app.database.child("user-recipes").child(userId!!)
            .addValueEventListener(eventListener)
    }
}
