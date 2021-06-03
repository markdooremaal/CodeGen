Feature: Transactions test

  Scenario: Ophalen van alle transactions geeft status 200
    When Ik alle transactions ophaal
    Then Is de status van het request 200

  Scenario: Aanmaken van een nieuwe transaction geeft status 201
    When Ik een nieuwe transaction aanmaak
    Then Is de status van het request 201
    
  Scenario: Ophalen van een bestaande transaction geeft status 200
    When Ik een bestaande transaction ophaal
    Then Is de status van het request 200

  Scenario: Transaction van een savings account naar een account van een andere klant geeft status 409
    When Ik een transaction maak van een savings account naar een andere klant
    Then Is de status van het request 409

  Scenario: Transaction van een normaal account naar een savings account geeft status 409
    When Ik een transaction maak van een normaal account naar andersmans savings account
    Then Is de status van het request 409

  Scenario: Transaction uitvoeren als user voor andermans account geeft status 409
    When Ik een transaction aanmaak als user voor andermans account
    Then Is de status van het request 409
    
  Scenario: Transaction overschrijdt de transaction limiet
    When Ik teveel geld probeer over te maken
    Then Is de status van het request 409

  Scenario: Transaction uitvoeren die de absolute limit negatief zou maken geeft status 409
    When Ik niet genoeg geld heb
    Then Is de status van het request 409

  Scenario: Transaction uitvoeren die de daglimiet aan transacties overschreidt geeft status 409
    When Ik teveel transacties uitvoer
    Then Is de status van het request 409