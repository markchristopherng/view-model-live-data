package example.vmld

import android.arch.lifecycle.MutableLiveData
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * live data containing state information and easy for RxJava integration
 *
 * Created by Xiangyu Zhou on 18/6/18.
 */
class StateLiveData<T> : MutableLiveData<T>() {

    val state = EventLiveData<State>()

    init {
        clearState()
    }

    /**
     * post a block running on a separated thread
     * LiveData will be in loading state before the block is running,
     * and in success state after the block finished running,
     * any exception happen will be captured and send into error state,
     * the value of the block will be used as result
     */
    fun post(block: StateLiveData<T>.() -> T) {
        postLoading()
        Completable.fromAction { postValue(block.invoke(this)) }
                .subscribeOn(Schedulers.io())
                .subscribe({ postSuccess() }, { postError(it) })
    }

    fun post(observable: Observable<T>, successOnFirstValue: Boolean = true): Disposable {
        postLoading()
        return observable.subscribeOn(Schedulers.io())
                .subscribe({
                    if (successOnFirstValue) {
                        postValueAndSuccess(it)
                    } else {
                        postValue(it)
                    }
                }, { postError(it) }, { postSuccess() })
    }

    fun post(flowable: Flowable<T>, successOnFirstValue: Boolean = true): Disposable {
        postLoading()
        return flowable.subscribeOn(Schedulers.io())
                .subscribe({
                    if (successOnFirstValue) {
                        postValueAndSuccess(it)
                    } else {
                        postValue(it)
                    }
                }, { postError(it) }, { postSuccess() })
    }

    fun post(maybe: Maybe<T>): Disposable {
        postLoading()
        return maybe.subscribeOn(Schedulers.io())
                .subscribe({ postValueAndSuccess(it) }, { postError(it) }, { postSuccess() })
    }

    fun post(single: Single<T>): Disposable {
        postLoading()
        return single.subscribeOn(Schedulers.io())
                .subscribe({ postValueAndSuccess(it) }, { postError(it) })
    }

    fun post(completable: Completable): Disposable {
        postLoading()
        return completable.subscribeOn(Schedulers.io())
                .subscribe({ postSuccess() }, { postError(it) })
    }

    private fun postValueAndSuccess(value: T) {
        super.postValue(value)
        postSuccess()
    }

    fun clearState() {
        state.postValue(State.Idle)
    }

    fun postLoading() {
        state.postValue(State.Loading())
    }

    fun postSuccess() {
        state.postValue(State.Success())
    }

    fun postError(throwable: Throwable) {
        state.postValue(State.Error(throwable))
    }

    fun postState(s: State) {
        state.postValue(s)
    }

    /**
     * state of this live data, can be extended
     */
    open class State {

        object Idle : State()

        open class Loading : State()

        open class Success : State()

        open class Error(val error: Throwable) : State()
    }
}