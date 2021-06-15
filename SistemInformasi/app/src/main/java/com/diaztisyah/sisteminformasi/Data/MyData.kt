package com.diaztisyah.sisteminformasi.Data

import android.os.Parcel
import android.os.Parcelable

data class MyData(
    val name: String?,
    val photo: String?,
    val des: String?

): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(photo)
        parcel.writeString(des)

    }
    override fun describeContents(): Int {
        return 0
    }
    companion object CREATOR : Parcelable.Creator<MyData> {
        override fun createFromParcel(parcel: Parcel): MyData {
            return MyData(parcel)
        }
        override fun newArray(size: Int): Array<MyData?> {
            return arrayOfNulls(size)
        }
    }
}