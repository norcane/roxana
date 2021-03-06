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

import roxana.core.l10n.Messages
import roxana.core.renderers.{Renderer, Renderers}
import rx.Ctx

/**
  * Renderers for the ''roxana'' toolkit components.
  */
package object renderers {

  def bootstrap4(implicit rxCtx: Ctx.Owner, messages: Messages): Renderer =
    BootstrapV4Renderers.all.orElse(Renderers.default)

}
