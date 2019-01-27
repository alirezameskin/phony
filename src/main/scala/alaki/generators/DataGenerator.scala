package alaki.generators
import scala.util.Random

trait DataGenerator {
  val random: Random

  def randomItem[A](items: Seq[A]): A =
    items(random.nextInt(items.size))

  def randomItems[A](count: Int)(items: Seq[A]): Seq[A] =
    Random.shuffle(items).take(count)
}
