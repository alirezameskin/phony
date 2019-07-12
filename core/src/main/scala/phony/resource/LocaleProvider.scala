package phony.resource

import phony.data._

case class LocaleProvider(
  lorem: LoremData,
  names: NameData,
  internet: InternetData,
  calendar: CalendarData,
  location: LocationData,
  contact: ContactData
)
