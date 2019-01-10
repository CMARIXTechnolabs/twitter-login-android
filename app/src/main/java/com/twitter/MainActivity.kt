package com.twitter

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.webkit.CookieManager
import android.webkit.CookieSyncManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.identity.TwitterAuthClient


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val TAG = "MainActivity"
    val CONSUMER_KEY = "AlqfZ7Rzmn6eMhC4W0PVJK46h"
    val CONSUMER_SECRET = "Fw64iOOgDBPiWNIouB6R3yEaqlcs4Gi0xUdFSz8gyiIo4Z5Xhz"

    internal var mTwitterAuthClient: TwitterAuthClient? = null

    internal var btnLogin: Button? = null
    internal var btnLogut: Button? = null
    internal var txtViewDetails: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val config = TwitterConfig.Builder(this)
            .logger(DefaultLogger(Log.DEBUG))//enable logging when app is in debug mode
            .twitterAuthConfig(
                TwitterAuthConfig(
                    CONSUMER_KEY,
                    CONSUMER_SECRET
                )
            )
            //pass the created app Consumer KEY and Secret also called API Key and Secret
            .debug(true)//enable debug mode
            .build()

        //finally initialize twitter with created configs
        Twitter.initialize(config)

        setContentView(R.layout.activity_main)

        btnLogin = findViewById(R.id.btnLogin) as Button
        btnLogin!!.setOnClickListener(this)
        btnLogin!!.visibility = View.VISIBLE

        btnLogut = findViewById(R.id.btnLogut) as Button
        btnLogut!!.setOnClickListener(this)
        btnLogut!!.visibility = View.GONE

        txtViewDetails = findViewById(R.id.txtViewDetails) as TextView
        txtViewDetails!!.visibility = View.GONE

        mTwitterAuthClient = TwitterAuthClient()
    }

    private fun getTwitterSession(): TwitterSession? {

        //NOTE : if you want to get token and secret too use uncomment the below code
        /*TwitterAuthToken authToken = session.getAuthToken();
        String token = authToken.token;
        String secret = authToken.secret;*/

        return TwitterCore.getInstance().sessionManager.activeSession
    }

    //TODO: Twitter Login-----------------------
    fun twitterLogin() {
        if (getTwitterSession() == null) {
            mTwitterAuthClient!!.authorize(this, object : Callback<TwitterSession>() {
                override fun success(twitterSessionResult: Result<TwitterSession>) {
                    Toast.makeText(this@MainActivity, "Success", Toast.LENGTH_SHORT).show()
                    val twitterSession = twitterSessionResult.data
                    fetchTwitterEmail(twitterSession)

                }

                override fun failure(e: TwitterException) {
                    Toast.makeText(this@MainActivity, "Failure", Toast.LENGTH_SHORT).show()
                }
            })
        } else {//if user is already authenticated direct call fetch twitter email api
            fetchTwitterEmail(getTwitterSession())
        }
    }

    fun fetchTwitterEmail(twitterSession: TwitterSession?) {
        mTwitterAuthClient?.requestEmail(twitterSession, object : Callback<String>() {
            override fun success(result: Result<String>) {
                //here it will give u only email and rest of other information u can get from TwitterSession

                Log.d(TAG, "twitterLogin:userId" + twitterSession!!.userId)
                Log.d(TAG, "twitterLogin:userName" + twitterSession!!.userName)
                Log.d(TAG, "twitterLogin: result.data" + result.data)

//                val i = Intent(this@LoginActivity, SignupActivity::class.java)
//                val bundle = Bundle()
//                bundle.putString(Utils.FIRST_NAME, "")
//                bundle.putString(Utils.LAST_NAME, "")
//                bundle.putString(Utils.EMAIL, result.data)
//                bundle.putString(Utils.AUTH_TYPE, "TWITTER")
//                bundle.putString(Utils.TPA_TOKEN, twitterSession.userId.toString())
//                i.putExtras(bundle)
//                startActivity(i)
//                finish()
                btnLogin!!.visibility = View.GONE
                txtViewDetails!!.visibility = View.VISIBLE
                btnLogut!!.visibility = View.VISIBLE
                var userId = twitterSession!!.userId
                var username = twitterSession!!.userName
                var email = result.data
                var token = twitterSession.userId.toString()
                var str = "Now you are successfully login with twitter \n\n"
                var tokenStr = ""
                var usernameStr = ""
                var emailStr = ""
                if (token != null || token != "") {
                    tokenStr = "User Id : " + token + "\n\n"
                }

                if (username != null || username != "") {
                    usernameStr = "Username : " + username + "\n\n"
                }

//                if (email != null || email != "") {
//                    emailStr = "Email ID : " + email + "\n\n"
//                }

                txtViewDetails!!.setText("" + str + tokenStr + usernameStr + emailStr)

            }

            override fun failure(exception: TwitterException) {
                Toast.makeText(this@MainActivity, "Failed to authenticate. Please try again.", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (mTwitterAuthClient != null)
            mTwitterAuthClient!!.onActivityResult(requestCode, resultCode, data)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btnLogin -> {
                if (checkInternetConnection(this@MainActivity))
                    twitterLogin()
            }

            R.id.btnLogut -> {
                if (checkInternetConnection(this@MainActivity)){
                    Toast.makeText(this@MainActivity, "Logout Successfull.", Toast.LENGTH_SHORT)
                        .show()
//                    mTwitterAuthClient!!.cancelAuthorize()
                    CookieSyncManager.createInstance(this);
                    var cookieManager = CookieManager.getInstance();
                    cookieManager.removeSessionCookie();
                    TwitterCore.getInstance().getSessionManager().clearActiveSession()
                    btnLogin!!.visibility = View.VISIBLE
                    btnLogut!!.visibility = View.GONE
                    txtViewDetails!!.visibility = View.GONE
                    txtViewDetails!!.setText("")
                }
            }
        }

    }

    fun checkInternetConnection(context: Context): Boolean {

        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (cm.activeNetworkInfo != null
            && cm.activeNetworkInfo.isAvailable
            && cm.activeNetworkInfo.isConnected
        ) {
            true
        } else {
            false
        }
    }
}
