package ie.wit.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RecipesModel(var id: Long = 0,
                         val recipeTransactionmethod: String = "N/A",
                         val amount: Int = 0) : Parcelable

