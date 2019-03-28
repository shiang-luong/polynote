package polynote.runtime.python

import java.util.concurrent.{Callable, ExecutorService}

import jep.python.PyCallable

import scala.collection.JavaConverters._
import scala.language.dynamics

/**
  * Some Scala sugar around [[PyCallable]]
  */
class PythonFunction(callable: PyCallable, runner: PythonObject.Runner) extends PythonObject(callable, runner) with Dynamic {

  private def unwrapArg(arg: Any): Any = arg match {
    case pyObj: PythonObject => pyObj.unwrap
    case obj => obj
  }

  override def applyDynamic(method: String)(args: Any*): Any = {
    if (method == "apply" || method == "call" || method == "__call__")
      callPosArgs(callable, args.asInstanceOf[Seq[AnyRef]])
    else
      super.applyDynamic(method)(args: _*)

  }

  override def applyDynamicNamed(method: String)(args: (String, Any)*): Any = {
    if (method == "apply" || method == "call" || method == "__call__")
      callKwArgs(callable, args)
    else
      super.applyDynamicNamed(method)(args: _*)
  }

}
