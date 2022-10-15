package com.example.chatapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapplication.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {


    private lateinit var recyclerView: RecyclerView
//    private lateinit var userList: ArrayList<firebaseUser>
//    private lateinit var adapter: userAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var ref: DatabaseReference
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragement(home())

        auth = FirebaseAuth.getInstance()
        ref = FirebaseDatabase.getInstance().getReference()

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){

                R.id.DASHBOARD -> replaceFragement(fragmentHome())
                R.id.MARKET -> replaceFragement(fragmentProfile())
                R.id.PROFILE -> replaceFragement(fragmentSetting())
                else->{
                }
            }
            true
        }

        binding.weather.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                // Do some work here
                val currentIntent=Intent(this@MainActivity,wheatherActivity::class.java)
                startActivity(currentIntent)
            }
        })
    }

    private fun replaceFragement(fragment: Fragment)
    {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()
    }
}