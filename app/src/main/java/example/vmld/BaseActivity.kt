package example.vmld

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

abstract class BaseActivity : AppCompatActivity() {

    fun <T> LiveData<T>.observe(observer: (T?) -> Unit) = observe(this@BaseActivity, Observer { observer(it) })

    fun <T> LiveData<T>.observeNonNull(observer: (T) -> Unit) = observe { if (it != null) observer(it) }

    fun <T> EventLiveData<T>.observeEvent(observer: (T) -> Unit) = observeEvent(this@BaseActivity, Observer { if (it != null) observer(it) })

    inline fun <reified T : BaseViewModel> getViewModel(): T = ViewModelProviders.of(this)[T::class.java]

  }