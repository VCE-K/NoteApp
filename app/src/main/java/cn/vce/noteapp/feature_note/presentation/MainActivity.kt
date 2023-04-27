package cn.vce.noteapp.feature_note.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.vce.noteapp.R
import cn.vce.noteapp.feature_note.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}

