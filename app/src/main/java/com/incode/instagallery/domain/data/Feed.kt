package com.incode.instagallery.domain.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Feed(
    var id: String? = "",
    var title: String? = "",
    var comment: String? = "",
    var pictureUrl: String? = "",
    var publishedAt: String? = "",
) : Parcelable
