package eamato.funn.r6companion

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    data class DummyObject(
        var str: String,
        var int: Int
    )

    data class TestOperator(
        val str: String?,
        val int: Int?,
        val float: Float?,
        val testOperatorInnerClass: TestOperatorInnerClass?
    ) {
        data class TestOperatorInnerClass(
            val list: List<TestOperatorInnerClassInnerClass?>?
        ) {
            data class TestOperatorInnerClassInnerClass(
                val str: String?
            )
        }
    }

    @Test
    fun list_isEqual() {
        val list1 = listOf(
            TestOperator(
                "equals",
                1,
                0.2f,
                TestOperator.TestOperatorInnerClass(
                    listOf(
                        TestOperator.TestOperatorInnerClass.TestOperatorInnerClassInnerClass(
                            "equals"
                        )
                    )
                )
            ),
            TestOperator(
                "equals2",
                2,
                0.4f,
                TestOperator.TestOperatorInnerClass(
                    listOf(
                        TestOperator.TestOperatorInnerClass.TestOperatorInnerClassInnerClass(
                            "equals2"
                        )
                    )
                )
            )
        )
        val list2 = listOf(
            TestOperator(
                "equals",
                1,
                0.2f,
                TestOperator.TestOperatorInnerClass(
                    listOf(
                        TestOperator.TestOperatorInnerClass.TestOperatorInnerClassInnerClass(
                            "equals"
                        )
                    )
                )
            ),
            TestOperator(
                "equals2",
                2,
                0.4f,
                TestOperator.TestOperatorInnerClass(
                    listOf(
                        TestOperator.TestOperatorInnerClass.TestOperatorInnerClassInnerClass(
                            "equals2"
                        )
                    )
                )
            )
        )
        val list3 = listOf(
            TestOperator(
                "equals_not",
                0,
                0f,
                TestOperator.TestOperatorInnerClass(
                    listOf(
                        TestOperator.TestOperatorInnerClass.TestOperatorInnerClassInnerClass(
                            "equals_not"
                        )
                    )
                )
            ),
            TestOperator(
                "equals_not2",
                0,
                0f,
                TestOperator.TestOperatorInnerClass(
                    listOf(
                        TestOperator.TestOperatorInnerClass.TestOperatorInnerClassInnerClass(
                            "equals_not2"
                        )
                    )
                )
            )
        )

        assertEquals(true, list1 == list2)
        assertEquals(false, list1 == list3)
    }

    @Test
    fun list_copy() {
        val source = listOf(
            DummyObject("1", 1),
            DummyObject("2", 2),
            DummyObject("3", 3),
            DummyObject("4", 4),
            DummyObject("5", 5)
        )
        val res = source.map { it.copy() }.toList()
        source[0].int = 99

        assertEquals(false, source == res)
    }

    @Test
    fun data_class_equality() {
        val obj1 = DummyObject("1", 1)
        val obj2 = DummyObject("1", 1)
        val obj3 = obj1.copy()

        assertEquals(true, obj1 == obj2)
        assertEquals(true, obj1 == obj3)
        assertEquals(true, obj2 == obj3)

        val obj4 = obj3.copy(int = 99)

        assertEquals(false, obj4 == obj1)
        assertEquals(false, obj4 == obj2)
        assertEquals(false, obj4 == obj3)
    }
}