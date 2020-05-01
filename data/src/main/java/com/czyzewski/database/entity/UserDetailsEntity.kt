package com.czyzewski.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.czyzewski.models.UserDetailsModel

@Entity(tableName = "user_details")
class UserDetailsEntity(
    val login: String,
    @PrimaryKey
    val id: Long,
    val nodeId: String,
    val avatarUrl: String,
    val type: String,
    val name: String?,
    val publicRepos: Long,
    val publicGists: Long,
    val followers: Long,
    val following: Long,
    val createdAt: String,
    val updatedAt: String,
    val company: String?,
    val blog: String?,
    val location: String?,
    val email: String?,
    val bio: String?,
    val twitterUsername: String?
) {
    fun toModel() = UserDetailsModel(
        login = login,
        id = id,
        nodeId = nodeId,
        avatarUrl = avatarUrl,
        type = type,
        name = name,
        publicRepos = publicRepos,
        publicGists = publicGists,
        followers = followers,
        following = following,
        createdAt = createdAt,
        updatedAt = updatedAt,
        company = company,
        blog = blog,
        location = location,
        email = email,
        bio = bio,
        twitterUsername = twitterUsername
    )
}
