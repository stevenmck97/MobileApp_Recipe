package ie.wit.activities


import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager

import ie.wit.R
import ie.wit.adapters.RecipeListener
import ie.wit.adapters.RecipesAdapter
import ie.wit.main.RecipesApp
import ie.wit.models.RecipesModel
import kotlinx.android.synthetic.main.activity_recipe_list.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivityForResult

class RecipeListActivity : AppCompatActivity(), RecipeListener {

    lateinit var app: RecipesApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_list)
        app = application as RecipesApp

        //layout and populate for display
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager   //recyclerView is a widget in activity_recipe_list.xml
        loadRecipes()

        //enable action bar and set title
        toolbarMain.title = title
        setSupportActionBar(toolbarMain)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_add -> startActivityForResult<RecipeActivity>(0)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRecipeClick(recipe: RecipesModel) {
        startActivityForResult(intentFor<RecipeActivity>().putExtra("recipe_edit", recipe), 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        loadRecipes()
        super.onActivityResult(requestCode, resultCode, data)
    }

//loads recipes
    private fun loadRecipes() {
        showRecipes(app.recipes.findAll())
    }

//displays recipes from recyclerview widget
    fun showRecipes (recipes: List<RecipesModel>) {
        recyclerView.adapter = RecipesAdapter(recipes, this)
        recyclerView.adapter?.notifyDataSetChanged()
    }

}
