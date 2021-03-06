package ie.wit.fragments


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import ie.wit.R
import ie.wit.main.RecipesApp
import ie.wit.models.RecipesModel
import ie.wit.utils.*
import kotlinx.android.synthetic.main.fragment_recipe.recipeDescription
import kotlinx.android.synthetic.main.fragment_recipe.recipeImageView
import kotlinx.android.synthetic.main.fragment_recipe.recipeTitle
import kotlinx.android.synthetic.main.fragment_recipe.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import java.util.HashMap


class RecipeFragment : Fragment(), AnkoLogger {

    lateinit var app: RecipesApp
    var recipe = RecipesModel()
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
            val title = recipeTitle.text.toString()

            if (title.isEmpty()) {
                activity?.toast("Please enter a recipe title")
            } else {

                writeNewRecipes(
                    RecipesModel(
                        title = recipeTitle.text.toString(),
                        description = recipeDescription.text.toString(),
                        profilepic = app.userImage.toString(),
                        recipestoreimage = app.recipeImage.toString(),
                        isfavourite = favourite,
                        latitude = app.currentLocation.latitude,
                        longitude = app.currentLocation.longitude,
                        email = app.currentUser.email
                    )
                )
                activity?.toast("Recipe Added")
            }
        }
        }




    fun setFavouriteListener (layout: View) {
        layout.imagefavourite.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                if (!favourite) {
                    layout.imagefavourite.setImageResource(R.drawable.ic_favorite_on)
                    favourite = true
                }
                else {
                    layout.imagefavourite.setImageResource(R.drawable.ic_favorite_off)
                    favourite = false
                }
            }
        })
    }

    fun setImageListener (layout: View) {
        layout.recipeImageView.setOnClickListener { showImagePicker3(this,1) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1 -> {
                if (data != null) {
                    writeImageRef2(app,readImageUri2(resultCode, data).toString())
                    Picasso.get().load(readImageUri2(resultCode, data).toString())
                        .into(recipeImageView, object : Callback {
                            override fun onSuccess() {
                                // Drawable is ready
                                uploadImageView2(app,recipeImageView)
                            }
                            override fun onError(e: Exception) {}
                        })
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getAllRecipes(app.currentUser.uid)
    }

    override fun onPause() {
        super.onPause()
        if(app.currentUser.uid != null)
            app.database.child("user-recipes")
                    .child(app.currentUser.uid)
                    .removeEventListener(eventListener)
    }

    fun writeNewRecipes(recipes: RecipesModel) {
        // Create new recipes at /recipes & /recipes/$uid
        showLoader(loader, "Adding Recipes to Firebase")
        info("Firebase DB Reference : $app.database")
        val uid = app.currentUser.uid
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

    fun getAllRecipes(userId: String?) {
        eventListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                info("Firebase Recipes error : ${error.message}")
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                val children = snapshot.children
                children.forEach {
                    val recipes = it.getValue<RecipesModel>(RecipesModel::class.java)

                }

            }
        }

        app.database.child("user-recipes").child(userId!!)
            .addValueEventListener(eventListener)
    }
}
