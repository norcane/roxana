/*
 * roxana :: Reactive UI component framework for Scala.js applications
 * Copyright (c) 2018-2019 norcane
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

import roxana.core.{DefaultRoxanaContext, RoxanaContext}
import utest._

object rxInputTextSuite extends TestSuite {

  private implicit val rxCtx: RoxanaContext = new DefaultRoxanaContext()

  val tests = Tests {
    'testAttributes - {
      implicit val form: rxForm = rxForm.empty
      val input = testInputText
      val rendered = input.elem

      // verify attributes
      input.cls ==> rendered.classList.toString
      input.id ==> rendered.id
      input.tpe ==> rendered.`type`
    }

    'testBindings - {
      import roxana.core.helpers._
      import roxana.core.implicits._

      implicit val form: rxForm = rxForm.empty
      val boundControl = binding[rxInputText[Required]]
      val inputText = testInputText.bindTo(boundControl)
      val actual = bound(boundControl)

      // verify bindings
      inputText ==> actual
    }
  }

  private[roxana] object Data {
    val cls = "test-class"
    val id = "test-inputText"
    val tpe = "text"
  }

  private[roxana] def testInputText(implicit form: rxForm) =
    rxInputText[Required](cls = Data.cls, id = Data.id, tpe = Data.tpe)
}
