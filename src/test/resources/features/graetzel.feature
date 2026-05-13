Feature: Graetzelrad Check availability on date and book in case its available

  Scenario Outline: Successful login with different credentials
    Given the user opens Graetzels booking website handing over <datum>
    When the suer can see Modal Window "Privatsphäre", close it via "Alle Dienste erlauben"
    When the user can see "Sofort buchen" in the title
    When the user fills the following details:
      | Field       | Value      |
      | Vorname     | Horst      |
      | Nachname    | Bauer      |
      | Straße      | Leystrasse |
      | Hausnummer  | 2          |
      | Ort         | Wien       |
      | E-Mail      | horst.bauer@aon.at         |
      | Telefon     | 00436524564578             |
    When the user fills DropDown "Geburtsjahr" with "1980"
    When the user clicks "Senden"

    Examples:
      | datum |
      | 09.06.2026 |