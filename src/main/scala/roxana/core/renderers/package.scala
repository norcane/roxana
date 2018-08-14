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

import org.scalajs.dom
import rx.{Ctx, Rx}

/**
  * Package containing tools for defining custom ''component renderer''.
  */
package object renderers {

  /**
    * The ''component renderer'' represents the way how to wrap and extend the component appearance
    * or behaviour based on the selected environment. For example, custom renderer can be defined
    * for the ''Bootstrap'' CSS library, that would add appropriate CSS classes to the component,
    * to allow the proper styling. Other use can be setting selected CSS class based on the input
    * content validation.
    */
  type Renderer = PartialFunction[Component[_ <: dom.Element], Rx[dom.Element]]

  /**
    * Contains set of default renderers.
    */
  object Renderers {

    /**
      * The default renderer, renders the component as-is.
      *
      * @return rendered component
      */
    def default(implicit rxOwner: Ctx.Owner): Renderer = {
      case component => Rx(component.elem)
    }
  }

}
