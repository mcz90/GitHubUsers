package com.czyzewski.repository

import junit.framework.Assert.assertEquals
import org.junit.Test

class UsersRepositoryTest {

    @Test
    fun test1() {
        val string = "<https://api.github.com/users?since=154>; rel=\"next\","

        val substring = string
            .substringBefore(">; rel=\"next\",")
            .substringAfter("<https://api.github.com/users?since=")

        assertEquals("154", substring)
    }

}
