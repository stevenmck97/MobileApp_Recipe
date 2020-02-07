package ie.wit.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import ie.wit.R
import ie.wit.main.RecipesApp
import ie.wit.models.RecipesModel
import kotlinx.android.synthetic.main.fragment_recipe.view.*
import org.jetbrains.anko.toast


class RecipeFragment : Fragment() {

    lateinit var app: RecipesApp
    var totalReciped = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as RecipesApp
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_recipe, container, false)
        activity?.title = getString(R.string.action_recipe)

        root.progressBar.max = 10000
        root.amountPicker.minValue = 1
        root.amountPicker.maxValue = 1000

        root.amountPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            //Display the newly selected number to recipeTransactionAmount
            root.recipeTransactionAmount.setText("$newVal")
        }
        setButtonListener(root)
        return root;
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            RecipeFragment().apply {
                arguments = Bundle().apply {}
            }
    }

    fun setButtonListener( layout: View) {
        layout.recipeButton.setOnClickListener {
            val amount = if (layout.recipeTransactionAmount.text.isNotEmpty())
                layout.recipeTransactionAmount.text.toString().toInt() else layout.amountPicker.value
            if(totalReciped >= layout.progressBar.max)
                activity?.toast("Recipe Amount Exceeded!")
            else {
                val recipeTransactionmethod = if(layout.recipeTransactionMethod.checkedRadioButtonId == R.id.Direct) "Direct" else "Paypal"
                totalReciped += amount
                layout.totalSoFar.text = "$$totalReciped"
                layout.progressBar.progress = totalReciped
                app.recipessStore.create(RecipesModel(recipeTransactionmethod = recipeTransactionmethod,amount = amount))
            }
        }
    }
}
