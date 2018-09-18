package example.vmld

import android.arch.lifecycle.ViewModel
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable

/**
 * base view model enable toothpick injection, holding states of all [StateLiveData], be aware of
 * loading state for ViewModel and has better integration with RxJava
 *
 * Created by Xiangyu Zhou on 13/3/18.
 */
@Suppress("LeakingThis")
open class BaseViewModel : ViewModel() {

    private val disposableList = mutableListOf<Disposable>()

    // integration with rx
    fun <T> Observable<T>.subscribe(stateLiveData: StateLiveData<T>, successOnFirstValue: Boolean = true) = stateLiveData.post(this, successOnFirstValue).also { disposableList.add(it) }

    fun <T> Flowable<T>.subscribe(stateLiveData: StateLiveData<T>, successOnFirstValue: Boolean = true) = stateLiveData.post(this, successOnFirstValue).also { disposableList.add(it) }

    fun <T> Maybe<T>.subscribe(stateLiveData: StateLiveData<T>) = stateLiveData.post(this).also { disposableList.add(it) }

    fun <T> Single<T>.subscribe(stateLiveData: StateLiveData<T>) = stateLiveData.post(this).also { disposableList.add(it) }

    fun Completable.subscribe(stateLiveData: StateLiveData<*>) = stateLiveData.post(this).also { disposableList.add(it) }

    override fun onCleared() {
        disposableList.filter { !it.isDisposed }.forEach { it.dispose() }
        disposableList.clear()
        super.onCleared()
    }
}