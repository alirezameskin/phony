package alaki

package object data {
  case class NameData(
    firstNames: Vector[String],
    lastNames: Vector[String],
    prefixes: Vector[String],
    suffixes: Vector[String]
  )

  case class InternetData(emailDomains: Vector[String], domainSuffixes: Vector[String])

  case class CalendarData(days: Vector[String], months: Vector[String])

  case class LoremData(words: Vector[String])

  case class Country(name: String, code: String)
  case class LocationData(countries: Vector[Country])
}
