package roxana.routing

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
  protected def withScreen[T <: Screen : ClassTag](screen: => T)(fn: T => Unit): Unit = {
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
