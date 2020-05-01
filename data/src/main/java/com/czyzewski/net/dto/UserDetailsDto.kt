package com.czyzewski.net.dto

import com.czyzewski.models.UserDetailsModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class UserDetailsDto(
    val login: String,
    val id: Long,
    @SerialName("node_id")
    val nodeId: String,
    @SerialName("avatar_url")
    val avatarUrl: String,
    val type: String,
    val name: String?,
    @SerialName("public_repos")
    val publicRepos: Long,
    @SerialName("public_gists")
    val publicGists: Long,
    val followers: Long,
    val following: Long,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String,
    val company: String?,
    val blog: String?,
    val location: String?,
    val email: String?,
    val bio: String?,
    @SerialName("twitter_username")
    val twitterUsername: String?
) {

    fun toModel() =
        UserDetailsModel(
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
