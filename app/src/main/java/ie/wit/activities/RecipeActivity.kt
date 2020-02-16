package ie.wit.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ie.wit.R
import ie.wit.helpers.readImage
import ie.wit.helpers.readImageFromPath
import ie.wit.helpers.showImagePicker
import ie.wit.main.RecipesApp
import ie.wit.models.RecipesModel
import kotlinx.android.synthetic.main.activity_recipe.*
import android.view.Menu
import android.view.MenuItem
import org.jetbrains.anko.*


class RecipeActivity : AppCompatActivity(), AnkoLogger {

    var recipe = RecipesModel()
    lateinit var app: RecipesApp
    var edit = false
    val IMAGE_REQUEST = 1



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)
        app = application as RecipesApp


        if (intent.hasExtra("recipe_edit")) {
            edit = true
            recipe = intent.extras?.getParcelable<RecipesModel>("recipe_edit")!!
            recipeTitle.setText(recipe.title)
            recipeDescription.setText(recipe.description)
            btnAdd.setText(R.string.save_recipe)
            recipeImage.setImageBitmap(readImageFromPath(this, recipe.image))
            if (recipe.image != null) {
                chooseImage.setText(R.string.change_recipe_image)
            }
        }

        //validates whether a title has been entered or not. Will not allow you to add if no title is present
        btnAdd.setOnClickListener() {
            recipe.title = recipeTitle.text.toString()
            recipe.description = recipeDescription.text.toString()
            if (recipe.title.isEmpty()) {
                toast(R.string.enter_recipe_title)
            } else {
                if (edit) {
                    app.recipes.update(recipe.copy())
                } else {
                    app.recipes.create(recipe.copy())
                }
            }
            info("add Button Pressed: $recipeTitle")
            setResult(AppCompatActivity.RESULT_OK)
            finish()

        }

        //Add action bar and set title
        toolbarAdd.title = title
        setSupportActionBar(toolbarAdd)



        chooseImage.setOnClickListener {
            showImagePicker(this, IMAGE_REQUEST)
        }

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_recipe, menu)
        if (edit && menu != null) menu.getItem(0).setVisible(true)
        return super.onCreateOptionsMenu(menu)
    }

//deletes recipe card when pressed or allows you to cancel
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

//allows you to choose an image from your device and replace it with the placeholder image in the add activity
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

