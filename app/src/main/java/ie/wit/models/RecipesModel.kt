package ie.wit.models

import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

@IgnoreExtraProperties
@Parcelize
data class RecipesModel(
    var uid: String? = "",
    var paymenttype: String = "N/A",
    var amount: Int = 0,
    var message: String = "a message",
    var upvotes: Int = 0,
    var profilepic: String = "",
    var isfavourite: Boolean = false,
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var email: String? = "joe@bloggs.com")
    : Parcelable
{
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "paymenttype" to paymenttype,
            "amount" to amount,
            "message" to message,
            "upvotes" to upvotes,
            "profilepic" to profilepic,
            "isfavourite" to isfavourite,
            "latitude" to latitude,
            "longitude" to longitude,
            "email" to email
        )
    }
}


