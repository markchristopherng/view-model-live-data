package example.vmld

import io.reactivex.Single

/**
 * Created by markng on 13/3/18.
 */

open class PayBillManager {

    /**
     * Get all saved billers in the db
     *
     * @return
     */
    open fun getAll(): Single<List<SavedBill>> {
        var list = mutableListOf(
                SavedBill(1, "Company A", "ABC", "123"),
                SavedBill(1, "Company B", "DEF", "456"),
                SavedBill(1, "Company C", "GHI", "789")
        )
        return Single.just(list)
    }

}