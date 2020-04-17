package ie.wit.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ie.wit.R
import ie.wit.main.RecipesApp
import ie.wit.utils.getAllRecipess
import ie.wit.utils.getFavouriteRecipess
import ie.wit.utils.setMapMarker
import kotlinx.android.synthetic.main.fragment_favourites.*


class FavouritesFragment : Fragment() {

    lateinit var app: RecipesApp
    var viewFavourites = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as RecipesApp
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val layout = inflater.inflate(R.layout.fragment_favourites, container, false)

        return layout;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = getString(R.string.favourites_title)

        imageMapFavourites.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                app.mMap.clear()
                setMapMarker(app)
                if (!viewFavourites) {
                    imageMapFavourites.setImageResource(R.drawable.ic_favorite_on)
                    viewFavourites = true
                    getFavouriteRecipess(app)
                }
                else {
                    imageMapFavourites.setImageResource(R.drawable.ic_favorite_off)
                    viewFavourites = false
                    getAllRecipess(app)
                }
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            FavouritesFragment().apply {
                arguments = Bundle().apply { }
            }
    }
}