package com.example.audiorecorderfinalchoice

import java.io.File

interface OnItemClick {
    fun onItemClickListener(file: File,position: Int)
}