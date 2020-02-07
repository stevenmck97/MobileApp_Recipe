package ie.wit.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.wit.R
import ie.wit.models.RecipesModel
import kotlinx.android.synthetic.main.card_recipes.view.*

class RecipesAdapter constructor(private var recipess: List<RecipesModel>)
    : RecyclerView.Adapter<RecipesAdapter.MainHolder>() {

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
        val recipes = recipess[holder.adapterPosition]
        holder.bind(recipes)
    }

    override fun getItemCount(): Int = recipess.size

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(recipes: RecipesModel) {
            itemView.recipeTransactionamount.text = recipes.amount.toString()
            itemView.recipeTransactionmethod.text = recipes.recipeTransactionmethod
            itemView.imageIcon.setImageResource(R.mipmap.ic_launcher_round)
        }
    }
}