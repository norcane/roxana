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

import org.scalajs.dom

object LoadingOverlay {

  private val CssClass = "displayed"
  private val overlay = dom.document.getElementById("loading-overlay")

  def show(): Unit = {
    overlay.classList.add(CssClass)
  }

  def hide(): Unit = {
    overlay.classList.remove(CssClass)
  }
}
