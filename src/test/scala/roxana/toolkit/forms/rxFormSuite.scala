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

object rxFormSuite extends TestSuite {

  private implicit val rxCtx: RoxanaContext = new DefaultRoxanaContext()

  val tests = Tests {
    'testAttributes - {
      val form = testForm
      val rendered = form.elem

      // verify attributes
      form.cls ==> rendered.classList.toString
    }

    'testInputRegistration - {
      val form = testForm
      form.elem // force the creation of the DOM element
      val inputs = form.inputs.now

      inputs.size ==> 1
    }
  }

  private object Data {
    val Cls = "test-class"
  }

  private def testForm = {
    import roxana.core.implicits._

    rxForm(cls = Data.Cls) { implicit form =>
      rxInputTextSuite.testInputText(form)
    }
  }
}
