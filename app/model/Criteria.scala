package model

/*
 * classe qui d�finit les diff�rents crit�res de tri
 */
trait Criteria
case object ByName extends Criteria
case object ByDate extends Criteria
