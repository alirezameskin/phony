package alaki
import scala.util.Random

private[alaki] class RandomUtility {
  private val random: Random = new Random()

  def nextBoolean: Boolean = random.nextBoolean

  def nextDouble: Double = random.nextDouble

  def nextFloat: Float = random.nextFloat

  def nextInt: Int = random.nextInt

  def nextInt(max: Int) = random.nextInt(max)

  def randomItem[A](items: Seq[A]): A =
    items(random.nextInt(items.size))

  def randomItems[A](count: Int)(items: Seq[A]): Seq[A] =
    Random.shuffle(items).take(count)
}
