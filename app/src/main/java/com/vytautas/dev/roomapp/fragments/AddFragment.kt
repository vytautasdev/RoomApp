package com.vytautas.dev.roomapp.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.vytautas.dev.roomapp.R
import com.vytautas.dev.roomapp.data.User
import com.vytautas.dev.roomapp.data.UserViewModel
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_add.view.*


class AddFragment : Fragment() {

    private lateinit var mUserViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add, container, false)

        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        view.add_button.setOnClickListener {
            insertDataToDatabase()
        }

        return view
    }

    private fun insertDataToDatabase() {
        val firstName = add_first_name_et.text.toString()
        val lastName = add_last_name_et.text.toString()
        val age = add_age_et.text

        if (inputCheck(firstName, lastName, age)) {
            //Create user object
            val user = User(0, firstName, lastName, Integer.parseInt(age.toString()))
            //Add data to database
            mUserViewModel.addUser(user)
            Toast.makeText(
                requireContext(),
                "Successfully added user: ${user.firstName}",
                Toast.LENGTH_SHORT
            ).show()
            //Navigate back
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        } else {
            Toast.makeText(
                requireContext(),
                "Please fill out all fields.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun inputCheck(firstName: String, lastName: String, age: Editable): Boolean {
        return !(
                TextUtils.isEmpty(firstName)
                        || TextUtils.isEmpty(lastName)
                        || TextUtils.isEmpty(age)
                )
    }
}