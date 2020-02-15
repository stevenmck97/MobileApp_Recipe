package ie.wit.main

import android.app.Application
import android.util.Log
import ie.wit.models.RecipeJSONStore
import ie.wit.models.RecipesStore

class RecipesApp : Application() {

    lateinit var recipes: RecipesStore

    override fun onCreate() {
        super.onCreate()
        recipes = RecipeJSONStore(applicationContext)
        Log.v("Recipe","Recipes App started")
    }
}