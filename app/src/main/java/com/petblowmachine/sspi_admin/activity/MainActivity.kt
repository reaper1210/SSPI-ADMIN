package com.petblowmachine.sspi_admin.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AppCompatDelegate
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.firestore.FirebaseFirestore
import com.petblowmachine.sspi_admin.R
import com.petblowmachine.sspi_admin.adapter.ViewPagerAdapter
import com.petblowmachine.sspi_admin.fragment.Categories
import com.petblowmachine.sspi_admin.fragment.Requests
import com.petblowmachine.sspi_admin.modal.Applic

class MainActivity : AppCompatActivity() {
    lateinit var viewpager:ViewPager2
    lateinit var searchEdtText: EditText
    private lateinit var db:FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        db = FirebaseFirestore.getInstance()
        val adminToken = getSharedPreferences(Applic.sharedPreferenceName, MODE_PRIVATE).getString("admin_token","").toString()
        val docData = hashMapOf(
            "token" to adminToken
        )
        db.collection("admin").document("admin").set(docData)

        viewpager = findViewById(R.id.viewpager)
        searchEdtText = findViewById(R.id.searchEdtTxtMainAct)

        val fragmentList = arrayListOf(
            Categories(),
            Requests()
        )
        val adapter = ViewPagerAdapter(
            fragmentList,
            this.supportFragmentManager,
            lifecycle
        )

        viewpager.adapter = adapter

        viewpager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if(position==1){
                    searchEdtText.visibility = View.GONE
                }
                else{
                    searchEdtText.visibility = View.VISIBLE
                }
            }
        })

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        TabLayoutMediator(tabLayout, viewpager) { tab, position ->
            if(position == 0){
                tab.text = "Categories"
            }
            else{
                tab.text = "Requests"
            }
        }.attach()

        for (i in 0 until tabLayout.tabCount) {
            val tab = (tabLayout.getChildAt(0) as ViewGroup).getChildAt(i)
            val p = tab.layoutParams as ViewGroup.MarginLayoutParams
            p.setMargins(0, 0, 20, 0)
            tab.requestLayout()
        }
    }

    override fun onBackPressed() {
        finishAffinity()
    }

}