package roxana.interop

import rx._
import utest._

trait TestUtils {

  implicit class RichVar[T](x: Var[T]) {
    def rx: Rx[T] = x
  }

  def testRx[T](rxVar: Var[T], rxResult: => T, initial: T, newValue: T): Unit = {
    rxResult ==> initial
    rxVar() = newValue
    rxResult ==> newValue
  }

}
