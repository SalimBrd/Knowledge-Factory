Application de Formation

Résumé :
Site MOOC permettant de tester ses compétences.
Une personne arrive sur le site, l'accueil lui permet de voir les services proposés.
Il a le choix de se connecter pour pouvoir accéder aux fonctionnalités de l'application.

Une fois connecté l'utilisateur peut visionner des cours en ligne, tester ses connaissances
avec des questionnaires à choix unique ou multiple. Une fois une formation terminée, l'utilisateur
est récompensé par une certification.
Plus value du site : 
- Vente de livres complémentaires avec certains cours du site.
  Achetables directement sur la plateforme.
- Mise en place d'un dispositif d'atelier en ligne interactif, avec un professionnel du domaine.
  L'objectif : 2h de créneau dans la semaine durant laquelle un professionnel va effectuer un cours sur
  une thématique. (Bonus)
  La thématique serait choisie de cette manière, un sondage hebdomadaire est publié sur le site
  aux non-abonnés et aux abonnés, 4 choix de thèmes, celui qui emporte le plus de votes sera le 
  sujet de la semaine.
  La page de sondage contiendra, le sondage en cours, et les résultats du sondage précédent

L'abonnement donnerait ces avantages :
- Plus de ressources à disposition (livres, etc...).
- Plus de cours en ligne.
- Un autre atelier en ligne de 2h à destination des abonnés.
- Priorité sur le support en ligne. (Bonus)

-- Nouveau rôle --

Abonné

-- Nouvelles tables --

Course (course_id, suggested_hours, difficulty, premium)
Questionnaire (quiz_id, title, description, course_id)
Question (question_id, content, order, multipleAnswers, quiz_id)
Answer (answer_id, content, order, success, question_id)
QuestionnaireSuccess (quiz_success_id, quiz_id, user_id)
Diploma (diploma_id, user_id, course_id)
Book (book_id, title, description, author, price, premium)
Survey (survey_id, title, description, premium)
Survey_choices (survey_choice_id, content, survey_id)
Survey_answers (survey_answer_id, survey_choice_id, user_id)

-- Nouvelles fonctionnalités --

- Cours en ligne.
- Génération gratuite d'un certificat de réussite de formation.
- Achat de livres.
- Ateliers interactifs avec sondage pour choisir le thème.
- Abonnement pour des avantages exclusifs.

-- Schéma SQL --

course :  Route Endpoint /api/course
  id: auto-generated unique
  title: String (Not Null, Max Length = 100)
  suggested_hours: String (Not Null)
  difficulty: Difficulty (Enum: default value EASY)
  premium: Boolean (0 or 1)
  creationDate: Date
  updatedDate: Date

questionnaire : Route Endpoint /api/questionnaire
  id: auto-generated unique
  title: String (Not Null)
  description: String (Not Null)
  placement: Integer (Not Null)
  Course: Many-To-One
  creationDate: Date
  updatedDate: Date

question : Route Endpoint /api/question
  id: auto-generated unique
  content: String (Not Null)
  placement: Integer (Not Null)
  multipleAnswers: Boolean (0 or 1 Not Null)
  Questionnaire: Many-To-One
  creationDate: Date
  updatedDate: Date

answer : Route Endpoint /api/answer
  id: auto-generated unique
  content: String (Not Null)
  placement: Integer (Not Null)
  success: Boolean (0 or 1 Not Null)
  Question: Many-To-One
  creationDate: Date
  updatedDate: Date

questionnaire_success : Route Endpoint /api/questionnaire/success
  id: auto-generated unique
  User: Many-To-Many
  Questionnaire: Many-To-Many
  creationDate: Date
  updatedDate: Date

diploma : Route Endpoint /api/diploma
  id: auto-generated unique
  User: Many-To-Many
  Course: Many-To-Many
  creationDate: Date
  updatedDate: Date

book : Route Endpoint /api/book
  id: auto-generated unique
  title: String (Not Null, Max Length = 100)
  description: String (Not Null)
  author: String (Not Null, Max Length = 40) 
  price: Decimal(3,2) (Not Null)
  premium: Boolean (0 or 1 Not Null)
  creationDate: Date
  updatedDate: Date

book_order : Route Endpoint /api/book/order
  id: auto-generated unique
  User: Many-To-Many
  Book: Many-To-Many
  creationDate: Date
  updatedDate: Date

survey : Route Endpoint /api/survey
  id: auto-generated unique
  title: String (Not Null, Max Length = 100)
  description: String (Not Null)
  premium: Boolean (0 or 1 Not Null)
  activeDate: Date (unique Not Null)
  creationDate: Date
  updatedDate: Date

survey_choice : Route Endpoint /api/survey/choice
  id: auto-generated unique
  content: String (Not Null)
  Survey: Many-To-One
  creationDate: Date
  updatedDate: Date

survey_answer : Route Endpoint /api/survey/answer
  id: auto-generated unique
  User: Many-To-Many
  SurveyChoice: Many-To-Many
  creationDate: Date
  updatedDate: Date

TODO

-- Back --

Rajouter le nouveau rôle                                                           OK
Créer les nouvelles tables et vérifier si le describe donnent le résultat souhaité OK
Création des nouveaux services/controllers                                         OK
Création des CRUD                                                                  OK
Faire les bons codes retours                                                       OK
Tester en amont les routes avec postman                                            OK
Faire les tests unitaires                                                          OK

-- Front --

Créer les pages suivantes :
-- FrontOffice -- 
Homepage pour tous les utilisateurs             /                                  OK
Page des courses (pagination)                   /courses                           OK
                    page sondages               /survey                            OK
Page Questionnaire                              /course/:course_id/quiz/:quiz_id   OK
Page Cours                                      /course/:course_id/cours-url       OK
Page Achat de livres                            /book                              OK
Page Livre                                      /book/:book_id                     OK
Page Panier                                     /cart                              KO
Page Achat                                      /purchase                          OK

-- BackOffice --
Affichage de tout les questionnaire avec CRUD   /admin/questionnaire               KO
Edition d'un questionnaire                      /admin/questionnaire/:id           KO
Affichage de toutes les courses avec CRUD       /admin/course                      KO
Edition d'une course                            /admin/course/:id                  KO
Affichage de toutes les questions avec CRUD     /admin/question                    KO
Affichage de toutes les réponses avec CRUD      /admin/answer                      KO
Affichage de tous les sondages                  /admin/survey                      KO
Edition d'un sondage                            /admin/survey/:id                  KO
Affichage de tous les livres                    /admin/book                        KO
Edition d'un livre                              /admin/book/:id                    KO

-- Bonus --
Ajouter les boutons pour retourner en arrière  OK
Ajouter un alertService                        KO
Ajouter un footer                              OK
Embellir le site                               OK
Rendre plus beau le bouton dans error 404      OK
Faire des onglets pour filtrer les premiums    OK


UPDATE 10/04/2021
- Rajouter la partie Admin

