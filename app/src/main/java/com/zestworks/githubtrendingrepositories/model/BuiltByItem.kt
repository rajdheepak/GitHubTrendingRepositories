package com.zestworks.githubtrendingrepositories.model

import com.google.gson.annotations.SerializedName

data class BuiltByItem(

	@field:SerializedName("href")
	val href: String? = null,

	@field:SerializedName("avatar")
	val avatar: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)