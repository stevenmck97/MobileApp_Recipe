package ie.wit.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ie.wit.R
import ie.wit.models.RecipesModel
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.card_recipes.view.*

interface RecipesListener {
    fun onRecipesClick(recipes: RecipesModel)
}

class RecipesAdapter constructor(var recipes: ArrayList<RecipesModel>,
                                  private val listener: RecipesListener, reportall : Boolean)
    : RecyclerView.Adapter<RecipesAdapter.MainHolder>() {

    val reportAll = reportall

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.card_recipes,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val recipes = recipes[holder.adapterPosition]
        holder.bind(recipes,listener,reportAll)
    }

    override fun getItemCount(): Int = recipes.size

    fun removeAt(position: Int) {
        recipes.removeAt(position)
        notifyItemRemoved(position)
    }

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(recipes: RecipesModel, listener: RecipesListener, reportAll: Boolean) {
            itemView.tag = recipes
//            itemView.paymentamount.text = recipes.amount.toString()
//            itemView.paymentmethod.text = recipes.paymenttype
            itemView.recipeTitleList.text = recipes.title
            itemView.recipeDescriptionList.text = recipes.description
            if(recipes.isfavourite) itemView.imagefavourite.setImageResource(android.R.drawable.star_big_on)

            if(!reportAll)
                itemView.setOnClickListener { listener.onRecipesClick(recipes) }

            if(!recipes.profilepic.isEmpty()) {
                Picasso.get().load(recipes.profilepic.toUri())
                    //.resize(180, 180)
                    .transform(CropCircleTransformation())
                    .into(itemView.imageIcon)
            }
            else
                itemView.imageIcon.setImageResource(R.mipmap.ic_launcher_homer_round)
        }
    }
}