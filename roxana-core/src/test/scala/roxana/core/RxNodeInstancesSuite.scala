/*
 * roxana :: Reactive UI component framework for Scala.js applications
 * Copyright (c) 2018 norcane
 * ---
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package roxana.core

import roxana.core.implicits._
import rx._
import scalatags.JsDom.all._
import utest._

object RxNodeInstancesSuite extends TestSuite with TestUtils {

  val tests = Tests {

    val TextOrig = "text-orig"
    val TextNew = "text-new"

    val text = Var(TextOrig)

    'rxVar - {
      val node = span(text).render
      testRx(text, node.textContent, TextOrig, TextNew)
    }

    'rxRx - {
      val node = span(text.rx).render
      testRx(text, node.textContent, TextOrig, TextNew)
    }

    'rxDynamic - {
      val node = span(Rx(text())).render
      testRx(text, node.textContent, TextOrig, TextNew)
    }
  }

}
