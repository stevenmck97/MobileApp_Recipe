package ie.wit.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ie.wit.R
import ie.wit.fragments.ReportAllFragment
import ie.wit.models.RecipesModel
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.card_recipes.view.*
import kotlinx.android.synthetic.main.card_recipes.view.recipeDescription
import kotlinx.android.synthetic.main.card_recipes.view.recipeTitle
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions


interface RecipesListener {
    fun onRecipesClick(recipes: RecipesModel)
}

class RecipesAdapter(options: FirebaseRecyclerOptions<RecipesModel>,
                      private val listener: RecipesListener?)
    : FirebaseRecyclerAdapter<RecipesModel,
        RecipesAdapter.RecipesViewHolder>(options) {

    class RecipesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(recipes: RecipesModel, listener: RecipesListener) {
            with(recipes) {
                itemView.tag = recipes
                itemView.recipeTitle.text = recipes.title
                itemView.recipeDescription.text = recipes.description


//          Picasso.get().load(recipes.recipestoreimage.toUri()).into(itemView.recipeImageView)

                if(listener is ReportAllFragment)
                    ; // Do Nothing, Don't Allow 'Clickable' Rows
                else
                    itemView.setOnClickListener { listener.onRecipesClick(recipes) }

                if(recipes.isfavourite) itemView.imagefavourite.setImageResource(R.drawable.ic_favorite_on)

                if(!recipes.profilepic.isEmpty()) {
                    Picasso.get().load(recipes.profilepic.toUri())
                        //.resize(180, 180)
                        .transform(CropCircleTransformation())
                        .into(itemView.imageIcon)
                }
                else
                    itemView.imageIcon.setImageResource(R.mipmap.ic_launcher_food_round)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipesViewHolder {

        return RecipesViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.card_recipes, parent, false))
    }

    override fun onBindViewHolder(holder: RecipesViewHolder, position: Int, model: RecipesModel) {
        holder.bind(model,listener!!)
    }

    override fun onDataChanged() {
        // Called each time there is a new data snapshot. You may want to use this method
        // to hide a loading spinner or check for the "no documents" state and update your UI.
        // ...
    }
}