package org.thoughtcrime.securesms.home.listeners

interface MosqueResponseListener {
    fun onSuccess(responseData: String?)
    fun onFailure(errorMessage: String?)
}