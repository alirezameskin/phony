package alaki.generators

import cats.effect.IO
import org.scalamock.scalatest.MockFactory
import org.scalatest.FunSuite

import scala.util.Random

class AlphanumericGeneratorSpec extends FunSuite with MockFactory {

  val random = mock[Random]
  val generator = new AlphanumericGenerator[IO](random)

  test("It should return one boolean if boolean method is called") {
    (random.nextBoolean _).expects().returning(false)

    generator.boolean.map(bool => assert(bool == false)).unsafeRunSync
  }

  test("It should generate a float number if float method is called") {
    (random.nextFloat _).expects().returning(0.20386976F)

    generator.float.map(float => assert(float == 0.20386976F)).unsafeRunSync
  }

  test("It should generate an integer if number method is called") {
    (random.nextInt(_: Int)).expects(100000).returning(4560)

    generator.number.map(num => assert(num == 4560)).unsafeRunSync
  }

  test("It should generate an integer less than 100 ") {
    (random.nextInt(_: Int)).expects(100).returning(60)

    generator.number(100).map(num => assert(num == 60)).unsafeRunSync
  }

  test("It should generate an integer between 100 and 600") {
    (random.nextInt(_ : Int)).expects(500).returning(345)

    generator.number(100, 600).map(num => assert(num == 445)).unsafeRunSync
  }
}
