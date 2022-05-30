package com.group25.timebanking.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.group25.timebanking.R
import com.group25.timebanking.models.Request
import com.group25.timebanking.models.Session
import com.group25.timebanking.models.UserRating
import com.group25.timebanking.utils.Database


class SessionAdapter(
    private val data: List<Session>,
    context: Context,
    layoutInflater: LayoutInflater
) :
    RecyclerView.Adapter<SessionAdapter.ViewHolder>() {

    val context: Context = context
    val layoutInflater: LayoutInflater = layoutInflater

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tvTitle: TextView = v.findViewById<TextView>(R.id.tvTitle)
        val tvDateTime: TextView = v.findViewById<TextView>(R.id.tvDateTime)
        val tvDuration: TextView = v.findViewById<TextView>(R.id.tvDuration)
        val tvLocation: TextView = v.findViewById<TextView>(R.id.tvLocation)
        val tvUser: TextView = v.findViewById<TextView>(R.id.tvUser)
        val btnWriteFeedback = v.findViewById<Button>(R.id.btnWriteFeedback)

        fun bind(session: Session) {
            tvDateTime.text = session.AdDateTime
            tvTitle.text = session.AdTitle
            tvLocation.text = session.AdLocation
            tvDuration.text = session.AdDuration.toString() + " hours"
            var mAuth = FirebaseAuth.getInstance()
            if(session.OrganiserUser==mAuth.currentUser!!.email!!){
                tvUser.text = "Attendee:  "+session.AttendingUserName
                if(!session.AttendingUserFeedbackActive)
                    btnWriteFeedback.visibility=View.GONE
            }
            else{
                tvUser.text = "Organiser:  "+session.OrganiserUserName
                if(!session.OrganiserUserFeedbackActive)
                    btnWriteFeedback.visibility=View.GONE
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SessionAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.session_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: SessionAdapter.ViewHolder, position: Int) {
        holder.bind(data[position])

        holder.btnWriteFeedback.setOnClickListener {
            val builder = AlertDialog.Builder(context).create()
            val view = layoutInflater.inflate(R.layout.dialog_write_feedback, null)
            val ratingBarComment = view.findViewById<RatingBar>(R.id.ratingBarComment)
            val etFeedback = view.findViewById<EditText>(R.id.etFeedback)
            val btnCancel = view.findViewById<Button>(R.id.btnCancel)
            val btnOK = view.findViewById<Button>(R.id.btnOK)
            builder.setView(view)
            btnCancel.setOnClickListener {
                builder.dismiss()
            }
            btnOK.setOnClickListener {
                val mAuth = FirebaseAuth.getInstance()
                var userId = if(data[position].OrganiserUser==mAuth.currentUser!!.email!!){
                    data[position].AttendingUserName
                } else{
                    data[position].OrganiserUserName
                }
                var isOrganiser = data[position].OrganiserUser==mAuth.currentUser!!.email!!

                val rating = UserRating(
                    id = "",
                    UserId = userId,
                    SessionId = data[position].id!!,
                    Rating = ratingBarComment.rating.toInt(),
                    IsOrganiser = isOrganiser,
                    Feedback = etFeedback.text.toString()
                )
                Database.getInstance(context).saveUserRating(rating) {
                    Toast.makeText(context,"Your feedback saved", Toast.LENGTH_SHORT).show()
                    builder.dismiss()
                }
            }
            builder.setCanceledOnTouchOutside(false)
            builder.show()
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}