package com.vytautas.dev.roomapp.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.vytautas.dev.roomapp.R
import com.vytautas.dev.roomapp.data.User
import com.vytautas.dev.roomapp.data.UserViewModel
import kotlinx.android.synthetic.main.fragment_update.*
import kotlinx.android.synthetic.main.fragment_update.view.*


class UpdateFragment : Fragment() {

    private lateinit var mUserViewModel: UserViewModel

    private val args by navArgs<UpdateFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_update, container, false)

        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        view.update_first_name_et.setText(args.currentUser.firstName)
        view.update_last_name_et.setText(args.currentUser.lastName)
        view.update_age_et.setText(args.currentUser.age.toString())

        view.update_button.setOnClickListener {
            updateItem()
            //Navigate back
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }

        //Add menu
        setHasOptionsMenu(true)

        return view
    }

    private fun inputCheck(firstName: String, lastName: String, age: Editable): Boolean {
        return !(
                TextUtils.isEmpty(firstName)
                        || TextUtils.isEmpty(lastName)
                        || age.isEmpty()
                )
    }

    private fun updateItem() {
        val firstName = update_first_name_et.text.toString()
        val lastName = update_last_name_et.text.toString()
        val age = Integer.parseInt(update_age_et.text.toString())

        if (inputCheck(firstName, lastName, update_age_et.text)) {

            //Update user object
            val updatedUser = User(args.currentUser.id, firstName, lastName, age)
            //Update data in database
            mUserViewModel.updateUser(updatedUser)
            Toast.makeText(
                requireContext(),
                "Successfully updated user: ${args.currentUser.firstName}",
                Toast.LENGTH_SHORT
            ).show()

        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_delete) {
            deleteUser()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun deleteUser() {

        val builder = AlertDialog.Builder(requireContext())
        builder.apply {
            setPositiveButton("Yes") { _, _ ->
                mUserViewModel.deleteUser(args.currentUser)
                Toast.makeText(
                    requireContext(),
                    "Successfully deleted: ${args.currentUser.firstName}.",
                    Toast.LENGTH_SHORT
                ).show()
                //Navigate back
                findNavController().navigate(R.id.action_updateFragment_to_listFragment)
            }
            setNegativeButton("Cancel") { _, _ ->
                return@setNegativeButton
            }
            setTitle("Delete ${args.currentUser.firstName}?")
            setMessage("Are you sure you want to delete ${args.currentUser.firstName}?")
            create().show()
        }
    }
}