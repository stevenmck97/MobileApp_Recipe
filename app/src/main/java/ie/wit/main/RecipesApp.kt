package ie.wit.main

import android.app.Application
import android.util.Log
import ie.wit.models.RecipesMemStore
import ie.wit.models.RecipesStore

class RecipesApp : Application() {

    lateinit var recipessStore: RecipesStore

    override fun onCreate() {
        super.onCreate()
        recipessStore = RecipesMemStore()
        Log.v("Recipe","Recipes App started")
    }
}