package example.vmld

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    fun <T> LiveData<T>.observe(observer: (T?) -> Unit) = observe(this@BaseActivity, Observer { observer(it) })

    fun <T> LiveData<T>.observeNonNull(observer: (T) -> Unit) = observe { if (it != null) observer(it) }

    fun <T> EventLiveData<T>.observeEvent(observer: (T) -> Unit) = observeEvent(this@BaseActivity, Observer { if (it != null) observer(it) })

    inline fun <reified T : BaseViewModel> getViewModel(): T = ViewModelProviders.of(this)[T::class.java]

  }