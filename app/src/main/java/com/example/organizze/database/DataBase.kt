package com.example.organizze.database

import com.example.organizze.model.User
import com.example.organizze.others.UserSingleton
import com.google.firebase.database.*

class DataBase {

    companion object {
        @JvmStatic
        private var firebaseDataBase: DatabaseReference? = null

        private fun getFirebaseDB(): DatabaseReference {
            if (firebaseDataBase == null) {
                firebaseDataBase = FirebaseDatabase.getInstance().reference
            }
            return firebaseDataBase!!
        }

        @JvmStatic
        fun saveInDataBase(user: User) {
            this.getFirebaseDB().child("users")
                .child(user.userId)
                .setValue(user)
        }

        @JvmStatic
        fun readUserInformationInDataBase(userId: String) {

            val userReferenceInDb = getFirebaseDB().child("users").child(userId)

            userReferenceInDb.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                   snapshot.getValue(User::class.java).takeIf { true }
                        ?.let { UserSingleton.instance = it }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        }
    }
}