# Spotitube
*Door Joep Hoffland (601552)*


## Omschrijving
Spotitube is een gezamelijke app van Spotify en YouTube warmee een klant een overzicht kan krijgen van afspeellijsten met daarin audio- en videostreams.

Voor het vak DEA binnen het OOSE-semester aan de HAN-ICA heb ik de opdracht gekregen om de back-end te ontwikkelen. Deze applicatie moet gebruik maken van *JAX-RS*, *CDI (Context & Dependency injection)*, de *JDBC API* en gedeployed kunnen worden op Apache TomEE Plus. De [client](https://hanica-dea.github.io/spotitube/) (front-end) is aangeleverd door DEA en moet RESTful kunnen communiceren met de back-end volgens de [REST API specificatie](https://github.com/HANICA-DEA/spotitube#api).

De applicatie heb ik gerealiseerd op basis van de causbeschrijving, waarbij ik gebruik heb gemaakt van een *Data access layer*, het *Domain layer pattern* en het *Remote Facade pattern* doormiddel van REST-services. Daarnaast wordt 100% van de code via *Unittests* getest en is dit document een toelichting van de applicatie.


## Package diagram
![Package diagram Spotitube](https://raw.githubusercontent.com/joephoffland/Spotitube/master/Documentatie/Package%20diagram%20Spotitube.png?token=AgYwFc2mIiyYNgh6LUKzXVM6yzz30HYVks5crLDmwA%3D%3D)


## Deployment diagram
![Deployment diagram Spotitube](https://raw.githubusercontent.com/joephoffland/Spotitube/master/Documentatie/Deployment%20Diagram%20Spotitube.png?token=AgYwFfz9Y415ZhCtQZCeeKc_34S3fz94ks5crLChwA%3D%3D)
