package com.inu.andoid.takepicture

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.inu.andoid.takepicture.databinding.ActivitySubBinding


class SubActivity : AppCompatActivity() {
    lateinit var binding: ActivitySubBinding
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
       binding = ActivitySubBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
         //   fun onClick(View v) {
              //  intent :Intent = Intent(SubActivity.this, MainActivity.class)
                intent.putExtra("result","resultData")
                setResult(RESULT_OK, intent)
                finish()
       //     }
        }
    }
}