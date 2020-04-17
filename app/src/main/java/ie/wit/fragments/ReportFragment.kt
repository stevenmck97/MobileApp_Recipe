package ie.wit.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

import ie.wit.R
import ie.wit.adapters.RecipesAdapter
import ie.wit.adapters.RecipesListener
import ie.wit.main.RecipesApp
import ie.wit.models.RecipesModel
import ie.wit.utils.*
import kotlinx.android.synthetic.main.fragment_report.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

open class ReportFragment : Fragment(), AnkoLogger,
    RecipesListener {

    lateinit var app: RecipesApp
    lateinit var loader : AlertDialog
    lateinit var root: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as RecipesApp
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_report, container, false)
        activity?.title = getString(R.string.action_report)

        root.recyclerView.setLayoutManager(LinearLayoutManager(activity))
        setSwipeRefresh()

        val swipeDeleteHandler = object : SwipeToDeleteCallback(activity!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = root.recyclerView.adapter as RecipesAdapter
                adapter.removeAt(viewHolder.adapterPosition)
                deleteRecipes((viewHolder.itemView.tag as RecipesModel).uid)
                deleteUserRecipes(app.auth.currentUser!!.uid,
                                  (viewHolder.itemView.tag as RecipesModel).uid)
            }
        }
        val itemTouchDeleteHelper = ItemTouchHelper(swipeDeleteHandler)
        itemTouchDeleteHelper.attachToRecyclerView(root.recyclerView)

        val swipeEditHandler = object : SwipeToEditCallback(activity!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                onRecipesClick(viewHolder.itemView.tag as RecipesModel)
            }
        }
        val itemTouchEditHelper = ItemTouchHelper(swipeEditHandler)
        itemTouchEditHelper.attachToRecyclerView(root.recyclerView)

        return root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ReportFragment().apply {
                arguments = Bundle().apply { }
            }
    }

    open fun setSwipeRefresh() {
        root.swiperefresh.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                root.swiperefresh.isRefreshing = true
                getAllRecipess(app.auth.currentUser!!.uid)
            }
        })
    }

    fun checkSwipeRefresh() {
        if (root.swiperefresh.isRefreshing) root.swiperefresh.isRefreshing = false
    }

    fun deleteUserRecipes(userId: String, uid: String?) {
        app.database.child("user-recipes").child(userId).child(uid!!)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.ref.removeValue()
                    }
                    override fun onCancelled(error: DatabaseError) {
                        info("Firebase Recipes error : ${error.message}")
                    }
                })
    }

    fun deleteRecipes(uid: String?) {
        app.database.child("recipes").child(uid!!)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.ref.removeValue()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        info("Firebase Recipes error : ${error.message}")
                    }
                })
    }

    override fun onRecipesClick(recipes: RecipesModel) {
        activity!!.supportFragmentManager.beginTransaction()
            .replace(R.id.homeFrame, EditFragment.newInstance(recipes))
            .addToBackStack(null)
            .commit()
    }

    override fun onResume() {
        super.onResume()
        if(this::class == ReportFragment::class)
            getAllRecipess(app.auth.currentUser!!.uid)
    }

    fun getAllRecipess(userId: String?) {
        loader = createLoader(activity!!)
        showLoader(loader, "Downloading Recipess from Firebase")
        val recipessList = ArrayList<RecipesModel>()
        app.database.child("user-recipes").child(userId!!)
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

                        recipessList.add(recipes!!)
                        root.recyclerView.adapter =
                            RecipesAdapter(recipessList, this@ReportFragment,false)
                        root.recyclerView.adapter?.notifyDataSetChanged()
                        checkSwipeRefresh()

                        app.database.child("user-recipes").child(userId)
                            .removeEventListener(this)
                    }
                }
            })
    }
}
