Phony
==========

Phony is fake data generate with `cats` library.

[![Build Status](https://travis-ci.com/alirezameskin/phony.svg?branch=master)](https://travis-ci.com/alirezameskin/phony)
[![codecov](https://codecov.io/gh/alirezameskin/phony/branch/master/graph/badge.svg)](https://codecov.io/gh/alirezameskin/phony)

## Using as a tool

```bash
#Generate 10 random names
phony -l en -t "{{ contact.firstName }} {{ contact.lastName }}" -c 10
```

```bash
echo 'INSERT INTO users (first_name, last_name, age) VALUES("{{ contact.firstName }}", "{{ contact.lastName }}", {{alphanumeric.number(18, 68)}});' > /tmp/tpl.txt

phony -f /tmp/tpl.txt -c 10
```

## Using as a library

To use Phony in an existing SBT project with Scala 2.12 and 2.13, add the following dependency to your build.sbt:

```sbtshell
resolvers += "phony" at "https://dl.bintray.com/meskin/phony/"

libraryDependencies += "com.github.alirezameskin" %% "phony-core" % "0.4.0"
```

## Examples

```scala
  import phony.Phony
  import scala.util.Try
  import phony.instances.try_.languages.ENGLISH
  import phony.instances.try_._

  val bio = Phony[Try].lorem.text
  println(bio)
```

```scala

  import phony.Phony
  import phony.instances.either.languages.ENGLISH
  import phony.instances.either._

  type EitherA[A] = Either[Throwable, A]
  val bio = Phony[EitherA].lorem.text
  println(bio)
```

### Using with [`cats-effect`](https://typelevel.org/cats-effect/)

```sbtshell
resolvers += "phony" at "https://dl.bintray.com/meskin/phony/"

libraryDependencies += "com.github.alirezameskin" %% "phony-cats-effect" % "0.4.0"
```

```scala
  import cats.effect.IO
  import phony.Phony
  import phony.cats.effect.instances.io.languages.ENGLISH
  import phony.cats.effect.instances.io._

  val bio = Phony[IO].lorem.text
  println(bio.unsafeRunSync())
```

### Using with [`fs2`](https://fs2.io/)

```scala
  import cats.effect.IO
  import fs2._
  import phony.Phony
  import phony.cats.effect.instances.io.languages.ENGLISH
  import phony.cats.effect.instances.io._

  case class Contact(firstName :String, lastName : String, age:Int)

  val contact = for {
    firstName <- Phony[IO].contact.firstName
    lastName  <- Phony[IO].contact.lastName
    age       <- Phony[IO].alphanumeric.number(18, 68)
  } yield Contact(firstName, lastName, age)

  val stream = Stream.eval(contact)
  stream.repeat.take(10).map(println).compile.drain.unsafeRunSync
```

## Generators

### Alphanumeric

```scala
  def char: F[Char]
  def boolean: F[Boolean]
  def float: F[Float]
  def number: F[Int]
  def number(max: Int)
  def number(min: Int, max: Int): F[Int]
  def custom(format: String) //format replace # with random number and replace ? with random character
  def hash: F[String]
  def hash(length: Int): F[String]
```

### Calendar 

```scala
  def year: F[Int]
  def day: F[String]
  def month: F[String]
  def time24h: F[String]
  def time12h: F[String]
  def date: F[Date]
  def date(format: String = "yyyy-MM-dd"): F[Date]
  def iso8601:F[String]
  def timezone: F[String]
```  

### Contact 

```scala
  def firstName: F[String]
  def lastName: F[String]
  def prefix: F[String]
  def suffix: F[String]
  def fullName: F[String]
  def fullName(withPrefix: Boolean = false, withSuffix: Boolean = false): F[String]
  def username: F[String]
  def phone: F[String]
  def cellPhone: F[String]
```

### Internet

```scala
  def uuid: F[String]
  def email: F[String]
  def password: F[String]
  def domain: F[String]
  def hostname: F[String]
  def protocol: F[String]
  def url: F[String]
  def ip: F[String]
  def ipv6: F[String]
  def hashtag: F[String]
```

### Lorem

```scala
  def word: F[String]
  def words: F[List[String]]
  def words(length: Int): F[List[String]]
  def sentence: F[String]
  def text: F[String]
  def sentence(size: Int): F[String]
  def text(lines: Int): F[String]
```


### Location

```scala
  def latitude: F[String]
  def longitude: F[String]
  def country: F[phony.data.Country]
  def countryName: F[String]
  def countryCode: F[String]
  def postalCode: F[String]
```

