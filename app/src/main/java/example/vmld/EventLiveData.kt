package example.vmld

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.Observer
import android.os.Handler
import android.os.Looper
import java.util.concurrent.atomic.AtomicBoolean

/**
 * live data that guarantees that for each observer
 * 1) not data will be swallowed
 * 2) [observeEvent] method to ensure one event can only be seen once
 *
 * Created by Xiangyu Zhou on 18/6/18.
 */
class EventLiveData<T> : MediatorLiveData<T>() {

    private var isRead: AtomicBoolean = AtomicBoolean(false)

    /**
     * ensure the event is non-null and can only been seen once
     */
    fun observeEvent(owner: LifecycleOwner, observer: Observer<T>) {
        super.observe(owner, Observer {
            if (it != null && !isRead.get()) {
                observer.onChanged(it)
                isRead.set(true)
            }
        })
    }

    /**
     * re-implemented post method, as the original implementation may swallow date changes
     * by ignored all data before last [postValue] call
     */
    override fun postValue(value: T) {

        if (Thread.currentThread() == Looper.getMainLooper().thread) {
            setValue(value)
        } else {
            Handler(Looper.getMainLooper()).post { setValue(value) }
        }
    }

    override fun setValue(value: T) {
        isRead.set(false)
        super.setValue(value)
    }
}