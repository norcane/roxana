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

package roxana.routing

import roxana.core.RoxanaContext

import scala.reflect.ClassTag

/**
  * Common base for the client-side controllers.
  */
trait ClientController {

  private var screen: Screen = _

  /**
    * Default ID of the ''DOM'' element, where the screen will be rendered. This element must be
    * already present in the ''DOM'' of the HTML page, otherwise an error is thrown.
    */
  val screenContainerId: String = "rx-screen-container"

  /**
    * Performs the given code over the selected [[Screen]]. If the screen is not rendered yet, new
    * instance is automatically rendered to the [[screenContainerId]] ''DOM'' element.
    *
    * @param screen screen to do modifications on (and render if necessary)
    * @param fn     function that takes the screen instance and performs modifications
    * @tparam T type of the screen
    */
  protected def withScreen[T <: Screen : ClassTag](screen: => T)
                                                  (fn: T => Unit)
                                                  (implicit rxCtx: RoxanaContext): Unit = {
    import roxana.core.helpers._
    val currScreen = screen match {
      case sc: T if implicitly[ClassTag[T]].runtimeClass.isInstance(sc) => sc
      case _ => screen
    }
    fn(currScreen)
    renderComponent(currScreen, screenContainerId)
    this.screen = currScreen
  }

}
