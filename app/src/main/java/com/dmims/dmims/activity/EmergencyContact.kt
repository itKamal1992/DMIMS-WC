package com.dmims.dmims.activity

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import com.dmims.dmims.Generic.GenericUserFunction
import com.dmims.dmims.Generic.InternetConnection
import com.dmims.dmims.R
import com.dmims.dmims.adapter.EmergencyContactAdapter
import com.dmims.dmims.common.Common
import com.dmims.dmims.dataclass.EmUser
import com.dmims.dmims.model.APIResponse
import com.dmims.dmims.remote.IMyAPI
import kotlinx.android.synthetic.main.activity_dashboard.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EmergencyContact : AppCompatActivity() {
    lateinit var mServices: IMyAPI
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emergency_contact)
        setSupportActionBar(toolbar)
        mServices = Common.getAPI()
        val progressBar = findViewById<ProgressBar>(R.id.progressBar1)
        val recyclerView = findViewById<RecyclerView>(R.id.contact_list)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        progressBar.visibility = View.VISIBLE
        if (InternetConnection.checkConnection(this)) {
        try {
            mServices.GetEmergencyServices()
                .enqueue(object : Callback<APIResponse> {
                    override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                        Toast.makeText(this@EmergencyContact, t.message, Toast.LENGTH_SHORT).show()
                        progressBar.visibility = View.GONE
                    }

                    override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                        val result: APIResponse? = response.body()
                        if (result!!.Status == "ok") {
                            var k: Int
                            var listSize = result.Data9!!.size
                            val users1 = java.util.ArrayList<EmUser>()
                            for (i in 0..listSize - 1) {
                                     k = R.drawable.ic_contact
                                users1.add(

                                    EmUser(
                                        result.Data9!![i].service_name,
                                        result.Data9!![i].service_handler,
                                        result.Data9!![i].ser_hand_mob_no,
                                        k

                                    )
                                )
                            }
                            progressBar.visibility = View.GONE
                            val adapter = EmergencyContactAdapter(this@EmergencyContact,users1)
                            recyclerView.adapter = adapter
                        } else {
                            progressBar.visibility = View.GONE
                            Toast.makeText(this@EmergencyContact, result.Status, Toast.LENGTH_SHORT).show()
                        }
                    }
                })

        } catch (ex: Exception) {

            ex.printStackTrace()
            GenericUserFunction.showApiError(this,"Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time.")
        }
        }
        else {
            GenericUserFunction.showInternetNegativePopUp(
                this,
                getString(R.string.failureNoInternetErr)
            )
        }

    }

}
