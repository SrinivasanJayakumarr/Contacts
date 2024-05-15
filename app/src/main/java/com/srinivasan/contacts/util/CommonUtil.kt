package com.srinivasan.contacts.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.srinivasan.contacts.R
import com.srinivasan.contacts.domain.model.Contact

object CommonUtil {

    fun String.getProperMobileNumber(): String {
        return this.filter { it.isDigit() }
    }

    fun checkIfContactValid(
        contact: Contact
    ): Boolean {
        return contact.name.isNotBlank()
                || contact.mobile?.isNotBlank() == true
                || contact.phone?.isNotBlank() == true
                || contact.email?.isNotBlank() == true
    }

    fun getProperContact(
        contact: Contact
    ): Contact {
        return if (contact.name.isBlank()) {
            contact.copy(
                name = contact.mobile?.ifBlank { contact.phone?.ifBlank { contact.email?: "Name" } } ?: "Name"
            )
        } else {
            contact
        }
    }

    fun getErrorMessage(message: String): String {
        return when {
            message.contains("Unable to resolve host") -> "No Internet Connection"
            message.contains("timeout") -> "Request Timeout"
            else -> message
        }
    }

    fun makeCall(
        context: Context,
        mobileNumber: String
    ) {
        val callIntent = Intent(Intent.ACTION_DIAL)
        callIntent.data = Uri.parse("tel:$mobileNumber")
        context.startActivity(callIntent)
    }

    fun sendMessage(
        context: Context,
        mobileNumber: String,
        message: String = "Thanks for the opportunity :)"
    ) {
        val smsIntent = Intent(Intent.ACTION_VIEW)
        smsIntent.data = Uri.parse("smsto:$mobileNumber")
        smsIntent.putExtra("sms_body", message)
        context.startActivity(smsIntent)
    }

    fun sendMail(
        context: Context,
        recipientEmail: String,
        subject: String = "Zoho Interview Task",
        body: String = "Greetings!! Have a gud day!"
    ) {

        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:$recipientEmail")
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.putExtra(Intent.EXTRA_TEXT, body)

        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        } else {
            Toast.makeText(
                context,
                context.getString(R.string.no_mail_app_found),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}