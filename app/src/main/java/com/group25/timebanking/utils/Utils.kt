package com.group25.timebanking.utils

import com.group25.timebanking.models.UserRating

class Utils {
    fun getTotalUserRating(ratingList: ArrayList<UserRating>):Int {
        var totalRate: Int = 0
        for (rate in ratingList) {
            totalRate += rate.Rating
        }
        return totalRate / ratingList.size
    }

    fun getUserRatingAsOrganiser(ratingList: ArrayList<UserRating>):Int {
        var totalRate: Int = 0
        var count: Int = 0;
        for (rate in ratingList) {
            if(rate.IsOrganiser) {
                totalRate += rate.Rating
                count++
            }
        }
        return totalRate / count
    }

    fun getUserRatingAsAttendee(ratingList: ArrayList<UserRating>):Int {
        var totalRate: Int = 0
        var count: Int = 0;
        for (rate in ratingList) {
            if(!rate.IsOrganiser) {
                totalRate += rate.Rating
                count++
            }
        }
        return totalRate / count
    }
}