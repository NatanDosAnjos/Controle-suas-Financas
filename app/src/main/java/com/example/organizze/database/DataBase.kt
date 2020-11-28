package com.example.organizze.database

import com.example.organizze.model.FinancialMovement
import com.example.organizze.model.User
import com.example.organizze.others.GlobalUserInstance
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
        fun saveInDataBase(user: User, firstChild: String) {
            this.getFirebaseDB().child(firstChild)
                .child(user.userId)
                .setValue(user)
        }

        @JvmStatic
        fun saveInDataBase(financialMovement: FinancialMovement) {
            this.getFirebaseDB()
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

        @JvmStatic
        fun readUserInformationInDataBase(userId: String) {

            val userReferenceInDb = getFirebaseDB().child(User.FIRST_CHILD).child(userId)

            userReferenceInDb.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                   snapshot.getValue(User::class.java).takeIf { true }
                        ?.let { GlobalUserInstance.instance = it }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        }

        @JvmStatic
        fun updateUserInformation(userId: String, financialMovement: FinancialMovement) {
            val userReference = this.getFirebaseDB()
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
}