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

package roxana.toolkit.forms

import rx._
import utest._

object rxButtonSuite extends TestSuite {

  private implicit val rxCtx: Ctx.Owner = Ctx.Owner.safe()

  val tests = Tests {
    'testAttributes - {
      val button = testButton
      val rendered = button.elem

      // verify attributes
      button.cls ==> rendered.classList.toString
      button.disabled ==> rendered.disabled
      button.tpe ==> rendered.`type`
    }

    'testBinding - {
      import roxana.core.helpers._
      import roxana.core.implicits._

      val boundButton = binding[rxButton]
      val button = testButton.bindTo(boundButton)
      val actual = bound(boundButton)

      // verify bindings
      button ==> actual
    }
  }

  private object Data {
    val cls = "test-class"
    val disabled = true
    val id = "test-button"
    val tpe = "button"
  }

  private def testButton = rxButton("test", cls = Data.cls, disabled = Data.disabled,
    id = Data.id, tpe = Data.tpe)
}
