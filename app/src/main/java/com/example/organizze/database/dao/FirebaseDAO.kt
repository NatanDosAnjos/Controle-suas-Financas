package com.example.organizze.database.dao

import com.example.organizze.model.FinancialMovement
import com.example.organizze.model.User
import com.example.organizze.others.GlobalUserInstance
import com.google.firebase.database.*


class FirebaseDAO {

    companion object {

        private var firebaseDataBase: DatabaseReference? = null
            get() {
                if (field == null) {
                    field = FirebaseDatabase.getInstance().reference
                }
                return field
            }
    }

        /*private fun getFirebaseDB(): DatabaseReference {
            if (firebaseDataBase == null) {
                firebaseDataBase = FirebaseDatabase.getInstance().reference
            }
            return firebaseDataBase!!
        }*/

        fun saveInDataBase(user: User, firstChild: String) {
            firebaseDataBase!!.child(firstChild)
                .child(user.userId)
                .setValue(user)
        }

        fun saveInDataBase(financialMovement: FinancialMovement) {
            firebaseDataBase!!
                .child(FinancialMovement.FIRST_CHILD)
                .child(financialMovement.userId)
                .child(financialMovement.year)
                .child(financialMovement.month)
                .child(financialMovement.day)
                .child(financialMovement.category)
                .child(financialMovement.incomeOrExpense)
                .child(financialMovement.description)
                .setValue(financialMovement)
        }

        fun readUserInformationInDataBase(userId: String) {

            val userReferenceInDb = firebaseDataBase!!.child(User.FIRST_CHILD).child(userId)

            userReferenceInDb.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                   snapshot.getValue(User::class.java).takeIf { true }
                        ?.let { GlobalUserInstance.instance = it }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        }

        fun updateUserInformation(userId: String, financialMovement: FinancialMovement) {
            val userReference = firebaseDataBase!!
                .child(User.FIRST_CHILD)
                .child(userId)

            if (financialMovement.isExpense()) {
                val newValue = GlobalUserInstance.instance.totalExpenses + financialMovement.value
                userReference
                    .child(User.STRING_TOTAL_EXPENSES)
                    .setValue(newValue)
            } else {
                val newValue = GlobalUserInstance.instance.totalIncome + financialMovement.value
                userReference
                    .child(User.STRING_TOTAL_INCOME)
                    .setValue(newValue)
            }
        }
}