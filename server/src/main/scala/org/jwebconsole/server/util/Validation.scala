package org.jwebconsole.server.util

trait Validation[T] {

  def v: T

  def map[E](f: T => E): Validation[E] = {
    this match {
      case Valid(_) => Valid(f(v))
      case Invalid(_, messages) => Invalid(f(v), messages)
    }
  }

  def flatMap[E](f: T => Validation[E]): Validation[E] = {
    val res = f(v)
    res match {
      case Valid(item) => Valid(item)
      case Invalid(item, messages) => {
        this match {
          case Valid(_) => Invalid(item, messages)
          case Invalid(_, msgs) => Invalid(item, msgs ::: messages)
        }
      }
    }
  }

}

case class Valid[T](v: T) extends Validation[T]

case class Invalid[T](v: T, messages: List[String]) extends Validation[T]