package model

/*
 * classe qui définit les différents critères de tri
 */
trait Criteria
case object ByName extends Criteria
case object ByDate extends Criteria
