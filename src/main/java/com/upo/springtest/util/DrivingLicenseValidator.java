package com.upo.springtest.util;

import com.upo.springtest.enums.Category;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DrivingLicenseValidator {

    public static List<Category> getDrivingCategories(String drivingLicenseNumber){
        List<Category> categories = new ArrayList<>();
        int x = Character.getNumericValue(drivingLicenseNumber.charAt(0));
        if(x % 2 == 0)
            categories.add(Category.B);
        if (x == 0)
            categories.add(Category.A);
        else if(x == 2)
            categories.add(Category.C);
        else if(x == 1)
            categories.add(Category.D);
        else if(x == 3)
            categories.add(Category.T);

        return categories;
    }
}
