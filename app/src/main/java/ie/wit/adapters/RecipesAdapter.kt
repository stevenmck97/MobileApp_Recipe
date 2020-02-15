package ie.wit.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.wit.R
import ie.wit.helpers.readImageFromPath
import ie.wit.models.RecipesModel
import kotlinx.android.synthetic.main.card_recipes.view.*

interface RecipeListener {
    fun onRecipeClick(recipe: RecipesModel)
}

class RecipesAdapter constructor(private var recipes: List<RecipesModel>,
                                 private val listener: RecipeListener) : RecyclerView.Adapter<RecipesAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(
            LayoutInflater.from(parent?.context).inflate(
                R.layout.card_recipes,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val recipe = recipes[holder.adapterPosition]
        holder.bind(recipe, listener)
    }

    override fun getItemCount(): Int = recipes.size

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

            fun bind(recipe: RecipesModel, listener: RecipeListener) {
                itemView.recipeTitleList.text = recipe.title
                itemView.recipeDescriptionList.text = recipe.description
                itemView.imageViewList.setImageBitmap(
                    readImageFromPath(
                        itemView.context,
                        recipe.image
                    )
                )
                itemView.setOnClickListener { listener.onRecipeClick(recipe) }
            }
        }
    }
