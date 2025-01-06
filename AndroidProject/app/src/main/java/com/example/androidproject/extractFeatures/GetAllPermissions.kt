package com.example.androidproject.extractFeatures

import android.Manifest
import android.content.Intent
import android.util.Log
import java.lang.reflect.Modifier

fun getAllPermissions() {
    val listOfPermissions = ArrayList<String>()
    val listOfIntent = ArrayList<String>()


    var allpermis = String()
    var allint = String()

    val whatever = Manifest.permission().javaClass.declaredFields
    for (field in whatever) {
        if (Modifier.isStatic(field.modifiers)) {
            listOfPermissions.add(field.name)
            allpermis = allpermis + field.name + "\n"
//            permissionsFile.writeText(field.name + "\n")
        }
    }

    val allintents = Intent().javaClass.declaredFields
    for (field in allintents) {
        if (Modifier.isStatic(field.modifiers)) {
            listOfIntent.add(field.name)
            allint = allint + field.name + "\n"
        }
    }

////    val allint1 = MimeTypes
//    for (field in allint1) {
//        if (Modifier.isStatic(field.modifiers)) {
//            listOfIntent.add(field.name)
//            allint = allint + field.name + "\n"
//        }
//    }


    Log.d("permissions", "total nr of permissions: %d".format(listOfPermissions.size))
}

//fun testSMTH(){
////    Manifest.permission().javaClass.declaredFields
//    val listOfIntent = ArrayList<String>()
//    Intent().javaClass.declaredFields.forEach {
//        if(Modifier.isStatic(it.modifiers)){
//            lis
//        }
//    }
//}