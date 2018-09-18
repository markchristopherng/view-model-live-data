package example.vmld

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_pay_bill.bills_textview

class PayBillActivity : BaseActivity() {

    lateinit var payBillViewModel : PayBillViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay_bill)

        payBillViewModel = getViewModel()
        payBillViewModel.loadBills()

        //Display bills
        payBillViewModel.bills.observeNonNull {
            displayBills(it)
        }

        //Display error & spinner
        payBillViewModel.bills.state.observeEvent {
            if ( it is StateLiveData.State.Error) {
                displayError()
            }
            displaySpinner( it is StateLiveData.State.Loading)
        }
    }

    fun displayBills(bills : List<SavedBill>) {
        bills_textview.text = bills.toString()
    }

    fun displayError() {
    }

    fun displaySpinner(show : Boolean) {
    }
}
