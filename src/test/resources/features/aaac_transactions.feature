Feature: Transactions test

  Scenario: Ophalen van alle transactions geeft status 200
    When Ik alle transactions ophaal
    Then Is de status van het request 200

  Scenario: Aanmaken van een nieuwe transaction van 200 geeft status 201
    When Ik een transactie aanmaak van een regular account naar een andere klant
    Then Is de status van het request 201

  Scenario: Ophalen van een bestaande transaction geeft status 200
    When Ik een bestaande transaction ophaal
    Then Is de status van het request 200

  Scenario: Transaction van een savings account naar een account van een andere klant geeft status 409
    When Ik een transaction maak van een savings account naar een andere klant
    Then Krijg ik een error 409

  Scenario: Transaction van een normaal account naar andermans savings account geeft status 409
    When Ik een transaction maak van een normaal account naar andersmans savings account
    Then Krijg ik een error 409

  Scenario: Transaction uitvoeren als user voor andermans account geeft status 409
    When Ik een transaction aanmaak als user voor andermans account
    Then Krijg ik een error 409
    
  Scenario: Transaction overschrijdt de transaction limiet
    When Ik teveel geld probeer over te maken
    Then Krijg ik een error 409

  Scenario: Transaction uitvoeren die de absolute limit zou overschrijden geeft status 409
    When Ik niet genoeg geld heb
    Then Krijg ik een error 409

  Scenario: Transaction uitvoeren die de daglimiet aan transacties overschreidt geeft status 409
    When Ik te veel hoge transacties uitvoer
    Then Krijg ik een error 409

  Scenario: Transaction uitvoeren door een inactief account
    When Ik een transactie aanmaak vanaf een inactief account
    Then Krijg ik een error 403