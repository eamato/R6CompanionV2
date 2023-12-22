package eamato.funn.benchmark

import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.random.Random

/**
 * This is an example startup benchmark.
 *
 * It navigates to the device's home screen, and launches the default activity.
 *
 * Before running this benchmark:
 * 1) switch your app's active build variant in the Studio (affects Studio runs only)
 * 2) add `<profileable android:shell="true" />` to your app's manifest, within the `<application>` tag
 *
 * Run this benchmark from Studio to see startup measurements, and captured system traces
 * for investigating your app's performance.
 */
@RunWith(AndroidJUnit4::class)
class ExampleStartupBenchmark {

    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun startupCompilationModeNone() = startup(CompilationMode.None())

    @Test
    fun startupCompilationModePartial() = startup(CompilationMode.Partial())

    @Test
    fun startupCompilationModeFull() = startup(CompilationMode.Full())

    @Test
    fun testUserJourneyCompilationModeNone() = testUserJourney(CompilationMode.None())

    @Test
    fun testUserJourneyCompilationModePartial() = testUserJourney(CompilationMode.Partial())

    @Test
    fun testUserJourneyCompilationModeFull() = testUserJourney(CompilationMode.Full())

    private fun startup(mode: CompilationMode) = benchmarkRule.measureRepeated(
        packageName = "eamato.funn.r6companion",
        metrics = listOf(StartupTimingMetric()),
        iterations = 5,
        startupMode = StartupMode.COLD,
        compilationMode = mode
    ) {
        pressHome()
        startActivityAndWait()
    }

    private fun testUserJourney(mode: CompilationMode) = benchmarkRule.measureRepeated(
        packageName = "eamato.funn.r6companion",
        metrics = listOf(FrameTimingMetric()),
        iterations = 5,
        startupMode = StartupMode.COLD,
        compilationMode = mode
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

fun MacrobenchmarkScope.allNewsScrollAndClick() {
    repeat(3) {
        device.findObject(By.res(packageName, "rv_news"))
            ?.run {
                setGestureMargin(device.displayWidth / 5)
                fling(Direction.DOWN)
            }
    }

    device.findObject(By.res(packageName, "rv_news"))?.click()

    device.wait(Until.hasObject(By.res(packageName, "iv_news_image")), 5_000L)

    device.pressBack()
}

fun MacrobenchmarkScope.eSportsNewsScrollAndClick() {
    device.wait(Until.hasObject(By.res(packageName, "btn_news_category_esport")), 5_000L)

    device.findObject(By.res(packageName, "btn_news_category_esport"))?.click()

    device.wait(Until.hasObject(By.res(packageName, "rv_news")), 5_000L)

    repeat(3) {
        device.findObject(By.res(packageName, "rv_news"))
            ?.run {
                setGestureMargin(device.displayWidth / 5)
                fling(Direction.DOWN)
            }
    }

    device.findObject(By.res(packageName, "rv_news"))?.click()

    device.wait(Until.hasObject(By.res(packageName, "iv_news_image")), 5_000L)
}

fun MacrobenchmarkScope.navigateToRoulette() {
    device.findObject(By.res(packageName, "bnv"))
        ?.findObject(By.res(packageName, "FragmentRoulette"))
        ?.click()
}

fun MacrobenchmarkScope.goToOperatorsRollResult() {
    device.wait(Until.hasObject(By.res(packageName, "rv_operators")), 5_000L)

    repeat(3) {
        device.findObject(By.res(packageName, "rv_operators"))
            ?.run {
                setGestureMargin(device.displayWidth / 5)
                scroll(Direction.DOWN, Random.nextFloat())
                click()
            }
    }

    device.findObject(By.res(packageName, "btn_go_to_roll_result"))?.click()

    device.wait(Until.hasObject(By.res(packageName, "tv_winner")), 5_000L)
}

fun MacrobenchmarkScope.navigateToCompanion() {
    device.findObject(By.res(packageName, "bnv"))
        ?.findObject(By.res(packageName, "FragmentCompanion"))
        ?.click()
}

fun MacrobenchmarkScope.clickOnOperatorAndFlingResult() {
    repeat(3) {
        device.wait(Until.hasObject(By.res(packageName, "rv_operators")), 5_000L)
        device.findObject(By.res(packageName, "rv_operators"))?.setGestureMargin(device.displayWidth / 5)
        device.findObject(By.res(packageName, "rv_operators"))?.scroll(Direction.DOWN, Random.nextFloat())
        device.findObject(By.res(packageName, "rv_operators"))?.click()

        device.wait(Until.hasObject(By.res(packageName, "rv_operator_details")), 5_000L)

        var direction = Direction.DOWN
        repeat(5) {
            device.findObject(By.res(packageName, "rv_operator_details"))?.run {
                setGestureMargin(device.displayWidth / 5)
                fling(direction)
            }

            device.waitForIdle(5_000L)
            direction = if (direction == Direction.DOWN) Direction.UP else Direction.DOWN
        }

        device.pressBack()
        device.waitForIdle(5_000L)
    }
}

fun MacrobenchmarkScope.navigateToCompanionMapsAndPerformDeepNavigation() {
    device.wait(Until.hasObject(By.res(packageName, "btn_go_to_maps")), 5_000L)

    device.findObject(By.res(packageName, "btn_go_to_maps"))?.click()

    repeat(3) {
        device.wait(Until.hasObject(By.res(packageName, "rv_maps")), 5_000L)
        device.findObject(By.res(packageName, "rv_maps"))?.setGestureMargin(device.displayWidth / 5)
        device.findObject(By.res(packageName, "rv_maps"))?.scroll(Direction.DOWN, Random.nextFloat())
        device.findObject(By.res(packageName, "rv_maps"))?.click()

        device.wait(Until.hasObject(By.res(packageName, "rv_map_blueprints")), 5_000L)
        device.findObject(By.res(packageName, "rv_map_blueprints"))?.run {
            setGestureMargin(device.displayWidth / 5)
            device.waitForIdle(5_000L)
            click()

            device.wait(Until.hasObject(By.res(packageName, "iv_image")), 5_000L)

            device.waitForIdle(5_000L)
            device.pressBack()
            device.waitForIdle(5_000L)
        }
        device.pressBack()
        device.waitForIdle(5_000L)
    }
}