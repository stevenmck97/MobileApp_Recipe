package ie.wit.models

import android.util.Log

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class RecipesMemStore : RecipesStore {

        val recipess = ArrayList<RecipesModel>()

        override fun findAll(): List<RecipesModel> {
            return recipess
        }

        override fun findById(id:Long) : RecipesModel? {
            val foundRecipes: RecipesModel? = recipess.find { it.id == id }
            return foundRecipes
        }

        override fun create(recipes: RecipesModel) {
            recipes.id = getId()
            recipess.add(recipes)
            logAll()
        }

        fun logAll() {
            Log.v("Recipe","** Recipess List **")
            recipess.forEach { Log.v("Recipe","${it}") }
        }
    }
