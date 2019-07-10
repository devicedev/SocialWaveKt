package com.devicedev.socialwavekt.ui.main.fragments.profile

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.devicedev.socialwavekt.R
import com.devicedev.socialwavekt.data.retrofit.ServiceGenerator
import com.devicedev.socialwavekt.ui.main.MainActivity
import com.devicedev.socialwavekt.utils.*
import kotlinx.android.synthetic.main.profile_fragment.*
import kotlinx.android.synthetic.main.profile_fragment.birthdayTextView
import kotlinx.android.synthetic.main.profile_fragment.emailEditText
import kotlinx.android.synthetic.main.profile_fragment.femaleRadioButton
import kotlinx.android.synthetic.main.profile_fragment.maleRadioButton
import kotlinx.android.synthetic.main.profile_fragment.nameEditText
import kotlinx.android.synthetic.main.profile_fragment.passwordEditText
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URI
import java.util.*
import java.util.Calendar.*

class ProfileFragment(val token: String) : Fragment() {

    companion object {

        private val REQUEST_GALLERY: Int = 1

        private val REQUEST_CAMERA: Int = 2

        private val REQUEST_PERMISSION_READ_EXTERNAL_STORAGE: Int = 3

        private val REQUEST_PERMISSION_CAMER_WRITE_EXTERNAL_STORAGE: Int = 4

        private const val TAG = "ProfileFragment"

    }

    private lateinit var viewModel: ProfileViewModel

    private lateinit var activity: MainActivity

    private var image: File? = null
    private var imageURI: Uri? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.profile_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)

        activity = super.getActivity() as MainActivity

        passwordEditText.setText(TEST_PASSWORD)

        setUpViewModel()

        setUpRefreshLayout()

        setUpEditButton()

        setUpImageView()


    }

    private fun setUpImageView() {
        profileImageView.setOnClickListener {

            val pictureDialog = AlertDialog.Builder(activity)
            pictureDialog.setTitle(getString(R.string.select_action))

            pictureDialog.setItems(
                arrayOf(
                    getString(R.string.select_gallery),
                    getString(R.string.select_camera)
                )
            ) { dialog, which ->
                when (which) {
                    0 -> {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                            if (activity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                                requestPermissions(
                                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                                    REQUEST_PERMISSION_READ_EXTERNAL_STORAGE
                                )
                            } else {
                                choosePhotoFromGallery()

                            }

                        } else {
                            choosePhotoFromGallery()

                        }

                    }
                    1 -> {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                            if (
                                activity.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                                activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                            ) {
                                requestPermissions(
                                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                    REQUEST_PERMISSION_CAMER_WRITE_EXTERNAL_STORAGE
                                )
                            } else {
                                takePhotoFromCamera()

                            }

                        } else {
                            takePhotoFromCamera()

                        }

                    }
                }

            }
            pictureDialog.show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_PERMISSION_READ_EXTERNAL_STORAGE ->
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    choosePhotoFromGallery()
                } else {
                    activity.onError(R.string.err_permission_denied)
                }
            REQUEST_PERMISSION_CAMER_WRITE_EXTERNAL_STORAGE ->
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePhotoFromCamera()
                } else {
                    activity.onError(R.string.err_permission_denied)

                }

        }

    }

    private fun choosePhotoFromGallery() {

        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(intent, REQUEST_GALLERY)

    }

    private fun takePhotoFromCamera() {

        val values = ContentValues()

        values.put(MediaStore.Images.Media.TITLE, "New Picture")

        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")

        imageURI = activity.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageURI)

        startActivityForResult(intent, REQUEST_CAMERA)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when {
            resultCode == RESULT_OK -> {

                when (requestCode) {
                    REQUEST_GALLERY -> {

                        data?.let {
                            val uri = it.data

                            uri?.let { uri ->

                                val path = getPathFromURI(activity, uri)

                                path?.let {

                                    setImage(uri.toString())

                                    image = File(it)
                                }


                            }

                        }


                    }
                    REQUEST_CAMERA -> {
                        imageURI?.let { uri ->

                            val path = getPathFromURI(activity, uri)

                            path?.let {

                                setImage(uri.toString())

                                image = File(it)
                            }

                        }
                    }
                }

            }
            requestCode == REQUEST_CAMERA -> activity.onError(R.string.err_failed_take_photo)
            requestCode == REQUEST_GALLERY -> activity.onError(R.string.err_failed_fetch_photo)
        }

    }

    private fun setUpEditButton() {
        editButton.setOnClickListener {

            activity.toggleProgressBar(View.VISIBLE)

            val name = nameEditText.text.toString().trim()

            val email = emailEditText.text.toString().trim()

            val password = passwordEditText.text.toString().trim()

            val gender = if (maleRadioButton.isChecked && !femaleRadioButton.isChecked) {

                "male"

            } else if (femaleRadioButton.isChecked && !maleRadioButton.isChecked) {

                "female"

            } else {

                null

            }

            var success = true

            val nameResult = Validator.Register.name(name)

            val emailResult = Validator.Login.email(email)

            val passwordResult = Validator.Register.password(password)

            val genderResult = Validator.Register.gender(gender)

            if (!nameResult.success) {

                nameEditText.error = getString(nameResult.message)

                success = false
            }

            if (!emailResult.success) {

                emailEditText.error = getString(emailResult.message)

                success = false
            }
            if (!passwordResult.success) {

                passwordEditText.error = getString(passwordResult.message, passwordResult.arg)

                success = false

            }
            if (!genderResult.success) {

                activity.onError(getString(genderResult.message))

                success = false

            }
            if (!success) {

                activity.toggleProgressBar(View.INVISIBLE)

                return@setOnClickListener
            }

            viewModel.edit(token, name, email, password, gender!!, image)

            image = null
            imageURI = null

        }
    }

    private fun setUpRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener {

            swipeRefreshLayout.isRefreshing = true

            viewModel.refresh(token)


        }
    }

    private fun setUpViewModel() {
        viewModel.isRefreshing.observe(this, Observer {
            swipeRefreshLayout.isRefreshing = it
        })

        viewModel.progressBar.observe(this, Observer {
            activity.toggleProgressBar(it)
        })

        viewModel.user.observe(this, Observer {
            it?.let {

                with(it) {

                    nameEditText.setText(name)

                    emailEditText.setText(email)

                    birthdayTextView.text = getFormatedBirthday()

                    when (gender) {
                        "male" -> maleRadioButton.isChecked = true
                        "female" -> femaleRadioButton.isChecked = true
                    }

                    setImage("${ServiceGenerator.BASE_URL}images/${it.image}")

                }
            }
        })

    }

    private fun setImage(file: String) {

        val circularProgressDrawable = CircularProgressDrawable(activity)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        Glide.with(activity)
            .load(file)
            .apply(RequestOptions().placeholder(circularProgressDrawable).circleCrop())
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(profileImageView)

    }


}