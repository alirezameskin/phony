Alaki
==========

Alaki is fake data generate with `cats` effect library.

[![Build Status](https://travis-ci.com/alirezameskin/alaki.svg?token=37jaimBVvHpuikyqcM5g&branch=master)](https://travis-ci.com/alirezameskin/alaki)


## Quick Start

To use alaki in an existing SBT project with Scala 2.11 or a later version, add the following dependency to your build.sbt:

```scala
resolvers += "alaki" at "https://dl.bintray.com/meskin/alaki/"

libraryDependencies += "com.github.alirezameskin" % "alaki_2.12" % "0.1-snapshot"
```

## Examples

```scala
  import alaki.{Generator, Locale}
  import cats.effect.IO
  
  implicit val locale = Locale.ENGLISH
  val generator = Generator[IO]

  val bio = for (text <- generator.lorem.text) yield text

  println(bio.unsafeRunSync)
```

### Using with [`fs2`](https://fs2.io/)

```scala
case class Contact(firstName :String, lastName : String, age:Int)

implicit val locale = Locale.ENGLISH
val generator = Generator[IO]

val contact = for {
  firstName <- generator.name.firstName
  lastName  <- generator.name.lastName
  age       <- generator.alphanumeric.number(18, 68)
} yield Contact(firstName, lastName, age)

val contactsStream = Stream.eval(contact)

contactStream.repeatN(10).map(println).compile.drain.unsafeRunSync

```

## Generators

### Alphanumeric

```scala
  def boolean: F[Boolean]
  def float: F[Float]
  def number: F[Int]
  def number(max: Int)
  def number(min: Int, max: Int): F[Int]
```

### Calendar 

```scala
  def year: F[Int]
  def day: F[String]
  def month: F[String]
  def time24h: F[String]
  def time12h: F[String]
  def date: F[Date]
  def date(format: String = "yyyy-MM-dd")
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
  def country: F[alaki.data.Country]
  def countryName: F[String]
  def countryCode: F[String]
```
