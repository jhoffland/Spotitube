# Spotitube
*Door Joep Hoffland (HAN601552)*


## 1. Omschrijving
Spotitube is een gezamelijke app van Spotify en YouTube warmee een klant een overzicht kan krijgen van afspeellijsten met daarin audio- en videostreams.

Voor het vak DEA binnen het OOSE-semester aan de HAN-ICA heb ik de opdracht gekregen om de back-end te ontwikkelen. Deze applicatie moet gebruik maken van *JAX-RS*, *CDI (Context & Dependency injection)*, de *JDBC API* en gedeployed kunnen worden op Apache TomEE Plus. De [client](https://hanica-dea.github.io/spotitube/) (front-end) is aangeleverd door DEA en moet RESTful kunnen communiceren met de back-end volgens de [REST API specificatie](https://github.com/HANICA-DEA/spotitube#api).

De applicatie heb ik gerealiseerd op basis van de causbeschrijving, waarbij ik gebruik heb gemaakt van een *Data access layer*, het *Domain layer pattern* en het *Remote Facade pattern*. Daarnaast wordt 100% van de code via *Unittests* getest en is dit document een toelichting van de applicatie.


## 2. Package diagram
![Package diagram Spotitube](https://raw.githubusercontent.com/joephoffland/Spotitube/master/Documentatie/Package%20diagram%20Spotitube.png?token=AgYwFc2mIiyYNgh6LUKzXVM6yzz30HYVks5crLDmwA%3D%3D)

### 2.1 Toelichting

#### 2.1.1 Context & Dependency injection
Door gebruik te maken van Dependency injection worden de dependencies door de container "injected" tijdens runtime, waardoor het onder andere makkelijker wordt om te wisselen naar een nieuwe implementatie. CDI wordt toegepast op alle DAO objecten die gebruikt worden in de `rest.endpoints` package, door gebruik te maken van de JAX `@Inject` annotatie.

#### 2.1.2 Layer en Remote Facade pattern
**Layer pattern**: Het endpoint vraagt aan de DAO (Data Access Object) uit de *Datasource layer* informatie uit de database op. Vervolgens vult de DAO een Domain-object uit de *Domain Layer* en retourneert deze aan het endpoint.<br />
**Remote Facade pattern**: Het endpoint zet het Domain-object om naar een DTO (Data Transfer Object) die als HTTP-response wordt teruggegeven aan de client (Remote Facade pattern).

#### 2.1.3 Open Closed principle (OCP) - SOLID
Elke DAO implementeert een interface. Deze interface wordt ook in de `rest.endpoints` gebruikt om het type object aan te geven.

## 3. Deployment diagram
![Deployment diagram Spotitube](https://raw.githubusercontent.com/joephoffland/Spotitube/master/Documentatie/Deployment%20Diagram%20Spotitube.png?token=AgYwFfz9Y415ZhCtQZCeeKc_34S3fz94ks5crLChwA%3D%3D)
