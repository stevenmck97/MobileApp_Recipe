package ie.wit.fragments


import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager

import ie.wit.R
import ie.wit.adapters.RecipeListener
import ie.wit.adapters.RecipesAdapter
import ie.wit.main.RecipesApp
import ie.wit.models.RecipesModel
import kotlinx.android.synthetic.main.fragment_report.*
import kotlinx.android.synthetic.main.fragment_report.view.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivityForResult

class ReportFragment : AppCompatActivity(), RecipeListener {

    lateinit var app: RecipesApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_report)
        app = application as RecipesApp

        //layout and populate for display
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager   //recyclerView is a widget in activity_movie_list.xml
        loadRecipes()

        //enable action bar and set title
//        toolbarMain.title = title
//        setSupportActionBar(toolbarMain)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_add -> startActivityForResult<RecipeFragment>(0)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRecipeClick(recipe: RecipesModel) {
        startActivityForResult(intentFor<RecipeFragment>().putExtra("recipe_edit", recipe), 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        loadRecipes()
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun loadRecipes() {
        showMovies(app.recipes.findAll())
    }

    fun showMovies (recipes: List<RecipesModel>) {
        recyclerView.adapter = RecipesAdapter(recipes, this)
        recyclerView.adapter?.notifyDataSetChanged()
    }






//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        app = activity?.application as RecipesApp
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        var root = inflater.inflate(R.layout.fragment_report, container, false)
//
//        root.recyclerView.setLayoutManager(LinearLayoutManager(activity))
//        root.recyclerView.adapter = RecipesAdapter(app.recipes.findAll())
//
//        return root
//    }
//
//    companion object {
//        @JvmStatic
//        fun newInstance() =
//            ReportFragment().apply {
//                arguments = Bundle().apply { }
//            }
//    }
}
