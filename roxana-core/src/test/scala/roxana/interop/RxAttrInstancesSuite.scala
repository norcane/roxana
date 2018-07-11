package roxana.interop

import roxana.interop.all._
import rx._
import scalatags.JsDom.all._
import utest._


object RxAttrInstancesSuite extends TestSuite with TestUtils {

  val tests = Tests {

    val TextOrig = "42px"
    val TextNew = "43px"

    "Text attribute" - {
      val widthAttr = Var(TextOrig)

      "rx.Var" - {
        val node = span(widthA := widthAttr).render
        testRx(widthAttr, node.getAttribute("width"), TextOrig, TextNew)
      }

      "rx.Rx" - {
        val node = span(widthA := widthAttr.rx).render
        testRx(widthAttr, node.getAttribute("width"), TextOrig, TextNew)
      }

      "rx.Dynamic" - {
        val node = span(widthA := Rx(widthAttr())).render
        testRx(widthAttr, node.getAttribute("width"), TextOrig, TextNew)
      }
    }
  }

}
