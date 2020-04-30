package com.czyzewski.mappers

import com.czyzewski.database.entity.UserEntity
import com.czyzewski.models.Repositories
import com.czyzewski.models.RepositoriesModel
import com.czyzewski.models.UserModel
import com.czyzewski.net.dto.UserDto

interface IUserMapper {
    fun entityToModel(data: UserEntity): UserModel
    fun dtoToModel(data: UserDto): UserModel
    fun dtoToEntity(data: UserDto): UserEntity
    fun entitiesToModels(data: List<UserEntity>): List<UserModel>
    fun dtosToEntities(data: List<UserDto>): List<UserEntity>
}

class UserMapper : IUserMapper {

    override fun dtoToModel(data: UserDto) =
        UserModel(
            id = data.id,
            login = data.login,
            nodeId = data.nodeId,
            avatarUrl = data.avatarUrl,
            repositories = Repositories.Loading
        )

    override fun dtoToEntity(data: UserDto) =
        UserEntity(
            id = data.id,
            login = data.login,
            nodeId = data.nodeId,
            avatarUrl = data.avatarUrl,
            shouldSync = true,
            repositories = emptyList()
        )

    override fun entityToModel(data: UserEntity) =
        UserModel(
            id = data.id,
            login = data.login,
            nodeId = data.nodeId,
            avatarUrl = data.avatarUrl,
            repositories = Repositories.Loaded(
                RepositoriesModel(
                    data.id,
                    data.repositories?.map { it.name }?: emptyList())
            )
        )

    override fun entitiesToModels(data: List<UserEntity>) = data.map(::entityToModel)

    override fun dtosToEntities(data: List<UserDto>) = data.map(::dtoToEntity)
}
