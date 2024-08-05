package com.example.ninja

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.ViewTreeObserver
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.yandex.mobile.ads.banner.BannerAdEventListener
import com.yandex.mobile.ads.banner.BannerAdSize
import com.yandex.mobile.ads.banner.BannerAdView
import com.yandex.mobile.ads.common.AdError
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.AdRequestConfiguration
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.common.MobileAds
import com.yandex.mobile.ads.rewarded.Reward
import com.yandex.mobile.ads.rewarded.RewardedAd
import com.yandex.mobile.ads.rewarded.RewardedAdEventListener
import com.yandex.mobile.ads.rewarded.RewardedAdLoadListener
import com.yandex.mobile.ads.rewarded.RewardedAdLoader
import kotlin.math.roundToInt

@Suppress("DEPRECATION")
class MainActivity() : AppCompatActivity(), Parcelable, AnimationListener {
    private var rewardedAd: RewardedAd? = null
    private var rewardedAdLoader: RewardedAdLoader? = null
    private lateinit var myNinjaOnMap: ImageView
    private lateinit var soldierOnMap: ImageView
    private lateinit var tapOnScreen: Button
    private lateinit var shopButton: Button
    private lateinit var moneyText: TextView
    private lateinit var inAnimation: Animation
    private lateinit var outAnimation: Animation
    private lateinit var bannerAd: BannerAdView
    private lateinit var gift: Button
    private var money: Long = global.getMoney()
    private var power: Long = global.getPower()
    private lateinit var antars: String
    private var moneyToWtch: Long = money
    private var flaganim = false
    private lateinit var prefs: SharedPreferences



