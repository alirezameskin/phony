package phony

import scala.util.Random

private[phony] class RandomUtility {
  private val random: Random = new Random()

  def numerify(format: String): String = "#".r.replaceAllIn(format, nextInt.toString)

  def letterify(format: String): String = """\?""".r.replaceAllIn(format, nextChar.toString)

  def bothify(format: String) = letterify(numerify(format))

  def nextBoolean: Boolean = random.nextBoolean

  def nextDouble: Double = random.nextDouble

  def nextFloat: Float = random.nextFloat

  def nextChar: Char = (random.nextInt(25) + 65).toChar

  def nextInt: Int = random.nextInt

  def nextInt(max: Int) = random.nextInt(max)

  def randomItem[A](items: Seq[A]): A =
    items(random.nextInt(items.size))

  def randomItems[A](count: Int)(items: Seq[A]): List[A] =
    (1 until count).foldLeft(List.empty[A]) { (list, _) =>
      items match {
        case Nil => Nil
        case array => array(nextInt(array.size)) +: list
      }
    }
}
