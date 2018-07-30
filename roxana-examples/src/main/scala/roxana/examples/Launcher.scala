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

import roxana.core.renderers.Renderer

/**
  * ''Scala.js'' bootstrap code for the Roxana example project.
  */
object Launcher {

  implicit val renderer: Renderer = roxana.examples.renderers.bootstrap4

  def main(args: Array[String]): Unit = {
    println(".: Welcome to the Roxana example project :.")
    Router.init()
  }

}
