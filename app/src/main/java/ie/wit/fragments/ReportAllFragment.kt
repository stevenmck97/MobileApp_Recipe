package ie.wit.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

import ie.wit.R
import ie.wit.adapters.RecipesAdapter
import ie.wit.adapters.RecipesListener
import ie.wit.models.RecipesModel
import ie.wit.utils.*
import kotlinx.android.synthetic.main.fragment_report.view.*
import org.jetbrains.anko.info

class ReportAllFragment : ReportFragment(),
    RecipesListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_report, container, false)
        activity?.title = getString(R.string.menu_report_all)

        root.recyclerView.setLayoutManager(LinearLayoutManager(activity))
        setSwipeRefresh()

        return root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ReportAllFragment().apply {
                arguments = Bundle().apply { }
            }
    }

    override fun setSwipeRefresh() {
        root.swiperefresh.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                root.swiperefresh.isRefreshing = true
                getAllUsersRecipes()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        getAllUsersRecipes()
    }

    fun getAllUsersRecipes() {
        loader = createLoader(activity!!)
        showLoader(loader, "Downloading All Users Recipes from Firebase")
        val recipesList = ArrayList<RecipesModel>()
        app.database.child("recipes")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    info("Firebase Recipes error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    hideLoader(loader)
                    val children = snapshot.children
                    children.forEach {
                        val recipes = it.
                            getValue<RecipesModel>(RecipesModel::class.java)

                        recipesList.add(recipes!!)
                        root.recyclerView.adapter =
                            RecipesAdapter(recipesList, this@ReportAllFragment,true)
                        root.recyclerView.adapter?.notifyDataSetChanged()
                        checkSwipeRefresh()

                        app.database.child("recipes").removeEventListener(this)
                    }
                }
            })
    }
}
