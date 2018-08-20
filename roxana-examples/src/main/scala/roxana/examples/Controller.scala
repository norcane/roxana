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

import roxana.core.l10n.Messages
import roxana.core.renderers.Renderer
import roxana.core.{DefaultRoxanaContext, RoxanaContext}
import roxana.examples.screens.ExamplesScreen
import roxana.routing.{ClientController, Screen}

import scala.reflect.ClassTag

object Controller extends ClientController {

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

  override protected implicit val rxCtx: RoxanaContext = new DefaultRoxanaContext {
    override implicit val messages: Messages = l10n.DefaultMessages
    override implicit val renderer: Renderer = renderers.bootstrap4
  }
}
