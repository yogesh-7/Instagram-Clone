package com.example.instagram

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.instagram.Model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_account_setting.*
import kotlinx.android.synthetic.main.activity_comments.*

class CommentsActivity : AppCompatActivity() {

    private var postId = ""
    private var publisherId = ""
    private var firebaseUser : FirebaseUser ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)


        val intent = intent
        postId = intent.getStringExtra("postId")!!
        publisherId = intent.getStringExtra("publisherId")!!
        firebaseUser = FirebaseAuth.getInstance().currentUser


        userinfo()

        post_comment.setOnClickListener(View.OnClickListener {
            if(add_comment!!.text.toString() == "" ){
                Toast.makeText(this@CommentsActivity, "Please write comments first" , Toast.LENGTH_LONG)
            }
            else{

                addComment()
            }
        })





    }

    private fun addComment() {
        val commentsRef = FirebaseDatabase.getInstance().reference
            .child("Comments")
            .child(postId!!)
        val commentsMap = HashMap<String , Any>()
        commentsMap["comment"] = add_comment!!.text.toString()
        commentsMap["publisher"] = firebaseUser!!.uid

        commentsRef.push().setValue(commentsMap)
        add_comment!!.text.clear()
    }


    private fun userinfo() {

        val userRef = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                //  if (context!= null) return
                if (snapshot.exists()){

                    val user  = snapshot.getValue(User::class.java)
                    Picasso.get().load(user!!.getimage()).placeholder(R.drawable.profile).into(profile_image_comment)

                }

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

}