package org.itstep.android5.vetroumova.newbeginning.sixjars.ui;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by OLGA on 15.09.2016.
 */
public class BottomNavFABBehavior extends CoordinatorLayout.Behavior<FloatingActionButton> {

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        //return super.onDependentViewChanged(parent, child, dependency);
        /*float translationY = getFabTranslationYForSnackbar(parent, child);
        float percentComplete = -translationY / dependency.getHeight();
        float scaleFactor = 1 - percentComplete;

        child.setScaleX(scaleFactor);
        child.setScaleY(scaleFactor);*/
        return false;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        //return super.layoutDependsOn(parent, child, dependency);
        //return dependency instanceof AHBottomNavigation;
        return dependency instanceof Snackbar.SnackbarLayout;
    }
}
