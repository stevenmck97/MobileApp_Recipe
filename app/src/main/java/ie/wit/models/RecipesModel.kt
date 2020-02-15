package ie.wit.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RecipesModel(var id: Long = 0,
                        var title: String = "",
                        var recipeAddMethod: String = "N/A",
                        var description: String = "",
                        var image: String = "") : Parcelable {

}

