Feature: Users test

  Scenario: Inloggen geeft een jwt token
    When Ik inlog met "bram@bramsierhuis.nl" "test"
    Then Is de status van het request 200
    And Krijg ik een jwt token

  Scenario: Ophalen van alle users geeft status 200
    When Ik alle users ophaal
    Then Is de status van het request 200