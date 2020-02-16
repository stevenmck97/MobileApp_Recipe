package ie.wit.models

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.AnkoLogger
import ie.wit.helpers.*
import java.util.*

val JSON_FILE = "recipes.json"
val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
val listType = object : TypeToken<java.util.ArrayList<RecipesModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class RecipeJSONStore : RecipesStore, AnkoLogger {

    val context: Context
    var recipes = mutableListOf<RecipesModel>()


    constructor (context: Context) {
        this.context = context
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<RecipesModel> {
        return recipes
    }

    override fun create(recipe: RecipesModel) {
        recipe.id = generateRandomId()
        recipes.add(recipe)
        serialize()
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(recipes, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        recipes = Gson().fromJson(jsonString, listType)
    }

    override fun update(recipe: RecipesModel) {
        val recipes = findAll() as ArrayList<RecipesModel>
        var foundRecipe: RecipesModel? = recipes.find { p -> p.id == recipe.id }
        if (foundRecipe != null) {
            foundRecipe.title = recipe.title
            foundRecipe.description = recipe.description
            foundRecipe.image = recipe.image

        }
        serialize()
    }

    override fun delete(recipe: RecipesModel) {
        recipes.remove(recipe)
        serialize()
    }
}