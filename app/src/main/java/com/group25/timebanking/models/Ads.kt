package com.group25.timebanking.models

import java.util.*

class Ads(val title: String,
          val description:String,
          val date: Date,
          val time: String,
          val duration: Int,
          val location: String){
    constructor() : this("","",Date(),"00:00",0,"")

}