Pflichtenheft Lagerverwaltung
=============================

Projektbezeichnung |<span style="font-weight:100">Lagerverwaltung</span>
------------------ | ---------------
**Projektleiter**  | Stefan Leithenmayr
**Erstellt am**    | 16. November 2017
**Mitwirkende**    | Rene Deicker, Maximilian Hofer
**Zuletzt geändert** | 11. Februar 2018

Änderungsverzeichnis
-----

Nr. | Datum | Version | Geänderte Kapitel | Beschreibung der Änderung | Autor
------------------ | ---------------|---|---|---|--
1 | 18. November 2017 | 0.1 | Alle | Erstellung | Stefan Leithenmayr
2 | 25. November 2017 | 0.2 | Alle | Erweiterung| Stefan Leithenmayr
3 | 26. November 2017 | 0.3 | Abnahmekriterien, Lieferumfang | Erweiterung| Maximilian Hofer
4 | 1. Dezember 2017  | 0.4 | Alle + Diagramme | Erweiterung | René Deicker, Stefan Leithenmayr
5 | 5. Februar 2018  | 0.5 | Alle | Überarbeitung | René Deicker
6 | 6. Februar 2018  | 0.6 | Alle | Überarbeitung | Maximilian Hofer
7 | 10. Februar 2018 | 0.7 | Klassendiagramm + Use-Case-Diagram | Überarbeitung | Maximilian Hofer
8 | 11. Februar 2018 | 0.8 | Beschreibung der Geschäftsprozesse | Überarbeitung | Maximilian Hofer
9 | 11. Feburar 2018 | 0.9 | Glossar | Überarbeitung | Maximilian Hofer

Inhaltsverzeichnis
========

> - Motivation
> - Ausgangssituation und Zielsetzung
>    - Ausgangssituation
>    - Ist - Zustand 
>    - Beschreibung des Problembereiches
>    - Glossar
>    - Modell des Problembereiches
>    - Beschreibung des Geschäftsfeldes
>    - Beschreibung der Geschäftsprozesse
>    - Zielbestimmung
> - Funktionale Anforderungen
>    - Use Case Diagramm
>    - GUI
> - Nicht funktionale Anforderungen
> - Mengengerüst
> - Lieferumfang
> - Abnahmekriterien
> - Literaturverzeichnis

1.Motivation
================

Unser Projekt wird im Rahmen des Unterrichtfaches Projektentwicklung durchgeführt.
Unsere Motivation ist, dass wir unsere Java - Kenntnisse vertiefen möchten.
Weiteres ist unsere Hauptmotivation möglichst viel zu lernen, 
bezüglich der Durchführung eines Projektes.

2.Ausgangssituation und Zielsetzung
============

2.1 Ausgangssituation
---------------------
Unser Projekt wird im Umfeld der HTL-Leonding für das RoboLab erstellt. 


2.1.1 Beschreibung des Problembereiches
---------------------------------------
Das Problem ist, dass man schwer herausfinden kann, welche Gegenstände bei welchen Personen sind. Gegenstände können daher schnell verloren gehen.

2.1.2 Glossar
--------

Fachbegriff | Beschreibung|
------------------ | ---------------|
Leihe | Schüler dürfen Produkte aus dem RoboLab ausleihen
Exemplar | Ausprägung von Produkt
Benutzer | Kunde der unser Programm verwendet
Produkt | Sind die Produkte die der Admin einlagern und die die Schüler ausleihen können

2.1.3 Modell des Problembereiches
---

![CLD Diagram](./images/Klassendiagramm_Lagerverwaltung.jpg)

2.1.4 Beschreibung des Geschäftsfeldes
---

![UC Diagram](./images/USE_Case_Diagram.JPG)

2.1.5 Beschreibung der Geschäftsprozesse
---------------------------------------

Name des Geschäftsprozesses | Auslösendes Ereignis | Ergebnis | Mitwirkende
------------------ | ---------------|----------|-----------------
Produkte ausleihen| Produkt wird vom Schüler ausgeliehen  | Produkt wird im Lagerbestand entfernt | Schüler
Lagerbestand zeigen | Admin/Schüler möchte wissen, was sich noch im Lager befindet und was ausgeliehen wurde | Lagerstand wird gezeigt | Admin und Schüler
Produkte zurückgeben | Schüler gibt ein ausgeliehenes Produkt zurück | Produkt wird wieder im Lagerbestand angezeigt | Schüler
Ausgeliehenes anzeigen | Produkt wird vom Schüler ausgeliehen | Ausgeliehene Produkte, sowie dazugehörige Personen werden angezeigt | Admin und Schüler
Produkte hinzufügen | Neues Produkt gekauft/hinzugekommen | Produkt wird in Datenbank hinzugefügt | Admin

2.2 Zielbestimmung
-----------------
Wir erstellen unsere Software um die Verwaltung der Produkte im RoboLab zu vereinfachen.

>- Güter können zeiteffizient ein-/ausgelagert werden
>- Journal eines gewissen Zeitraumes kann auf einen Blick betrachtet werden
>- Aktueller Lagerstand kann dargestellt werden
>- ausgeliehene Produkte können angesehen werden
>- Wer welches Produkt ausgeliehen hat kann dargestellt werden

3.Funktionale Anforderungen
========

3.1 Use Case Diagramm
--------------------

![UC Diagram](./images/USE_Case_Diagram.jpeg)

**Geschäftsprozesse**

> - neue Produkte einlagern
> - Produkte ausleihen
> - aktuell ausgeliehene Produkte ansehen 
> - Zeitraum der ausgeliehenen Produkte ansehen

3.2 GUI
--------
![GUI](./images/GUI.jpeg)

4.Nicht funktionale Anforderungen
==============

> - Das System muss den unautorisierten Zugriff auf die Stammdaten
  verhindern, soweit dies technisch möglich ist
> - SQL - Server
> - Möglichst geringer Ressourcenverbrauch
> - Schnelle Datenabfragen, ansonsten entstehen Wartezeiten


5.Mengengerüst
============

Es fallen die Daten an, die Sie im Kapitel 2.1.4 (Modell des Problembereiches) in dem Klassendiagramm finden.
Die anfallenden Datenmengen kann man in diesem Fall nicht pauschal beurteilen, da wir z.B. die Anzahl der User nicht wisssen.

6.Lieferumfang
===
Im Lieferumfang befindet sich

> - Fertige Software
> - Bedienungsanleitung für die Software

7.Abnahmekriterien
========
Abnahmekriterien sind

> - Läuft die Datenbank ordnungsgemäß?
> - Funktioniert das Programm entsprechend den Anforderungen?
> - Funktioniert das hinzufügen von Produkten
> - Software muss auf den Pc´s des RoboLab laufen und darf keine Bugs aufweisen

8.Literaturverzeichnis
=====================

Tobias Ambrosch: Die Lagerverwaltung
https://blog.selectline.de/die-lagerverwaltung/ (1.12.2017)
