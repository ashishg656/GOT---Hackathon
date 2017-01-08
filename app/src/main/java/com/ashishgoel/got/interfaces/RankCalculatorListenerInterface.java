package com.ashishgoel.got.interfaces;

import com.ashishgoel.got.objects.kingDetails.KingDetailsObject;

import java.util.HashMap;

/**
 * Created by Ashish on 08/01/17.
 */

public interface RankCalculatorListenerInterface {

    void onCalculationStared();

    void onCalculationCompleted(HashMap<String, KingDetailsObject> result);
}
