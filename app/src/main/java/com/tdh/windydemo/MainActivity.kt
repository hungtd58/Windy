package com.tdh.windydemo

import androidx.fragment.app.Fragment
import com.tdh.libase.base.BaseActivity
import com.tdh.windydemo.home.HomeFragment

class MainActivity : BaseActivity() {
    override fun initFirstFragment(): Fragment {
        return HomeFragment()
    }
}