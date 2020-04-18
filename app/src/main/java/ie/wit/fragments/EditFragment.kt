package ie.wit.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

import ie.wit.R
import ie.wit.main.RecipesApp
import ie.wit.models.RecipesModel
import ie.wit.utils.createLoader
import ie.wit.utils.hideLoader
import ie.wit.utils.showLoader
import kotlinx.android.synthetic.main.fragment_edit.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class EditFragment : Fragment(), AnkoLogger {

    lateinit var app: RecipesApp
    lateinit var loader : AlertDialog
    lateinit var root: View
    var editRecipes: RecipesModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as RecipesApp

        arguments?.let {
            editRecipes = it.getParcelable("editrecipes")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_edit, container, false)
        activity?.title = getString(R.string.action_edit)
        loader = createLoader(activity!!)


        root.recipeTitle.setText(editRecipes!!.title)
        root.recipeDescription.setText(editRecipes!!.description)


        root.editUpdateButton.setOnClickListener {
            showLoader(loader, "Updating Recipes on Server...")
            updateRecipesData()
            updateRecipes(editRecipes!!.uid, editRecipes!!)
            updateUserRecipes(app.auth.currentUser!!.uid,
                               editRecipes!!.uid, editRecipes!!)
        }

        return root
    }

    companion object {
        @JvmStatic
        fun newInstance(recipes: RecipesModel) =
            EditFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("editrecipes",recipes)
                }
            }
    }

    fun updateRecipesData() {

        editRecipes!!.title = root.recipeTitle.text.toString()
        editRecipes!!.description = root.recipeDescription.text.toString()

    }

    fun updateUserRecipes(userId: String, uid: String?, recipes: RecipesModel) {
        app.database.child("user-recipes").child(userId).child(uid!!)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.ref.setValue(recipes)
                        activity!!.supportFragmentManager.beginTransaction()
                        .replace(R.id.homeFrame, ReportFragment.newInstance())
                        .addToBackStack(null)
                        .commit()
                        hideLoader(loader)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        info("Firebase Recipes error : ${error.message}")
                    }
                })
    }

    fun updateRecipes(uid: String?, recipes: RecipesModel) {
        app.database.child("recipes").child(uid!!)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.ref.setValue(recipes)
                        hideLoader(loader)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        info("Firebase Recipes error : ${error.message}")
                    }
                })
    }
}
