Feature: Graetzelrad Check availability on date and book in case its available

  Scenario Outline: Successful login with different credentials
    Given the user opens Graetzels booking website handing over <datum>
    And the user can see Modal Window "Privatsphäre", close it via "Alle Dienste erlauben"
    And the user can see "Sofort buchen" in the title
    When the user fills the following details:
      | Field       | Value      |
      | Vorname     | Horst      |
      | Nachname    | Bauer      |
      | Straße      | Leystrasse |
      | Hausnummer  | 2          |
      | Ort         | Wien       |
      | E-Mail      | horst.bauer@aon.at         |
      | Telefon     | 00436524564578             |
    And the user fills DropDown "Geburtsjahr" with "1980"
    And the user clicks "Senden"

    Examples:
      | datum |
      | 11.06.2026 |