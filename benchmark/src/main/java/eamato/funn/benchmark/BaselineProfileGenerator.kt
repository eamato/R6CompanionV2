package eamato.funn.benchmark

import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BaselineProfileGenerator {

    @get:Rule
    val baselineRule = BaselineProfileRule()

    @Test
    fun generateBaselineProfile() = baselineRule.collect(
        packageName = "eamato.funn.r6companion"
    ) {
        pressHome()
        startActivityAndWait()

        eSportsNewsScrollAndClick()
        device.pressBack()
        device.wait(Until.hasObject(By.res(packageName, "rv_news")), 5_000L)
        allNewsScrollAndClick()
        navigateToRoulette()
        goToOperatorsRollResult()
        device.pressBack()
        navigateToCompanion()
        clickOnOperatorAndFlingResult()
        device.pressBack()
        navigateToCompanion()
        navigateToCompanionMapsAndPerformDeepNavigation()
    }
}