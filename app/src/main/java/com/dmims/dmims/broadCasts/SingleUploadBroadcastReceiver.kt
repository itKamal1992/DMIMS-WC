package com.dmims.dmims.broadCasts
import net.gotev.uploadservice.UploadServiceBroadcastReceiver

class SingleUploadBroadcastReceiver : UploadServiceBroadcastReceiver() {

    interface Delegate {
        fun onProgress(progress: Int)
        fun onProgress(uploadedBytes: Long, totalBytes: Long)
        fun onError(exception: Exception)
        fun onCompleted(serverResponseCode: Int, serverResponseBody: ByteArray)
        fun onCancelled()
    }

    private var mUploadID: String? = null
    private var mDelegate: Delegate? = null

    fun setUploadID(uploadID:String){
        mUploadID=uploadID
    }

    fun setDelegate(delegate: Delegate){
        mDelegate=delegate
    }

    override fun onProgress(uploadId: String?, progress: Int) {
        if(uploadId.equals(mUploadID)&& mDelegate!=null) {
            mDelegate!!.onProgress(progress)
        }


    }

    override fun onProgress(uploadId: String?, uploadedBytes: Long, totalBytes: Long) {
        if(uploadId.equals(mUploadID)&& mDelegate!=null) {
            mDelegate!!.onProgress(uploadedBytes,totalBytes)
        }
    }

    override fun onError(uploadId: String?, exception: java.lang.Exception?) {
        if(uploadId.equals(mUploadID)&& mDelegate!=null) {
            if (exception != null) {
                mDelegate!!.onError(exception)
            }
        }
    }

    override fun onCompleted(
        uploadId: String?,
        serverResponseCode: Int,
        serverResponseBody: ByteArray?
    ) {
        if(uploadId.equals(mUploadID) && mDelegate!=null) {
            if (serverResponseBody != null) {
                mDelegate!!.onCompleted(serverResponseCode,serverResponseBody)
            }
        }
    }

    override fun onCancelled(uploadId: String?) {
        if(uploadId.equals(mUploadID)&& mDelegate!=null) {
            mDelegate!!.onCancelled()
        }
    }
}