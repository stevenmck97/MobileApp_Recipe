package ie.wit.fragments



import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ie.wit.R
import ie.wit.helpers.readImage
import ie.wit.helpers.readImageFromPath
import ie.wit.helpers.showImagePicker
import ie.wit.main.RecipesApp
import ie.wit.models.RecipesModel
import kotlinx.android.synthetic.main.card_recipes.*
import kotlinx.android.synthetic.main.fragment_recipe.*
import kotlinx.android.synthetic.main.fragment_recipe.view.*
import android.view.Menu
import android.view.MenuItem
//import kotlinx.android.synthetic.main.fragment_recipe.*
//import kotlinx.android.synthetic.main.activity_recipe_list.*
//import kotlinx.android.synthetic.main.card_recipe.*
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import org.jetbrains.anko.*
import ie.wit.helpers.readImage
import ie.wit.helpers.readImageFromPath
import ie.wit.helpers.showImagePicker


class RecipeFragment : AppCompatActivity(), AnkoLogger {

    var recipe = RecipesModel()
    lateinit var app: RecipesApp
    var edit = false
    val IMAGE_REQUEST = 1
//    val intent = Intent()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_recipe)
        app = application as RecipesApp


//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//
//        val root = inflater.inflate(R.layout.fragment_recipe, container, false)
//        activity?.title = getString(R.string.action_recipe)


        if (intent.hasExtra("recipe_edit"))
        {
            edit = true
            recipe = intent.extras?.getParcelable<RecipesModel>("recipe_edit")!!
            recipeTitle.setText(recipe.recipeAddMethod)
            recipeDescription.setText(recipe.description)
            btnAdd.setText(R.string.save_recipe)
            recipeImage.setImageBitmap(readImageFromPath(this, recipe.image))
            if (recipe.image != null) {
                chooseImage.setText(R.string.change_recipe_image)
            }
        }

        btnAdd.setOnClickListener() {
            recipe.recipeAddMethod = recipeTitle.text.toString()
            recipe.description = recipeDescription.text.toString()
            if (recipe.recipeAddMethod.isEmpty()) {
                toast(R.string.enter_recipe_title)
            } else {
                if (edit) {
                    app.recipes.update(recipe.copy())
                } else {
                    app.recipes.create(recipe.copy())
                }
            }
            info("add Button Pressed: $recipeTitle")

        }

        //Add action bar and set title
//        toolbarAdd.title = title
//        setSupportActionBar(toolbarAdd)



        chooseImage.setOnClickListener {
            showImagePicker(this, IMAGE_REQUEST)
        }


//        setButtonListener(root)
//        return root;
    }

//    companion object {
//        @JvmStatic
//        fun newInstance() =
//            RecipeFragment().apply {
//                arguments = Bundle().apply {}
//            }
//    }

//    fun setButtonListener( layout: View) {
//        layout.btnAdd.setOnClickListener {
//
//                app.recipes.create(RecipesModel(recipeAddMethod = recipeTransactionmethod,amount = amount))
//            }
//        }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_recipe, menu)
        if (edit && menu != null) menu.getItem(0).setVisible(true)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_delete -> {
                app.recipes.delete(recipe)
                finish()
            }
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            IMAGE_REQUEST -> {
                if (data != null) {
                    recipe.image = data.getData().toString()
                    recipeImage.setImageBitmap(readImage(this, resultCode, data))
                    chooseImage.setText(R.string.change_recipe_image)
                }
            }

        }
    }
}

