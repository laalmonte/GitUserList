package com.directpl.gituserlist.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.directpl.gituserlist.R
import com.directpl.gituserlist.extension.start
import com.directpl.gituserlist.model.Status
import com.directpl.gituserlist.model.UserObject
import com.directpl.gituserlist.ui.adapters.ItemAdapter
import com.directpl.gituserlist.util.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.progressBar
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.item_list.*
import splitties.bundle.putExtras

@AndroidEntryPoint
class MainAcivity : AppCompatActivity(){

    private val mainViewModel : MainViewModel by viewModels()
    private val userListStorage = mutableListOf<UserObject>()
    private lateinit var itemAdapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        userListStorage.clear()
        setupAdapter()
        mainViewModel.getUsers()
        subscribeUi()
        attachActions()
    }

    private fun setupAdapter(){
        itemAdapter = ItemAdapter(onItemSelectCallback)
        itemList.apply {
            adapter = itemAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun attachActions(){
        etSearch.doOnTextChanged { text, start, before, count ->
            if (text.toString() == ""){
                itemAdapter.updateUserList(userListStorage)
            }
        }


        val userListStorageTemp = mutableListOf<UserObject>()

        btnSearch.setOnClickListener {
            if (etSearch.text.toString() == "") {
                itemAdapter.updateUserList(userListStorage)
            } else {
                itemAdapter.updateUserList(userListStorageTemp)
                mainViewModel.getUserDetail(etSearch.text.toString())
            }
        }
    }

    private fun subscribeUi() {
        mainViewModel.data.observe(this, Observer { response ->
            when(response.status) {
                Status.ERROR -> {
                    Handler(Looper.getMainLooper()).postDelayed({
                        progressBar.visibility = View.GONE
                        itemList.visibility    = View.VISIBLE
                    }, 2000)

                    Toast.makeText(this, "Please check network connection and try again...", Toast.LENGTH_SHORT).show()
                    subscribeDB()

                } Status.LOADING -> {}
                else -> {
                    Handler(Looper.getMainLooper()).postDelayed({
                        progressBar.visibility = View.GONE
                        itemList.visibility    = View.VISIBLE
                    }, 2000)
                    subscribeDB()
                }
            }
        })

        mainViewModel.data2.observe(this, Observer { response ->
            when(response.status) {
                Status.ERROR -> {
                    progressBar.visibility = View.GONE
                    when {
                        response.message.toString().contains("github") -> { Toast.makeText(this, "Please check network connection and try again...", Toast.LENGTH_SHORT).show() }
                        response.message.toString().contains("okhttp") -> { Toast.makeText(this, "Please input correct username and try again...", Toast.LENGTH_SHORT).show() }
                        else -> { Toast.makeText(this,response.message.toString(), Toast.LENGTH_SHORT).show() }
                    }

                } Status.LOADING -> {}
                else -> {
                    progressBar.visibility   = View.GONE
                    val userListStorageTemp2 = mutableListOf<UserObject>()
                    userListStorageTemp2.clear()
                    response.data?.let {
//                        setupDetails(it)
                        val userObj = UserObject(
                            it.login, it.id, it.node_id, it.avatar_url, it.gravatar_id,
                            it.url, it.html_url, it.followers_url, it.following_url, it.gists_url,
                            it.starred_url, it.subscriptions_url, it.organizations_url,
                            it.repos_url, it.events_url, it.received_events_url, it.type, it.site_admin,
                            it.name, it.company, it.blog, it.location, it.email,
                            it.hireable, it.bio, it.twitter_username, it.public_repos, it.public_gists,
                            it.followers, it.following, it.created_at, it.updated_at, ""
                        )

                        if (userListStorageTemp2.size > 0){
                            var isFound = false
                            userListStorage.forEach{
                                if (it.login == userObj.login) { isFound = true }
                            }

                            if (!isFound) userListStorageTemp2.add(userObj)
                        } else {
                            userListStorageTemp2.add(userObj)
                        }

                        itemAdapter.updateUserList(userListStorageTemp2)
                    }
                }
            }
        })
    }


    private fun subscribeDB(){
        mainViewModel.getUsersFromDB().observe(this, Observer {
            if (it.size > 0) {
                it.forEach { usr ->
                    val userObj = UserObject(
                            usr.login, usr.id, usr.nodeId, usr.avatarUrl, usr.gravatarId, usr.url,
                            usr.htmlUrl, usr.followersUrl, usr.followingUrl, usr.gistsUrl,
                            usr.starredUrl, usr.subscriptionsUrl, usr.organizationsUrl, usr.reposUrl,
                            usr.eventsUrl, usr.receivedEventsUrl, usr.type, usr.siteAdmin, usr.name, usr.company,
                            usr.blog, usr.location, usr.email, usr.hireable, usr.bio,
                            usr.twitterUsername, usr.publicRepos, usr.publicGists, usr.followers, usr.following,
                            usr.createdAt, usr.updatedAt, usr.note
                    )

                    if (userListStorage.size > 0){
                        var isFound = false
                        userListStorage.forEach{
                            if (it.login == userObj.login) { isFound = true }
                        }

                        if (!isFound) userListStorage.add(userObj)
                    } else {
                        userListStorage.add(userObj)
                    }
                }
            } else {
                userListStorage.clear()
            }

            itemAdapter.updateUserList(userListStorage)
            itemAdapter.updateContext(this)
        })
    }

    private val onItemSelectCallback = object : ItemAdapter.OnItemSelectCallback {
        override fun onSelectItem(userObject: UserObject) {
            getUrlFromIntent(userObject.htmlUrl.toString())
        }
    }

    private fun getUrlFromIntent( urlIntent:String ) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(urlIntent)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.settings_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_exit -> {
                Toast.makeText(applicationContext, "Thank you for using the App, Goodbye", Toast.LENGTH_LONG).show()
                Handler(Looper.getMainLooper()).postDelayed({
                    finish()
                }, 1250)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }


}