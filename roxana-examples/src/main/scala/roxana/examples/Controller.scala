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

package roxana.examples

import roxana.examples.screens.ExamplesScreen
import roxana.routing.{ClientController, Screen}
import rx._

import scala.reflect.ClassTag

object Controller extends ClientController {

  private implicit val rxCtx: Ctx.Owner = Ctx.Owner.safe()

  def home(): Unit = Router.routeTo(Routes.examples())

  def examples(): Unit = withScreen(new ExamplesScreen()) { screen =>
    screen.exampleName() = None
  }

  def example(demoName: String): Unit = withScreen(new ExamplesScreen()) { screen =>
    screen.exampleName() = Some(demoName)
  }

  override protected def withScreen[T <: Screen : ClassTag](screen: => T)(fn: T => Unit): Unit = {
    super.withScreen(screen)(fn)
    LoadingOverlay.hide()
  }

}
