package com.inu.andoid.takepicture

import android.Manifest
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.inu.andoid.takepicture.databinding.ActivityMainBinding
import java.io.File
import java.net.URI
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    lateinit var filePath: String
    private lateinit var resultLauncher : ActivityResultLauncher<Intent>
    // permissionLauncher 선언
    private lateinit var  permissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding
        setContentView(view.root)

        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        val file = File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        )
        filePath = file.absolutePath

        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == RESULT_OK) {
                Log.d("INFO", "RESULT_OK")
                val option = BitmapFactory.Options()
                option.inSampleSize = 10
                val bitmap = BitmapFactory.decodeFile(filePath, option)
                bitmap.let{
                    binding.galleryResult.setImageBitmap(bitmap)
                }
            }
        }


        // 원본사진이 저장되는 uri
        val photoUri: Uri? = FileProvider.getUriForFile(
            this,
            "com.inu.andoid.takepicture.fileprovider",
            file)



        binding.btn1.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // 권한 있는 경우 실행할 코드...
                Log.d("permissions :", "ok")
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                resultLauncher.launch(intent)
            } else {
                // 권한 없는 경우, 권한 요청
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                Log.d("permissions :", "no")
            }
        }
        binding.btn2.setOnClickListener {
        }

        binding.btn3.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // 권한 있는 경우 실행할 코드...
                Log.d("permissions :", "ok")

                resultLauncher.launch(intent)
            } else {
                // 권한 없는 경우, 권한 요청
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                Log.d("permissions :", "no")
            }
        }
        binding.btn4.setOnClickListener {

        }
        binding.btn5.setOnClickListener {

        }


        permissionLauncher = registerForActivityResult(
            RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // 권한 있는 경우 실행할 코드...
            } else {
                AlertDialog.Builder(this)
                    .setTitle("미디어 접근 권한")
                    .setMessage("미디어를 첨부하시려면, 앱 접근 권한을 허용해 주세요.")
                    .setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->
                        val intent = Intent()
                        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        val uri: Uri = Uri.fromParts(
                            "package",
                            BuildConfig.APPLICATION_ID, null
                        )
                        intent.data = uri
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    })
                    .create()
                    .show()
            }
        }

    }

}