Feature: Users test

  Scenario: Inloggen geeft een jwt token
    When Ik inlog met "bram@bramsierhuis.nl" "test"
    Then Is de status van het request 200
    And Krijg ik een jwt token

  Scenario: Ophalen van alle users geeft status 200
    When Ik alle users ophaal
    Then Is de status van het request 200

  Scenario: Aanmaken van een nieuwe user geeft status 201
    When Ik een nieuwe user aanmaak
    Then Is de status van het request 201

  Scenario: Aanmaken van een bestaande user geeft status 409
    When Ik een bestaande user aanmaak
    Then Krijg ik een error 409

  Scenario: Inactief maken van een bestaande user geeft status 200
    When Ik een bestaande user inactief maak
    Then Is de status van het request 200
    
  Scenario: Ophalen van een bestaande inactieve user geeft een inactive user en status 200
    When Ik een bestaande en inactieve user ophaal
    Then Is de status van het request 200
    And Is de status van de gebruiker inactive

  Scenario: Updaten van een bestaande user geeft status 200
    When Ik een bestaande user update
    Then Is de status van het request 200

  Scenario: Updaten van een niet bestaande user geeft status 400
    When Ik een niet bestaande user update
    Then Krijg ik een error 400