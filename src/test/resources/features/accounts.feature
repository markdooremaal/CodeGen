Feature: Accounts test

  Scenario: Ophalen van alle accounts geeft status 200
    When Ik alle accounts ophaal
    Then Is de status van het request 200

  Scenario: Aanmaken van een nieuw account geeft status 201
    When Ik een nieuw accout aanmaak
    Then Is de status van het request 201

  Scenario: Inactief maken van een bestaand account geeft status 200
    When Ik een bestaand account inactief maak
    Then Is de status van het request 200
    
  Scenario: Ophalen van een bestaand inactief account geeft een inactief account en status 200
    When Ik een bestaand en inactief account ophaal
    Then Is de status van het request 200
    And Is de status van het account inactive

  Scenario: Updaten van een bestaand account geeft status 200
    When Ik een bestaand account update
    Then Is de status van het request 200

  Scenario: Updaten van een niet bestaand account geeft status 400
    When Ik een niet bestaand account update
    Then Krijg ik een error 400