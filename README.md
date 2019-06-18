Phony
==========

Phony is fake data generate with `cats` effect library.

[![Build Status](https://travis-ci.com/alirezameskin/phony.svg?branch=master)](https://travis-ci.com/alirezameskin/phony)


## Quick Start

To use Phony in an existing SBT project with Scala 2.11 or a later version, add the following dependency to your build.sbt:

```scala
resolvers += "phony" at "https://dl.bintray.com/meskin/phony/"

libraryDependencies += "com.github.alirezameskin" %% "phony" % "0.2.2-snapshot"
```

## Examples

```scala
  import cats.effect.IO
  import phony.{Phony, Locale}
  
  implicit val locale = Locale.ENGLISH
  
  val bio = Phony[IO].lorem.text
  
  println(bio.unsafeRunSync)
```

### Using with [`fs2`](https://fs2.io/)

```scala
import cats.effect.IO
import fs2._
import phony.{Locale, Phony}

case class Contact(firstName :String, lastName : String, age:Int)

implicit val locale = Locale.ENGLISH
val phony = Phony[IO]

val contact = for {
  firstName <- phony.name.firstName
  lastName  <- phony.name.lastName
  age       <- phony.alphanumeric.number(18, 68)
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

### Name

```scala
  def firstName: F[String]
  def lastName: F[String]
  def prefix: F[String]
  def suffix: F[String]
  def fullName: F[String]
  def fullName(withPrefix: Boolean = false, withSuffix: Boolean = false): F[String]
  def username: F[String]
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
```

