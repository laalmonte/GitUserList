package com.directpl.gituserlist.model

import androidx.annotation.Keep

@Keep
data class UserObject(
    var login: String?,
    var id: Int?,
    var nodeId: String?,
    var avatarUrl: String?,
    var gravatarId: String?,
    var url: String?,
    var htmlUrl: String?,
    var followersUrl: String?,
    var followingUrl: String?,
    var gistsUrl: String?,
    var starredUrl: String?,
    var subscriptionsUrl: String?,
    var organizationsUrl: String?,
    var reposUrl: String?,
    var eventsUrl: String?,
    var receivedEventsUrl: String?,
    var type: String?,
    var siteAdmin: Boolean?,
    var name: String?,
    var company: String?,
    var blog: String?,
    var location: String?,
    var email: String?,
    var hireable: String?,
    var bio: String?,
    var twitter_username: String?,
    var public_repos: Int?,
    var public_gists: Int?,
    var followers: Int?,
    var following: Int?,
    var created_at: String?,
    var updated_at: String?,
    var note: String?
)