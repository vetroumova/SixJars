package org.itstep.android5.vetroumova.newbeginning.sixjars.ui;

/**
 * Created by OLGA on 15.09.2016.
 */

/*extends CoordinatorLayout.Behavior<FloatingActionButton>*/
public class BottomNavFABBehavior {
/*
    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        //return super.onDependentViewChanged(parent, child, dependency);
        *//*float translationY = getFabTranslationYForSnackbar(parent, child);
        float percentComplete = -translationY / dependency.getHeight();
        float scaleFactor = 1 - percentComplete;

        child.setScaleX(scaleFactor);
        child.setScaleY(scaleFactor);*//*
        return false;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        //return super.layoutDependsOn(parent, child, dependency);
        //return dependency instanceof AHBottomNavigation;
        return dependency instanceof Snackbar.SnackbarLayout;
    }*/
}
