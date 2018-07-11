package roxana.interop

import roxana.interop.all._
import rx._
import scalatags.JsDom.all._
import utest._

object RxNodeInstancesSuite extends TestSuite with TestUtils {

  val tests = Tests {

    val TextOrig = "text-orig"
    val TextNew = "text-new"

    "Text node" - {
      val text = Var(TextOrig)

      "rx.Var" - {
        val node = span(text).render
        testRx(text, node.textContent, TextOrig, TextNew)
      }

      "rx.Rx" - {
        val node = span(text.rx).render
        testRx(text, node.textContent, TextOrig, TextNew)
      }

      "rx.Dynamic" - {
        val node = span(Rx(text())).render
        testRx(text, node.textContent, TextOrig, TextNew)
      }
    }
  }

}
