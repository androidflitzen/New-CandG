package com.flitzen.cng.test_customer_list;
import com.flitzen.cng.model.CustomerModel;

import java.util.Comparator;

public class LetterComparator implements Comparator<CustomerModelTest> {

    @Override
    public int compare(CustomerModelTest l, CustomerModelTest r) {
        if (l == null || r == null) {
            return 0;
        }

        String lhsSortLetters = l.getName().substring(0, 1).toUpperCase();
        String rhsSortLetters = r.getName().substring(0, 1).toUpperCase();
        if (lhsSortLetters == null || rhsSortLetters == null) {
            return 0;
        }
        return lhsSortLetters.compareTo(rhsSortLetters);
    }
}