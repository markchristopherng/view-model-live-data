package example.vmld

import androidx.lifecycle.viewModelScope

/**
 * Created by markng on 18/9/18.
 */
class PayBillViewModel : BaseViewModel() {

    var manager: PayBillManager = PayBillManager()

    var bills: StateLiveData<List<SavedBill>> = StateLiveData()

    fun loadBillsRx() {
        manager.getAllRx()
                .subscribe(bills)
    }

    fun loadBills() {
        bills.loadWith(viewModelScope) {
            manager.getAll()
        }
    }


}
