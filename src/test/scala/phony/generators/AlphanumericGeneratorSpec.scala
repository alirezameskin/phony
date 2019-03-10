package phony.generators

import cats.effect.IO
import org.scalamock.scalatest.MockFactory
import org.scalatest.FunSuite
import phony.RandomUtility

class AlphanumericGeneratorSpec extends FunSuite with MockFactory {

  implicit val random = mock[RandomUtility[IO]]
  val generator = new AlphanumericGenerator[IO]

  test("It should generate a string with ###??? format") {
    (random.bothify(_: String)).expects("###???").returning(IO("368ERD"))

    generator.custom("###???").map(code => assert(code == "368ERD"))
  }

  test("It should generate a character") {
    (random.nextChar _).expects().returning(IO('F'))

    generator.char.map(char => assert(char == 'F')).unsafeRunSync
  }

  test("It should return one boolean if boolean method is called") {
    (random.nextBoolean _).expects().returning(IO(false))

    generator.boolean.map(bool => assert(bool == false)).unsafeRunSync
  }

  test("It should generate a float number if float method is called") {
    (random.nextFloat _).expects().returning(IO(0.20386976F))

    generator.float.map(float => assert(float == 0.20386976F)).unsafeRunSync
  }

  test("It should generate an integer if number method is called") {
    (random.nextInt(_: Int)).expects(100000).returning(IO(4560))

    generator.number.map(num => assert(num == 4560)).unsafeRunSync
  }

  test("It should generate an integer less than 100 ") {
    (random.nextInt(_: Int)).expects(100).returning(IO(60))

    generator.number(100).map(num => assert(num == 60)).unsafeRunSync
  }

  test("It should generate an integer between 100 and 600") {
    (random.nextInt(_: Int)).expects(500).returning(IO(345))

    generator.number(100, 600).map(num => assert(num == 445)).unsafeRunSync
  }
}
