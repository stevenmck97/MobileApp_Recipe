package ie.wit.models

import android.util.Log
import org.jetbrains.anko.AnkoLogger

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

abstract class RecipesMemStore : RecipesStore, AnkoLogger {

        val recipeList = ArrayList<RecipesModel>()

        override fun findAll(): List<RecipesModel> {
            return recipeList
        }

        override fun findById(id:Long) : RecipesModel? {
            val foundRecipes: RecipesModel? = recipeList.find { it.id == id }
            return foundRecipes
        }


    override fun create(recipe: RecipesModel) {
        recipe.id = getId()
        recipeList.add(recipe)
        logAll()
    }
//        override fun create(recipe: RecipesModel) {
//            recipes.r = getId()
//            recipes.add(recipe)
//            logAll()
//        }

      override fun update(movie: RecipesModel) {
        var foundRecipe: RecipesModel? = recipeList.find { p -> p.id == movie.id }
        if (foundRecipe != null) {
            foundRecipe.title = movie.title
            foundRecipe.description = movie.description
            foundRecipe.image = movie.image
            logAll();
        }
    }

        fun logAll() {
            Log.v("Recipe","** Recipess List **")
            recipeList.forEach { Log.v("Recipe","${it}") }
        }
    }
