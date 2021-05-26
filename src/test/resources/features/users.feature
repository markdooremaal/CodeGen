Feature: Users test

  Scenario: Ophalen van alle users geeft status 200
    When Ik alle users ophaal
    Then Is de status van het request 200