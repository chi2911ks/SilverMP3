package com.cbtool.silvermp3.interfaces

interface FragmentUIConfig {
    fun shouldShowBottomBar(): Boolean = true
    fun getNavigationItemId(): Int
}