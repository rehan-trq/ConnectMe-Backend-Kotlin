package com.arshman_rehan.i222427_i220965

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView
import java.io.ByteArrayOutputStream

class EditProfilePage : AppCompatActivity() {

    private lateinit var profileImage: CircleImageView
    private lateinit var name: EditText
    private lateinit var username: EditText
    private lateinit var contact: EditText
    private lateinit var bio: EditText
    private lateinit var updateButton: Button
    private lateinit var usernameDisplay: TextView

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private var userId: String? = null

    private var encodedImage: String? = null
    private val pickImageRequest = 1 // Not used anymore, kept for reference

    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            val imageUri: Uri? = result.data?.data
            imageUri?.let {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, it)
                profileImage.setImageBitmap(bitmap)
                encodeImage(bitmap)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile_page)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("RegisteredUsers")

        profileImage = findViewById(R.id.ProfilePicture)
        name = findViewById(R.id.nameEditText)
        username = findViewById(R.id.usernameEditText)
        contact = findViewById(R.id.contactEditText)
        bio = findViewById(R.id.bioEditText)
        updateButton = findViewById(R.id.myBtn)
        usernameDisplay = findViewById(R.id.Username)

        userId = auth.currentUser?.uid

        if (userId == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        loadUserData(userId!!)

        profileImage.setOnClickListener {
            openImagePicker()
        }

        updateButton.setOnClickListener {
            updateUserData(userId!!)
        }
    }

    private fun loadUserData(userId: String) {
        database.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val user = snapshot.getValue(userCredential::class.java)
                    user?.let {
                        name.setHint(it.name ?: "")
                        username.setHint(it.username ?: "")
                        contact.setHint(it.phoneNumber ?: "")
                        bio.setHint(it.bio.takeIf { b -> b?.isNotEmpty() == true } ?: "write your bio...")
                        usernameDisplay.text = it.name ?: ""

                        if (it.profileImage.isNotEmpty()) {
                            val decodedImage = Base64.decode(it.profileImage, Base64.DEFAULT)
                            val bitmap = BitmapFactory.decodeByteArray(decodedImage, 0, decodedImage.size)
                            profileImage.setImageBitmap(bitmap)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@EditProfilePage, "Failed to load data: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        imagePickerLauncher.launch(intent)
    }

    private fun encodeImage(bitmap: Bitmap) {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        val byteArray = outputStream.toByteArray()
        encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun checkUsernameUnique(newUsername: String, currentUserId: String, onResult: (Boolean) -> Unit) {
        database.orderByChild("username").equalTo(newUsername)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (userSnapshot in snapshot.children) {
                            val userKey = userSnapshot.key
                            if (userKey != currentUserId) {
                                onResult(false) // Username exists for another user
                                return
                            }
                        }
                        onResult(true) // Username is either not used or only by the current user
                    } else {
                        onResult(true) // Username doesn’t exist in the database
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@EditProfilePage, "Error checking username: ${error.message}", Toast.LENGTH_SHORT).show()
                    onResult(false) // Default to false on error to prevent update
                }
            })
    }

    private fun updateUserData(userId: String) {
        database.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val user = snapshot.getValue(userCredential::class.java)
                    user?.let {
                        val updatedName = name.text.toString().trim().ifEmpty { it.name ?: "" }
                        // Only update username if it’s non-empty; otherwise, keep the existing one
                        val updatedUsernameInput = username.text.toString().trim()
                        val updatedUsername = if (updatedUsernameInput.isNotEmpty()) updatedUsernameInput else it.username ?: ""
                        val updatedContact = contact.text.toString().trim().ifEmpty { it.phoneNumber ?: "" }
                        val updatedBio = bio.text.toString().trim().ifEmpty { it.bio ?: "" }

                        if (updatedUsername != it.username && updatedUsername.isNotEmpty()) {
                            checkUsernameUnique(updatedUsername, userId) { isUnique ->
                                if (!isUnique) {
                                    Toast.makeText(this@EditProfilePage, "Username already taken", Toast.LENGTH_SHORT).show()
                                    return@checkUsernameUnique
                                }
                                performUpdate(userId, updatedName, updatedUsername, updatedContact, updatedBio, it.profileImage)
                            }
                        } else {
                            performUpdate(userId, updatedName, updatedUsername, updatedContact, updatedBio, it.profileImage)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@EditProfilePage, "Failed to load data: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun performUpdate(
        userId: String,
        updatedName: String,
        updatedUsername: String,
        updatedContact: String,
        updatedBio: String,
        currentProfileImage: String?
    ) {
        val updates = mapOf(
            "name" to updatedName,
            "username" to updatedUsername,
            "phoneNumber" to updatedContact,
            "bio" to updatedBio,
            "profileImage" to (encodedImage ?: currentProfileImage ?: "")
        )

        database.child(userId).updateChildren(updates)
            .addOnSuccessListener {
                Toast.makeText(this@EditProfilePage, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@EditProfilePage, ProfilePage::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this@EditProfilePage, "Failed to update profile", Toast.LENGTH_SHORT).show()
            }
    }
}