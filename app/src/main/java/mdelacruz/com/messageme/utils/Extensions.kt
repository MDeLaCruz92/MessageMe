package mdelacruz.com.messageme.utils

import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.support.v4.content.ContextCompat
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import java.util.regex.Pattern

/**
 * Created by AmeyJain on 2/26/18.
 */


//==================================================================================================
// View Related
//==================================================================================================

fun View.visible() {
  visibility = View.VISIBLE
}

fun View.invisible() {
  visibility = View.INVISIBLE
}

fun View.gone() {
  visibility = View.GONE
}

fun View.isVisible(): Boolean {
  return visibility == View.VISIBLE
}

fun View.isInvisible(): Boolean {
  return visibility == View.INVISIBLE
}

fun View.isGone(): Boolean {
  return visibility == View.GONE
}

//fun ImageView.load(url: String?) {
//    if (TextUtils.isEmpty(url)) {
//        Picasso.with(context)
//                .load(R.mipmap.ic_launcher)
//                .into(this)
//    } else {
//        Picasso.with(context)
//                .load(url)
//                .into(this)
//    }
//}

//==================================================================================================
// Activity Related
//==================================================================================================

fun Activity.screenWidth(): Int {
  val metrics: DisplayMetrics = DisplayMetrics()
  windowManager.defaultDisplay.getMetrics(metrics)
  return metrics.widthPixels
}

fun Activity.screenHeight(): Int {
  val metrics: DisplayMetrics = DisplayMetrics()
  windowManager.defaultDisplay.getMetrics(metrics)
  return metrics.heightPixels
}

fun Activity.color(resId: Int) : Int {
  return ContextCompat.getColor(this, resId)
}

// used for simple start activity without Intent parameters
fun Activity.start(clazz: Class<out Activity>) {
  startActivity(Intent(this, clazz))
}

// used for simple start activity without Intent parameters
fun Activity.startWithIntentFlag(clazz: Class<out Activity>, intentFlag:Int) {
  val intent = Intent(this, clazz)
  intent.setFlags(intentFlag)
  startActivity(intent)
}

// simple start activity when using request code
fun Activity.startForResult(clazz: Class<out Activity>, requestCode: Int) {
  startActivityForResult(Intent(this, clazz), requestCode)
}

// simpler way to start replacing fragments with one line of code
fun Activity.startFragment(id: Int, fragment: Fragment, tag: String) {
  fragmentManager
      .beginTransaction()
      .add(id, fragment, tag)
      .addToBackStack(tag)
      .commit()
}
//==================================================================================================
// Toast Related
//==================================================================================================

// used for show a toast message in the center of the screen
fun Context.toast(message: Int) {
  val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
  toast.setGravity(Gravity.CENTER, 0, 0)
  toast.show()
}

fun Fragment.toast(message: Int) = activity.toast(message)

//==================================================================================================
// String related
//==================================================================================================

val EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$"

val DIGIT_CASE_PATTERN = "[0-9 ]"

val specailCharPatten = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE)
val UpperCasePatten = Pattern.compile("[A-Z ]")
val lowerCasePatten = Pattern.compile("[a-z ]")
val digitCasePatten   = Pattern.compile("[0-9 ]")

// used for validate if the current String is an email
fun String.isValidEmail(): Boolean {
  val pattern = Pattern.compile(EMAIL_PATTERN)
  return pattern.matcher(this).matches()
}

fun String.isValidPhone(): Boolean {
  val pattern = Pattern.compile(DIGIT_CASE_PATTERN)
  return pattern.matcher(this).matches()
}

fun String.isInValidPassword(): Boolean {
  return length < 8
}

fun EditText.textString(): String {
  return text.toString()
}

fun TextView.textString(): String {
  return text.toString()
}

