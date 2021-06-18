package com.incode.instagallery.domain.data

data class Feed(
    var id: String = "",
    var title: String? = "",
    var comment: String? = "",
    var pictureUri: String = "",
    var publishedAt: String? = "",
    val isFromNetwork: Boolean = false,
)
