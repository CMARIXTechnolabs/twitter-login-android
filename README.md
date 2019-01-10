# Twitter Login Integration - Andriod
<a target="_blank" href="LICENSE"><img src="https://img.shields.io/badge/licence-MIT-brightgreen.svg" alt="license : MIT"></a>
<a target="_blank" href="https://www.cmarix.com/android-application-development-services.html"><img src="https://img.shields.io/badge/platform-android-blue.svg" alt="platform : android"></a>

## Core Features ##

 - User can login with their twitter account
 
## How it works ##

 - User can press on Login with Twitter button from the main activity
 - User will be redirected to Twitter windows where user can entered their credential for the login
 - If user will enter the correct detail, app will fetch the basicdetail from the twitter and it will display on the same screen.
- You also need to create one app on twitter console which can be used in the coding for the twitter authentication.

## Steps to create Twitter App ##

**Step 1**: [Click here](https://twitter.com/login?redirect_after_login=https:/developer.twitter.com/content/developer-twitter/en.html) to login with your Twitter account to create a new app

**Step 2**: Click on your username to view Apps option
![](https://www.cmarix.com/git/Mobile/twitter-login-android/twitter_apps.png)

**Step 3**: Click on Apps to get list of Twitter apps which you have create
![](https://www.cmarix.com/git/Mobile/twitter-login-android/twitter_app_listing.png)

**Step 4**: Click on Create an app to create a new Twitter App
 ![](https://www.cmarix.com/git/Mobile/twitter-login-android/twitter_create_new_app.png)

**Step 5**: Click on Details button to view the App details, Key and Tokens and permissions for that app
![](https://www.cmarix.com/git/Mobile/twitter-login-android/twitter_keys.png)

**Step 6**: You can copy Consumer API Key and Consumer Secret Key and use it in your Application
  
## Requirements ##

 - Android JellyBean 4.1 
 

## Code Snippet ##

**Step 1**: Add twitter librabary in Gradle file to use it in code

    Gradle File

	defaultConfig {
        applicationId "com.twitter"
        minSdkVersion 16
        targetSdkVersion 28
        versionCode 1
        versionName "1.0"
    }

	dependencies {

    //Twitter SDK
    implementation 'com.twitter.sdk.android:twitter-core:3.1.1'
	}
   
**Step 2**: onCreate() method is used to verify Twitter developer account keys. 

	// We will get these keys from twitter developer account app

	val CONSUMER_KEY = "Your Consumer key"
	val CONSUMER_SECRET = "Your Consumer secret key"


	//It is intialization of twitter in onCreate() 

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


        
 
**Step 3**: getTwitterSession() method lets us know that Twitter sesion is active or expired
  
	// Get result of twitter session is active or not

	private fun getTwitterSession(): TwitterSession? {

        //NOTE : if you want to get token and secret too use uncomment the below code
        /*TwitterAuthToken authToken = session.getAuthToken();
        String token = authToken.token;
        String secret = authToken.secret;*/

        return TwitterCore.getInstance().sessionManager.activeSession
    }


**Step 4**: twitterLogin() method verifies twitter login credentials and allows user to login.

    // This function is use to twitter login

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

 **Step 5**: fetchTwitterEmail() fetches all necessory user details from twitter account.
 

	// Get the twitter user details.

	fun fetchTwitterEmail(twitterSession: TwitterSession?) {
        mTwitterAuthClient?.requestEmail(twitterSession, object : Callback<String>() {
            override fun success(result: Result<String>) {
                //here it will give u only email and rest of other information u can get from TwitterSession

                Log.d(TAG, "twitterLogin:userId" + twitterSession!!.userId)
                Log.d(TAG, "twitterLogin:userName" + twitterSession!!.userName)
                Log.d(TAG, "twitterLogin: result.data" + result.data)

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

                txtViewDetails!!.setText("" + str + tokenStr + usernameStr + emailStr)

            }

            override fun failure(exception: TwitterException) {
                Toast.makeText(this@MainActivity, "Failed to authenticate. Please try again.", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

	
    
## Let us know! ##
We’d be really happy if you sent us links to your projects where you use our component. Just send an email to [biz@cmarix.com](mailto:biz@cmarix.com "biz@cmarix.com") and do let us know if you have any questions or suggestion regarding twitter login in android app.

P.S. We’re going to publish more awesomeness examples on third party libaries, coding standards, plugins etc, in all the technology. Stay tuned!

## License ##

	MIT License
	
	Copyright © 2019 CMARIX TechnoLabs
	
	Permission is hereby granted, free of charge, to any person obtaining a copy
	of this software and associated documentation files (the "Software"), to deal
	in the Software without restriction, including without limitation the rights
	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
	copies of the Software, and to permit persons to whom the Software is
	furnished to do so, subject to the following conditions:
	
	The above copyright notice and this permission notice shall be included in all
	copies or substantial portions of the Software.
	
	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
	SOFTWARE.

 