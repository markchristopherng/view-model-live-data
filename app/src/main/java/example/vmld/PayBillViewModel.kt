package example.vmld

/**
 * Created by markng on 18/9/18.
 */
class PayBillViewModel : BaseViewModel() {

    var manager: PayBillManager = PayBillManager()

    var bills: StateLiveData<List<SavedBill>> = StateLiveData()

    fun loadBills() {
        manager.getAll()
                .subscribe(bills)
    }
}