    constructor(parcel: Parcel) : this() {
        money = parcel.readLong()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(money)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MainActivity> {
        override fun createFromParcel(parcel: Parcel): MainActivity {
            return MainActivity(parcel)
        }

        override fun newArray(size: Int): Array<MainActivity?> {
            return arrayOfNulls(size)
        }
    }

    @SuppressLint("SetTextI18n", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Since we're loading the banner based on the adContainerView size,
        // we need to wait until this view is laid out before we can get the width
        findViewById<BannerAdView>(R.id.adContainerView).viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                findViewById<BannerAdView>(R.id.adContainerView).viewTreeObserver.removeOnGlobalLayoutListener(this);
                bannerAd = loadBannerAd(adSize)
            }
        })
        MobileAds.initialize(this) {
            // now you can use ads
        }
        rewardedAdLoader = RewardedAdLoader(this).apply {
            setAdLoadListener(object : RewardedAdLoadListener {
                override fun onAdLoaded(rewarded: RewardedAd) {
                    rewardedAd = rewarded
                    // The ad was loaded successfully. Now you can show loaded ad.
                }

                override fun onAdFailedToLoad(error: AdRequestError) {
                    // Ad failed to load with AdRequestError.
                    // Attempting to load a new ad from the onAdFailedToLoad() method is strongly discouraged.
                }
            })
        }
        loadRewardedAd()

        window.navigationBarColor = ContextCompat.getColor(this, R.color.black)

        myNinjaOnMap = findViewById(R.id.myNinjaOnMap)
        soldierOnMap = findViewById(R.id.SoldierOnMap)
        tapOnScreen = findViewById(R.id.tapOnScreen)
        moneyText = findViewById(R.id.moneyText)
        shopButton = findViewById(R.id.shopButton)
        bannerAd = findViewById(R.id.adContainerView)
        gift = findViewById(R.id.gift)
        antars = ""
        inAnimation = AnimationUtils.loadAnimation(this, R.anim.hit_in)
        inAnimation.setAnimationListener(this)
        outAnimation = AnimationUtils.loadAnimation(this, R.anim.hit_out)
        outAnimation.setAnimationListener(object: AnimationListener{
            override fun onAnimationStart(p0: Animation?) {
                myNinjaOnMap.setImageResource(R.drawable.ninja_run_left)
            }

            override fun onAnimationEnd(p0: Animation?) {
                myNinjaOnMap.setImageResource(R.drawable.ninja_stop_right)
                flaganim = false
            }

            override fun onAnimationRepeat(p0: Animation?) {
            }

        })

        prefs = getSharedPreferences("settings", Context.MODE_PRIVATE)

        global.chekSettings(prefs)
        money = global.getMoney()
        power = global.getPower()
        calcAntars()
        moneyText.text = "$moneyToWtch $antars ан."

        tapOnScreen.setOnClickListener {
            if (!flaganim) {
                flaganim = true
                myNinjaOnMap.startAnimation(inAnimation)
            }
            money+= power
            calcAntars()
            moneyText.text = "$moneyToWtch $antars ан."
        }
        shopButton.setOnClickListener {
            global.setMoney(money)
            val shop = Intent(this, Shop::class.java, )
            startActivityForResult(shop, 1)
        }
        gift.setOnClickListener {
            showAd()
        }
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        money = global.getMoney()
        power = global.getPower()
        calcAntars()
        moneyText.text = "$moneyToWtch $antars ан."
    }

    override fun onAnimationStart(p0: Animation?) {
        myNinjaOnMap.setImageResource(R.drawable.ninja_run_right)
    }

    override fun onAnimationEnd(p0: Animation?) {
        myNinjaOnMap.setImageResource(R.drawable.ninja_stop_right)
        myNinjaOnMap.startAnimation(outAnimation)
        soldierOnMap.startAnimation(AnimationUtils.loadAnimation(this, R.anim.soldierhit))
    }

    override fun onAnimationRepeat(p0: Animation?) {
    }

    private val adSize: BannerAdSize
        get() {
            // Calculate the width of the ad, taking into account the padding in the ad container.
            var adWidthPixels = bannerAd.width
            if (adWidthPixels == 0) {
                // If the ad hasn't been laid out, default to the full screen width
                adWidthPixels = resources.displayMetrics.widthPixels
            }
            val adWidth = (adWidthPixels / resources.displayMetrics.density).roundToInt()

            return BannerAdSize.stickySize(this, adWidth)
        }

    private fun loadBannerAd(adSize: BannerAdSize): BannerAdView {
        return bannerAd.apply {
            setAdSize(adSize)
            setAdUnitId("R-M-10717292-3")
//            setAdUnitId("demo-banner-yandex")
            setBannerAdEventListener(object : BannerAdEventListener {
                override fun onAdLoaded() {
                    // If this callback occurs after the activity is destroyed, you
                    // must call destroy and return or you may get a memory leak.
                    // Note `isDestroyed` is a method on Activity.
                    if (isDestroyed) {
                        bannerAd.destroy()
                        return
                    }
                }

                override fun onAdFailedToLoad(error: AdRequestError) {
                    // Ad failed to load with AdRequestError.
                    // Attempting to load a new ad from the onAdFailedToLoad() method is strongly discouraged.
                }

                override fun onAdClicked() {
                    // Called when a click is recorded for an ad.
                }

                override fun onLeftApplication() {
                    // Called when user is about to leave application (e.g., to go to the browser), as a result of clicking on the ad.
                }

                override fun onReturnedToApplication() {
                    // Called when user returned to application after click.
                }

                override fun onImpression(impressionData: ImpressionData?) {
                    // Called when an impression is recorded for an ad.
                }
            })
            loadAd(
                AdRequest.Builder()
                    // Methods in the AdRequest.Builder class can be used here to specify individual options settings.
                    .build()
            )
        }
    }

    private fun loadRewardedAd() {
        val adRequestConfiguration = AdRequestConfiguration.Builder("R-M-10717292-4").build()
        rewardedAdLoader?.loadAd(adRequestConfiguration)
    }

    fun showAd() {
        rewardedAd?.apply {
            setAdEventListener(object : RewardedAdEventListener {
                override fun onAdShown() {
                    // Called when ad is shown.
                }

                override fun onAdFailedToShow(adError: AdError) {
                    // Called when an RewardedAd failed to show

                    // Clean resources after Ad failed to show
                    destroyRewardedAd()

                    // Now you can preload the next rewarded ad.
                    loadRewardedAd()
                }

                override fun onAdDismissed() {
                    // Called when ad is dismissed.
                    // Clean resources after Ad dismissed
                    destroyRewardedAd()

                    // Now you can preload the next rewarded ad.
                    loadRewardedAd()
                }

                override fun onAdClicked() {
                    // Called when a click is recorded for an ad.
                }

                override fun onAdImpression(impressionData: ImpressionData?) {
                    // Called when an impression is recorded for an ad.
                }

                @SuppressLint("SetTextI18n")
                override fun onRewarded(reward: Reward) {
                    // Called when the user can be rewarded.
                    money+=10000
                    moneyText.text = "$money ан."
                    global.setMoney(money)
                }
            })
            show(this@MainActivity)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        rewardedAdLoader?.setAdLoadListener(null)
        rewardedAdLoader = null
        destroyRewardedAd()
    }

    private fun destroyRewardedAd() {
        rewardedAd?.setAdEventListener(null)
        rewardedAd = null
    }

    private fun calcAntars(){
        if (money/10000000000>0) {
            antars = "млрд."
            moneyToWtch = money/1000000000
        } else if (money/10000000>0) {
            antars = "млн."
            moneyToWtch = money/1000000
        } else if (money/10000>0) {
            antars = "тыс."
            moneyToWtch = money/1000
        } else {
            antars = ""
            moneyToWtch = money
        }
    }

    @SuppressLint("CommitPrefEdits")
    override fun onStop() {
        global.setMoney(money)
        global.setSettings(prefs)
        super.onStop()
    }

//    override fun onResume() {
//        super.onResume()
//        global.chekSettings(prefs)
//        money = global.getMoney()
//        power = global.getPower()
//    }
}