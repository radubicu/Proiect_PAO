# Platforma de internshipuri pentru studenti

## Tipurile de obiecte
* **StudentProfile**
* **Employer**
* **Job**
* **Webinar**
* **Timeline Field** (abstract), cu implementarile: 
  - **EducationField**
  - **ExperienceField**
  - **ProjectField**
* **InboxMessage**
* Cinci tipuri de exceptii:
  - **EmployerNotFoundException**
  - **IncorrectEmailException**
  - **IncorrentPhoneNumberException**
  - **JobNotFoundException**
  - **StudentNotFoundException**

## Tipurile de interogari/actiuni
* un angajator poate *adauga* si *sterge* job-uri prin metodele **addJob(Job):void** si **removeJob(Job):void**
* un student poate *aplica* la un job prin metoda **applyForJob(Job):void**
* un student poate *renunta* la o aplicatie prin metoda **removeJobApplication(Job):void**
* un job poate fi *setat inactiv* (i.e. oferta nu mai este valabila) prin metoda **setJobActive(boolean):void**
* toti studentii *sunt notificati* cand un job este modificat (Observer Pattern), apelandu-se metoda **notifyProfiles():void**
* toti studentii *sunt notificati* cand un webinar este modificat (Observer Pattern), apelandu-se metoda **notifyProfiles():void**
* un student *primeste un mesaj* in inbox atunci cand este notificat de catre job-ul care s-a modificat **updateObserver(String):void**
* un student poate *citi* mesajele prin metoda **seeUnreadMessages():void**; fiecare mesaj va fi marcat ca fiind *citit* prin metoda **markRead():void**
* un webinar poate fi marcat ca *incheiat* prin metoda **setFinished():void**
* studentii care s-au inscris la un webinar primesc reminder cu o ora inainte de start, prin metoda **sendUpdate():void**

* Clasa serviciu **InternshipService** este singleton si gestioneaza studentii si angajatorii cu job-urile si webinarele lor.

## Colectii
ArrayList si TreeSet

## Mosteniri
EducationField, ExperienceField, ProjectField extind TimelineField
Exceptiile extind Exception

## Interfete
Comparable<T>, Cloneable, Observer / Observable

