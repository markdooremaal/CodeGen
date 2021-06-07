Feature: Transfers test

  Scenario: Ophalen van alle transfers geeft status 200
    When Ik alle transfers ophaal
    Then Is de status van het request 200

  Scenario: Aanmaken van een nieuwe transfers van 200 geeft status 201
    When Ik een deposit transfer aanmaak van 100
    Then Is de status van het request 201

  Scenario: Ophalen van een bestaande transfer geeft status 200
    When Ik een bestaande transfer ophaal
    Then Is de status van het request 200

  Scenario: Transfer voor andermans account geeft error 403
    When Ik een transfer aanmaak als user voor andermans account
    Then Krijg ik een error 403

  Scenario: Transfer uitvoeren die de absolute limit zou overschrijden geeft status 409
    When Ik niet genoeg geld heb voor een transfer
    Then Krijg ik een error 409

  Scenario: Transfer uitvoeren door een inactief account
    When Ik een transfer aanmaak vanaf een inactief account
    Then Krijg ik een error 403

  Scenario: Transfer van een savings account geeft status 403
    When Ik een transfer maak van een savings account
    Then Krijg ik een error 403

  Scenario: Transfer overschrijdt de transaction limiet
    When Ik teveel geld probeer op te nemen
    Then Krijg ik een error 409

  Scenario: Transfer uitvoeren die de daglimiet aan transacties overschreidt geeft status 409
    When Ik te veel hoge transfers uitvoer
    Then Krijg ik een error 409