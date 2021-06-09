package com.incode.instagallery.domain.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Feed(
    var id: String? = "",
    var title: String? = "",
    var comment: String? = "",
    var pictureUri: String = "",
    var publishedAt: String? = "",
    val isFromNetwork: Boolean = false,
) : Parcelable
