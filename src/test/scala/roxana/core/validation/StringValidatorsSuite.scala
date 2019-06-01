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

package roxana.core.validation

import utest._

object StringValidatorsSuite extends TestSuite {

  override def tests = Tests {

    'nonEmpty - {
      val i1 = ""
      val v1 = "The Cake is a Lie!"

      assert(
        validators.nonEmpty(i1).isInvalid,
        validators.nonEmpty(v1).isValid
      )
    }
  }
}
