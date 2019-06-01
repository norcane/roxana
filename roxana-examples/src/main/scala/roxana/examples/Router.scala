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

package roxana.examples

import roxana.routing.{HashBangRouter, StaticRoutes}

object Router extends HashBangRouter {
  override val routes: StaticRoutes = Routes
}

//noinspection TypeAnnotation
object Routes extends StaticRoutes {

  val home = route(root) to Controller.home
  val examples = route(root / "examples") to Controller.examples
  val example = route(root / "examples" / *) to Controller.example

}

